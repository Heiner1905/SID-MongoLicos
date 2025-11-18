package com.example.proyectosid.services.impl.mongodb;

import com.example.proyectosid.dto.ProgressCreateDTO;
import com.example.proyectosid.dto.ProgressResponseDTO;
import com.example.proyectosid.model.mongodb.Progress;
import com.example.proyectosid.model.mongodb.ProgressExercise;
import com.example.proyectosid.model.mongodb.ProgressMetrics;
import com.example.proyectosid.repository.mongodb.ProgressRepository;
import com.example.proyectosid.repository.postgresql.UserRepository;
import com.example.proyectosid.services.mongodb.IProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements IProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    @Override
    public ProgressResponseDTO recordProgress(ProgressCreateDTO dto, String username) {
        // Verificar que el usuario existe
        userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear progreso
        Progress progress = new Progress();
        progress.setUserId(username);
        progress.setRoutineId(dto.getRoutineId());
        progress.setCreatedAt(LocalDateTime.now());
        progress.setCompletedAt(LocalDateTime.now());

        // Exercise info
        ProgressExercise exercise = new ProgressExercise();
        exercise.setExerciseId(dto.getExerciseId());
        exercise.setName(dto.getExerciseName());
        exercise.setType(dto.getExerciseType());
        exercise.setSets(dto.getSets());
        exercise.setReps(dto.getReps());
        progress.setExercise(exercise);

        // Metrics
        ProgressMetrics metrics = new ProgressMetrics();
        metrics.setReps(dto.getReps());
        metrics.setSets(dto.getSets());
        metrics.setWeight(dto.getWeight());
        metrics.setTime(dto.getTime());
        metrics.setDistance(dto.getDistance());
        metrics.setEffortLevel(dto.getEffortLevel());
        metrics.setRhythm(dto.getRhythm());
        progress.setMetrics(metrics);

        Progress saved = progressRepository.save(progress);

        return mapToDTO(saved);
    }

    @Override
    public List<ProgressResponseDTO> getUserProgress(String userId) {
        List<Progress> progress = progressRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return progress.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressResponseDTO> getProgressByRoutine(String userId, String routineId) {
        List<Progress> progress = progressRepository.findByUserIdAndRoutineId(userId, routineId);
        return progress.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressResponseDTO> getProgressByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Progress> progress = progressRepository.findByUserAndDateRange(userId, startDate, endDate);
        return progress.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressResponseDTO> getProgressByExerciseAndDateRange(String userId, String exerciseId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Progress> progress = progressRepository.findByUserExerciseAndDateRange(userId, exerciseId, startDate, endDate);
        return progress.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressResponseDTO> getRecentProgress(String userId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        LocalDateTime endDate = LocalDateTime.now();

        List<Progress> progress = progressRepository.findByUserAndDateRange(userId, startDate, endDate);
        return progress.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProgressResponseDTO mapToDTO(Progress progress) {
        ProgressResponseDTO dto = new ProgressResponseDTO();
        dto.setId(progress.getId());
        dto.setUserId(progress.getUserId());
        dto.setRoutineId(progress.getRoutineId());
        dto.setCreatedAt(progress.getCreatedAt());
        dto.setCompletedAt(progress.getCompletedAt());

        // Exercise
        dto.setExerciseId(progress.getExercise().getExerciseId());
        dto.setExerciseName(progress.getExercise().getName());
        dto.setExerciseType(progress.getExercise().getType());

        // Metrics
        dto.setReps(progress.getMetrics().getReps());
        dto.setSets(progress.getMetrics().getSets());
        dto.setWeight(progress.getMetrics().getWeight());
        dto.setTime(progress.getMetrics().getTime());
        dto.setDistance(progress.getMetrics().getDistance());
        dto.setEffortLevel(progress.getMetrics().getEffortLevel());
        dto.setRhythm(progress.getMetrics().getRhythm());

        return dto;
    }
}
