package com.example.proyectosid.mapper;

import com.example.proyectosid.dto.RoutineCreateDTO;
import com.example.proyectosid.dto.RoutineExerciseDTO;
import com.example.proyectosid.dto.RoutineResponseDTO;
import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.model.mongodb.RoutineExercise;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoutineMapper {

    /**
     * Convierte Routine entity → RoutineResponseDTO
     */
    public RoutineResponseDTO toResponseDTO(Routine routine) {
        if (routine == null) return null;

        RoutineResponseDTO dto = new RoutineResponseDTO();
        dto.setId(routine.getId());
        dto.setName(routine.getName());
        dto.setDescription(routine.getDescription());
        dto.setUrlImg(routine.getUrlImg());
        dto.setIsPredefined(routine.getIsPredefined());
        dto.setOriginalRoutineId(routine.getOriginalRoutineId());
        dto.setIsCertified(routine.getIsCertified());
        dto.setIsActive(routine.getIsActive());
        dto.setCreatedAt(routine.getCreatedAt());
        dto.setUpdatedAt(routine.getUpdatedAt());

        if (routine.getCreatedBy() != null) {
            dto.setCreatedByUsername(routine.getCreatedBy().getUserId());
            dto.setCreatedByName(routine.getCreatedBy().getName());
        }

        if (routine.getExercises() != null) {
            dto.setExercises(routine.getExercises().stream()
                    .map(this::toExerciseDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Convierte RoutineCreateDTO → Routine entity
     */
    public Routine toEntity(RoutineCreateDTO dto, String username, String fullName) {
        if (dto == null) return null;

        Routine routine = new Routine();
        routine.setName(dto.getName());
        routine.setDescription(dto.getDescription());
        routine.setUrlImg(dto.getUrlImg());
        routine.setIsPredefined(false);
        routine.setIsCertified(false);
        routine.setIsActive(true);
        routine.setCreatedAt(LocalDateTime.now());
        routine.setUpdatedAt(LocalDateTime.now());

        // CreatedBy
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(username);
        createdBy.setName(fullName);
        routine.setCreatedBy(createdBy);

        // Exercises
        if (dto.getExercises() != null) {
            routine.setExercises(dto.getExercises().stream()
                    .map(this::toExerciseEntity)
                    .collect(Collectors.toList()));
        }

        return routine;
    }

    /**
     * Convierte RoutineExercise → RoutineExerciseDTO
     */
    private RoutineExerciseDTO toExerciseDTO(RoutineExercise exercise) {
        if (exercise == null) return null;

        RoutineExerciseDTO dto = new RoutineExerciseDTO();
        dto.setExerciseId(exercise.getExerciseId());
        dto.setName(exercise.getName());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        dto.setDuration(exercise.getDuration());
        return dto;
    }

    /**
     * Convierte RoutineExerciseDTO → RoutineExercise
     */
    private RoutineExercise toExerciseEntity(RoutineExerciseDTO dto) {
        if (dto == null) return null;

        RoutineExercise exercise = new RoutineExercise();
        exercise.setExerciseId(dto.getExerciseId());
        exercise.setName(dto.getName());
        exercise.setSets(dto.getSets());
        exercise.setReps(dto.getReps());
        exercise.setDuration(dto.getDuration());
        return exercise;
    }
}
