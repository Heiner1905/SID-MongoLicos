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
public class UserResponseDTO {
    private String username;
    private String role;
    private String studentId;
    private Integer employeeId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    
    // Información adicional
    private String fullName;
    private String email;
    private String userId; // ID del estudiante o empleado
}

