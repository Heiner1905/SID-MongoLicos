package com.example.proyectosid.services.impl;

import com.example.proyectosid.dto.TrainerStatisticsDTO;
import com.example.proyectosid.dto.UserStatisticsDTO;
import com.example.proyectosid.model.postgresql.TrainerStatistics;
import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.model.postgresql.UserStatistics;
import com.example.proyectosid.repository.mongodb.AssignmentRepository;
import com.example.proyectosid.repository.mongodb.ProgressRepository;
import com.example.proyectosid.repository.mongodb.RecommendationRepository;
import com.example.proyectosid.repository.mongodb.RoutineRepository;
import com.example.proyectosid.repository.postgresql.TrainerStatisticsRepository;
import com.example.proyectosid.repository.postgresql.UserRepository;
import com.example.proyectosid.repository.postgresql.UserStatisticsRepository;
import com.example.proyectosid.services.IStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements IStatisticsService {

    private final UserStatisticsRepository userStatisticsRepository;
    private final TrainerStatisticsRepository trainerStatisticsRepository;
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final ProgressRepository progressRepository;
    private final AssignmentRepository assignmentRepository;
    private final RecommendationRepository recommendationRepository;

    @Override
    public List<UserStatisticsDTO> getUserStatistics(String userId) {
        List<UserStatistics> stats = userStatisticsRepository.findByUserIdOrderByYearDescMonthDesc(userId);
        return stats.stream()
                .map(this::mapUserStatsToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatisticsDTO getUserStatisticsForMonth(String userId, int year, int month) {
        UserStatistics stats = userStatisticsRepository.findByUserIdAndYearAndMonth(userId, year, month)
                .orElse(null);
        return stats != null ? mapUserStatsToDTO(stats) : null;
    }

    @Override
    public List<UserStatisticsDTO> getTopUsersOfMonth(int year, int month, int limit) {
        List<UserStatistics> stats = userStatisticsRepository.findTopUsersByMonth(year, month, limit);
        return stats.stream()
                .map(this::mapUserStatsToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainerStatisticsDTO> getTrainerStatistics(String trainerId) {
        List<TrainerStatistics> stats = trainerStatisticsRepository.findByTrainerIdOrderByYearDescMonthDesc(trainerId);
        return stats.stream()
                .map(this::mapTrainerStatsToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TrainerStatisticsDTO getTrainerStatisticsForMonth(String trainerId, int year, int month) {
        TrainerStatistics stats = trainerStatisticsRepository.findByTrainerIdAndYearAndMonth(trainerId, year, month)
                .orElse(null);
        return stats != null ? mapTrainerStatsToDTO(stats) : null;
    }

    @Override
    public List<TrainerStatisticsDTO> getTopTrainersOfMonth(int year, int month, int limit) {
        List<TrainerStatistics> stats = trainerStatisticsRepository.findTopTrainersByMonth(year, month, limit);
        return stats.stream()
                .map(this::mapTrainerStatsToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void calculateAndSaveMonthlyStatistics(int year, int month) {
        log.info("📊 Iniciando cálculo de estadísticas para {}/{}", year, month);

        // Calcular estadísticas de usuarios
        calculateUserStatistics(year, month);

        // Calcular estadísticas de entrenadores
        calculateTrainerStatistics(year, month);

        log.info("✅ Estadísticas calculadas y guardadas correctamente para {}/{}", year, month);
    }

    @Override
    public void calculateCurrentMonthStatistics() {
        YearMonth currentMonth = YearMonth.now();
        calculateAndSaveMonthlyStatistics(currentMonth.getYear(), currentMonth.getMonthValue());
    }

    private void calculateUserStatistics(int year, int month) {
        // Obtener rango de fechas del mes
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Obtener todos los usuarios
        List<User> users = userRepository.findAll();

        for (User user : users) {
            String userId = user.getUsername();

            // Contar rutinas iniciadas en el mes
            Long routinesStarted = routineRepository.countRoutinesCreatedByUserInMonth(
                    userId, startOfMonth, endOfMonth);

            // Contar registros de progreso en el mes
            Long progressLogs = progressRepository.countProgressInMonth(
                    userId, startOfMonth, endOfMonth);

            // Buscar o crear estadística
            UserStatistics stats = userStatisticsRepository
                    .findByUserIdAndYearAndMonth(userId, year, month)
                    .orElse(new UserStatistics());

            stats.setUserId(userId);
            stats.setYear(year);
            stats.setMonth(month);
            stats.setRoutinesStarted(routinesStarted.intValue());
            stats.setProgressLogsCount(progressLogs.intValue());
            stats.setUpdatedAt(LocalDateTime.now());

            if (stats.getCreatedAt() == null) {
                stats.setCreatedAt(LocalDateTime.now());
            }

            userStatisticsRepository.save(stats);

            log.info("📈 Usuario {}: {} rutinas, {} progresos", userId, routinesStarted, progressLogs);
        }
    }

    private void calculateTrainerStatistics(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<User> trainers = userRepository.findByRole("trainer");

        for (User trainer : trainers) {
            String trainerId = trainer.getUsername();

            // ✅ CORREGIR: Contar asignaciones creadas en el mes
            Long newAssignments = assignmentRepository.countAssignmentsByTrainerInPeriod(
                    trainerId, startOfMonth, endOfMonth);

            // ✅ CORREGIR: Contar recomendaciones enviadas en el mes
            Long recommendations = recommendationRepository.countRecommendationsByTrainerInPeriod(
                    trainerId, startOfMonth, endOfMonth);

            TrainerStatistics stats = trainerStatisticsRepository
                    .findByTrainerIdAndYearAndMonth(trainerId, year, month)
                    .orElse(new TrainerStatistics());

            stats.setTrainerId(trainerId);
            stats.setYear(year);
            stats.setMonth(month);
            stats.setNewAssignmentsCount(newAssignments.intValue());
            stats.setRecommendationsCount(recommendations.intValue());
            stats.setUpdatedAt(LocalDateTime.now());

            if (stats.getCreatedAt() == null) {
                stats.setCreatedAt(LocalDateTime.now());
            }

            trainerStatisticsRepository.save(stats);

            log.info("👨‍🏫 Entrenador {}: {} asignaciones, {} recomendaciones",
                    trainerId, newAssignments, recommendations);
        }
    }


    private UserStatisticsDTO mapUserStatsToDTO(UserStatistics stats) {
        return UserStatisticsDTO.builder()
                .id(stats.getId())
                .userId(stats.getUserId())
                .year(stats.getYear())
                .month(stats.getMonth())
                .routinesStarted(stats.getRoutinesStarted())
                .progressLogsCount(stats.getProgressLogsCount())
                .createdAt(stats.getCreatedAt())
                .updatedAt(stats.getUpdatedAt())
                .build();
    }

    private TrainerStatisticsDTO mapTrainerStatsToDTO(TrainerStatistics stats) {
        return TrainerStatisticsDTO.builder()
                .id(stats.getId())
                .trainerId(stats.getTrainerId())
                .year(stats.getYear())
                .month(stats.getMonth())
                .newAssignmentsCount(stats.getNewAssignmentsCount())
                .recommendationsCount(stats.getRecommendationsCount())
                .createdAt(stats.getCreatedAt())
                .updatedAt(stats.getUpdatedAt())
                .build();
    }
}
