package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.dto.ExerciseRequest;
import com.example.proyectosid.model.mongodb.Exercise;

public interface IExerciseService {
    
    Exercise createExercise(ExerciseRequest request);
}
