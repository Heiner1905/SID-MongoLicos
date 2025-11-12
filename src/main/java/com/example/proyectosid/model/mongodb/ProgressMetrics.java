package com.example.proyectosid.model.mongodb;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressMetrics {
    private Integer reps;
    private Double weight;      // kg
    private Integer time;       // segundos
    private Double distance;    // km
    private Integer effortLevel; // 1-10
}
