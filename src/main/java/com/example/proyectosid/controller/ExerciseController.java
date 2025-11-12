package com.example.proyectosid.controller;

import com.example.proyectosid.dto.ExerciseRequest;
import com.example.proyectosid.model.mongodb.Exercise;
import com.example.proyectosid.services.mongodb.IExerciseService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // para permitir que React consuma el endpoint
public class ExerciseController {

    private final IExerciseService service;

    @PostMapping
    public ResponseEntity<Exercise> createExercise(@RequestBody ExerciseRequest request) {
        Exercise created = service.createExercise(request);
        return ResponseEntity.ok(created);
    }
}
