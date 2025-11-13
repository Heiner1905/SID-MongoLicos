package com.example.proyectosid.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "El tipo es obligatorio")
    private String type; // cardio, fuerza, movilidad

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duration; // minutos

    @Min(value = 1, message = "La dificultad debe ser entre 1 y 5")
    @Max(value = 5, message = "La dificultad debe ser entre 1 y 5")
    private Integer difficulty;

    private List<String> videos; // URLs opcionales
}
