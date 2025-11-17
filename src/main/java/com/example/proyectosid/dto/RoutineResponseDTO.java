package com.example.proyectosid.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoutineResponseDTO {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPredefined;
    private String urlImg;
    private String originalRoutineId;
    private String createdByUsername;
    private String createdByName;
    private List<RoutineExerciseDTO> exercises;
    private Boolean isCertified;
    private Boolean isActive;
}
