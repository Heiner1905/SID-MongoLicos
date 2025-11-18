package com.example.proyectosid.mapper;

import com.example.proyectosid.dto.ProgressCreateDTO;
import com.example.proyectosid.dto.ProgressResponseDTO;
import com.example.proyectosid.model.mongodb.Progress;
import com.example.proyectosid.model.mongodb.ProgressExercise;
import com.example.proyectosid.model.mongodb.ProgressMetrics;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProgressMapper {

    /**
     * Convierte Progress entity → ProgressResponseDTO
     */
    public ProgressResponseDTO toResponseDTO(Progress progress) {
        if (progress == null) return null;

        ProgressResponseDTO dto = new ProgressResponseDTO();
        dto.setId(progress.getId());
        dto.setUserId(progress.getUserId());
        dto.setRoutineId(progress.getRoutineId());
        dto.setCreatedAt(progress.getCreatedAt());
        dto.setCompletedAt(progress.getCompletedAt());

        if (progress.getExercise() != null) {
            dto.setExerciseId(progress.getExercise().getExerciseId());
            dto.setExerciseName(progress.getExercise().getName());
            dto.setExerciseType(progress.getExercise().getType());
        }

        if (progress.getMetrics() != null) {
            dto.setReps(progress.getMetrics().getReps());
            dto.setSets(progress.getMetrics().getSets());
            dto.setWeight(progress.getMetrics().getWeight());
            dto.setTime(progress.getMetrics().getTime());
            dto.setDistance(progress.getMetrics().getDistance());
            dto.setEffortLevel(progress.getMetrics().getEffortLevel());
            dto.setRhythm(progress.getMetrics().getRhythm());
        }

        return dto;
    }

    /**
     * Convierte ProgressCreateDTO → Progress entity
     */
    public Progress toEntity(ProgressCreateDTO dto, String userId) {
        if (dto == null) return null;

        Progress progress = new Progress();
        progress.setUserId(userId);
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

        return progress;
    }
}
