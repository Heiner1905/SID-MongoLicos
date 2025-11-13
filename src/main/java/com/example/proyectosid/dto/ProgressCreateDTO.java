package com.example.proyectosid.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgressCreateDTO {

    @NotBlank(message = "El ID de la rutina es obligatorio")
    private String routineId;

    @NotBlank(message = "El ID del ejercicio es obligatorio")
    private String exerciseId;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String exerciseName;

    @NotBlank(message = "El tipo de ejercicio es obligatorio")
    private String exerciseType;

    // Métricas
    @NotNull(message = "Las repeticiones son obligatorias")
    @Min(value = 0)
    private Integer reps;

    @NotNull(message = "Las series son obligatorias")
    @Min(value = 1)
    private Integer sets;

    @Min(value = 0)
    private Double weight; // kg

    @Min(value = 0)
    private Integer time; // segundos

    @Min(value = 0)
    private Double distance; // km

    @Min(value = 1, message = "El nivel de esfuerzo debe estar entre 1 y 10")
    @Max(value = 10, message = "El nivel de esfuerzo debe estar entre 1 y 10")
    private Integer effortLevel;
}
