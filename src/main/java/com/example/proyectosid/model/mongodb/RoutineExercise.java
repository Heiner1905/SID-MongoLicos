package com.example.proyectosid.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExercise {

    private String exerciseId;
    private String name;
    private Integer sets;
    private Integer reps;
    private Integer duration;   // segundos
}
