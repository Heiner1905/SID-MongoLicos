package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.dto.ExerciseCreateDTO;
import com.example.proyectosid.dto.ExerciseResponseDTO;

import java.util.List;

public interface IExerciseService {
    
    ExerciseResponseDTO createExercise(ExerciseCreateDTO dto, String username);

    List<ExerciseResponseDTO> getComplementaryExercises(String userId);

    List<ExerciseResponseDTO> getAllPredefinedExercises();

    ExerciseResponseDTO getExerciseById(String id);
}
