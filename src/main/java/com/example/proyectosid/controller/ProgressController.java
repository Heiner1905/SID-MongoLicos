package com.example.proyectosid.controller;

import com.example.proyectosid.dto.ProgressCreateDTO;
import com.example.proyectosid.dto.ProgressResponseDTO;
import com.example.proyectosid.services.mongodb.IProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final IProgressService progressService;

    /**
     * Registrar nuevo progreso
     * POST /api/progress
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PROGRESS_CREATE')")
    public ResponseEntity<ProgressResponseDTO> createProgress(
            @Valid @RequestBody ProgressCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        ProgressResponseDTO created = progressService.recordProgress(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtener todo el progreso de un usuario
     * GET /api/progress/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('PROGRESS_READ_OWN') or hasAuthority('STATISTICS_READ_ALL')")
    public ResponseEntity<List<ProgressResponseDTO>> getUserProgress(@PathVariable String userId) {
        List<ProgressResponseDTO> progress = progressService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }

    /**
     * Obtener progreso de una rutina específica
     * GET /api/progress/routine/{routineId}
     */
    @GetMapping("/routine/{routineId}")
    @PreAuthorize("hasAuthority('PROGRESS_READ_OWN')")
    public ResponseEntity<List<ProgressResponseDTO>> getProgressByRoutine(
            @RequestParam String userId,
            @PathVariable String routineId) {

        List<ProgressResponseDTO> progress = progressService.getProgressByRoutine(userId, routineId);
        return ResponseEntity.ok(progress);
    }

    /**
     * Obtener progreso reciente (últimos N días)
     * GET /api/progress/recent
     */
    @GetMapping("/recent")
    @PreAuthorize("hasAuthority('PROGRESS_READ_OWN')")
    public ResponseEntity<List<ProgressResponseDTO>> getRecentProgress(
            @RequestParam String userId,
            @RequestParam(defaultValue = "7") int days) {

        List<ProgressResponseDTO> progress = progressService.getRecentProgress(userId, days);
        return ResponseEntity.ok(progress);
    }

    /**
     * Obtener progreso por rango de fecha y ejercicio
     * GET /api/progress/filter
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('PROGRESS_READ_OWN')")
    public ResponseEntity<List<ProgressResponseDTO>> getProgressByDateRangeAndExercise(
            @RequestParam String userId,
            @RequestParam(required = false) String exerciseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<ProgressResponseDTO> progress;

        if (exerciseId != null && !exerciseId.isEmpty()) {
            progress = progressService.getProgressByExerciseAndDateRange(
                    userId, exerciseId, startDate, endDate);
        } else {
            progress = progressService.getProgressByDateRange(userId, startDate, endDate);
        }

        return ResponseEntity.ok(progress);
    }
}
