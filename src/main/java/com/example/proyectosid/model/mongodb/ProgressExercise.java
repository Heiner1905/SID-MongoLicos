package com.example.proyectosid.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressExercise {
    private String exerciseId;
    private String name;
    private String type;
    private Integer duration;   // segundos
    private Integer difficulty;   // 1-10
    private Integer sets;
    private Integer reps;
}
