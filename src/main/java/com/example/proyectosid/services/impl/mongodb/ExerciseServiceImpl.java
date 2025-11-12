package com.example.proyectosid.services.impl.mongodb;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.proyectosid.dto.ExerciseRequest;
import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.Exercise;
import com.example.proyectosid.repository.mongodb.ExerciseRepository;
import com.example.proyectosid.services.mongodb.IExerciseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements IExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    
    @Override
    public Exercise createExercise(ExerciseRequest request) {
        Exercise exercise = new Exercise();
        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setType(request.getType());
        exercise.setDuration(request.getDuration());
        exercise.setDifficulty(request.getDifficulty());
        exercise.setVideos(request.getVideos());
        exercise.setIsPredefined(request.getIsPredefined() != null ? request.getIsPredefined() : false);
        exercise.setCreatedAt(LocalDateTime.now());
        exercise.setCreatedBy(new CreatedBy(request.getCreatedByUserId(), request.getCreatedByName()));

        return exerciseRepository.save(exercise);
    }
    
}
