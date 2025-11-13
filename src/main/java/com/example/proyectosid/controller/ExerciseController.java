package com.example.proyectosid.controller;

import com.example.proyectosid.dto.ExerciseCreateDTO;
import com.example.proyectosid.dto.ExerciseResponseDTO;
import com.example.proyectosid.services.mongodb.IExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final IExerciseService exerciseService;

    /**
     * 1. Crear ejercicio
     * POST /api/exercises
     */
    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createExercise(
            @Valid @RequestBody ExerciseCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        ExerciseResponseDTO created = exerciseService.createExercise(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 2. Obtener ejercicios que NO tiene el usuario (complementarios)
     * GET /api/exercises/complementary/{userId}
     */
    @GetMapping("/complementary/{userId}")
    public ResponseEntity<List<ExerciseResponseDTO>> getComplementaryExercises(
            @PathVariable String userId) {

        List<ExerciseResponseDTO> exercises = exerciseService.getComplementaryExercises(userId);
        return ResponseEntity.ok(exercises);
    }

    /**
     * Obtener todos los ejercicios predefinidos
     * GET /api/exercises/predefined
     */
    @GetMapping("/predefined")
    public ResponseEntity<List<ExerciseResponseDTO>> getPredefinedExercises() {
        List<ExerciseResponseDTO> exercises = exerciseService.getAllPredefinedExercises();
        return ResponseEntity.ok(exercises);
    }

    /**
     * Obtener ejercicio por ID
     * GET /api/exercises/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable String id) {
        ExerciseResponseDTO exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);
    }
}
