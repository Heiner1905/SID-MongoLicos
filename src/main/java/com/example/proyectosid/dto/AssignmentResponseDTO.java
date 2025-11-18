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
public class AssignmentResponseDTO {
    private String id;
    private String userId;
    private String trainerId;
    private LocalDateTime assignedAt;
    private LocalDateTime unassignedAt;
    private Boolean isActive;
    private String assignedBy;
    private LocalDateTime createdAt;
    
    // Información adicional
    private String studentName;
    private String trainerName;
}

