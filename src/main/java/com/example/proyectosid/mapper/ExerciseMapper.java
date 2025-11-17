package com.example.proyectosid.mapper;

import com.example.proyectosid.dto.ExerciseCreateDTO;
import com.example.proyectosid.dto.ExerciseResponseDTO;
import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.Exercise;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExerciseMapper {

    /**
     * Convierte Exercise entity → ExerciseResponseDTO
     */
    public ExerciseResponseDTO toResponseDTO(Exercise exercise) {
        if (exercise == null) return null;

        ExerciseResponseDTO dto = new ExerciseResponseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setType(exercise.getType());
        dto.setUrlImg(exercise.getUrlImg());
        dto.setDuration(exercise.getDuration());
        dto.setDifficulty(exercise.getDifficulty());
        dto.setVideos(exercise.getVideos());
        dto.setIsPredefined(exercise.getIsPredefined());
        dto.setCreatedAt(exercise.getCreatedAt());

        if (exercise.getCreatedBy() != null) {
            dto.setCreatedByUsername(exercise.getCreatedBy().getUserId());
            dto.setCreatedByName(exercise.getCreatedBy().getName());
        }

        return dto;
    }

    /**
     * Convierte ExerciseCreateDTO → Exercise entity
     */
    public Exercise toEntity(ExerciseCreateDTO dto, String username, String fullName) {
        if (dto == null) return null;

        Exercise exercise = new Exercise();
        exercise.setName(dto.getName());
        exercise.setDescription(dto.getDescription());
        exercise.setType(dto.getType());
        exercise.setUrlImg(dto.getUrlImg());
        exercise.setDuration(dto.getDuration());
        exercise.setDifficulty(dto.getDifficulty());
        exercise.setVideos(dto.getVideos());
        exercise.setIsPredefined(false);  // Por defecto false, se cambia si es trainer
        exercise.setCreatedAt(LocalDateTime.now());

        // CreatedBy
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(username);
        createdBy.setName(fullName);
        exercise.setCreatedBy(createdBy);

        return exercise;
    }
}
