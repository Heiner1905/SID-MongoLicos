package com.example.proyectosid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerStatisticsDTO {
    private Long id;
    private String trainerId;
    private Integer year;
    private Integer month;
    private Integer newAssignmentsCount;
    private Integer recommendationsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
