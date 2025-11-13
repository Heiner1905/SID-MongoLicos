package com.example.proyectosid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RoutineCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotEmpty(message = "Debe incluir al menos un ejercicio")
    private List<RoutineExerciseDTO> exercises;
}
