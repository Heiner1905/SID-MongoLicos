package com.example.proyectosid.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignStudentRequestDTO {

    @NotBlank(message = "El username del estudiante es obligatorio")
    private String studentUsername;

    @NotBlank(message = "El username del entrenador es obligatorio")
    private String trainerUsername;
}

