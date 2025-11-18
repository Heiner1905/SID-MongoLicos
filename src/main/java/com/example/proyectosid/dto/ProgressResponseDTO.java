package com.example.proyectosid.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProgressResponseDTO {
    private String id;
    private String userId;
    private String routineId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // Exercise info
    private String exerciseId;
    private String exerciseName;
    private String exerciseType;

    // Metrics
    private Integer reps;
    private Integer sets;
    private Double weight;
    private Integer time;
    private Double distance;
    private Integer effortLevel;
    private Integer rhythm;
}
