package com.example.proyectosid.controller;

import com.example.proyectosid.dto.TrainerStatisticsDTO;
import com.example.proyectosid.dto.UserStatisticsDTO;
import com.example.proyectosid.services.IStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final IStatisticsService statisticsService;

    /**
     * Obtener todas las estadísticas de un usuario
     * GET /api/statistics/users/{userId}
     */
    @GetMapping("/users/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserStatisticsDTO>> getUserStatistics(@PathVariable String userId) {
        List<UserStatisticsDTO> stats = statisticsService.getUserStatistics(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtener estadísticas de un usuario para un mes específico
     * GET /api/statistics/users/{userId}/month?year=2025&month=11
     */
    @GetMapping("/users/{userId}/month")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserStatisticsDTO> getUserStatisticsForMonth(
            @PathVariable String userId,
            @RequestParam int year,
            @RequestParam int month) {

        UserStatisticsDTO stats = statisticsService.getUserStatisticsForMonth(userId, year, month);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtener top usuarios más activos del mes (solo trainers)
     * GET /api/statistics/users/top?year=2025&month=11&limit=10
     */
    @GetMapping("/users/top")
    @PreAuthorize("hasAuthority('STATISTICS_READ_ALL')")
    public ResponseEntity<List<UserStatisticsDTO>> getTopUsers(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "10") int limit) {

        List<UserStatisticsDTO> topUsers = statisticsService.getTopUsersOfMonth(year, month, limit);
        return ResponseEntity.ok(topUsers);
    }

    /**
     * Obtener todas las estadísticas de un entrenador (solo trainers)
     * GET /api/statistics/trainers/{trainerId}
     */
    @GetMapping("/trainers/{trainerId}")
    @PreAuthorize("hasAuthority('STATISTICS_READ_ALL')")
    public ResponseEntity<List<TrainerStatisticsDTO>> getTrainerStatistics(@PathVariable String trainerId) {
        List<TrainerStatisticsDTO> stats = statisticsService.getTrainerStatistics(trainerId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtener estadísticas de un entrenador para un mes específico
     * GET /api/statistics/trainers/{trainerId}/month?year=2025&month=11
     */
    @GetMapping("/trainers/{trainerId}/month")
    @PreAuthorize("hasAuthority('STATISTICS_READ_ALL')")
    public ResponseEntity<TrainerStatisticsDTO> getTrainerStatisticsForMonth(
            @PathVariable String trainerId,
            @RequestParam int year,
            @RequestParam int month) {

        TrainerStatisticsDTO stats = statisticsService.getTrainerStatisticsForMonth(trainerId, year, month);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtener top entrenadores más activos del mes (solo trainers)
     * GET /api/statistics/trainers/top?year=2025&month=11&limit=10
     */
    @GetMapping("/trainers/top")
    @PreAuthorize("hasAuthority('STATISTICS_READ_ALL')")
    public ResponseEntity<List<TrainerStatisticsDTO>> getTopTrainers(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "10") int limit) {

        List<TrainerStatisticsDTO> topTrainers = statisticsService.getTopTrainersOfMonth(year, month, limit);
        return ResponseEntity.ok(topTrainers);
    }

    /**
     * Forzar cálculo de estadísticas (solo para testing/admin)
     * POST /api/statistics/calculate?year=2025&month=11
     */
    @PostMapping("/calculate")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<String> calculateStatistics(
            @RequestParam int year,
            @RequestParam int month) {

        statisticsService.calculateAndSaveMonthlyStatistics(year, month);
        return ResponseEntity.ok("Estadísticas calculadas para " + year + "/" + month);
    }

    /**
     * Calcular estadísticas del mes actual
     * POST /api/statistics/calculate/current
     */
    @PostMapping("/calculate/current")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<String> calculateCurrentMonthStatistics() {
        statisticsService.calculateCurrentMonthStatistics();
        return ResponseEntity.ok("Estadísticas del mes actual calculadas");
    }
}
