package com.example.proyectosid.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoutineExerciseDTO {

    @NotBlank(message = "El ID del ejercicio es obligatorio")
    private String exerciseId;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String name;

    @Min(value = 1, message = "Las series deben ser al menos 1")
    private Integer sets;

    @Min(value = 0, message = "Las repeticiones no pueden ser negativas")
    private Integer reps;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duration; // segundos
}
