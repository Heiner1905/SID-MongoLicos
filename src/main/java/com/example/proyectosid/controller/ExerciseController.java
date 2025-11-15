package com.example.proyectosid.controller;

import com.example.proyectosid.dto.ExerciseCreateDTO;
import com.example.proyectosid.dto.ExerciseResponseDTO;
import com.example.proyectosid.model.mongodb.Exercise;
import com.example.proyectosid.services.mongodb.IExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final IExerciseService exerciseService;

    /**
     * Crear ejercicio (cualquier usuario puede crear, pero solo trainers pueden crear predefinidos)
     * POST /api/exercises
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExerciseResponseDTO> createExercise(
            @Valid @RequestBody ExerciseCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        ExerciseResponseDTO created = exerciseService.createExercise(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtener ejercicios complementarios
     * GET /api/exercises/complementary/{userId}
     */
    @GetMapping("/complementary/{userId}")
    @PreAuthorize("hasAuthority('EXERCISE_READ')")
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
    @PreAuthorize("hasAuthority('EXERCISE_READ')")
    public ResponseEntity<List<ExerciseResponseDTO>> getPredefinedExercises() {
        List<ExerciseResponseDTO> exercises = exerciseService.getAllPredefinedExercises();
        return ResponseEntity.ok(exercises);
    }

    /**
     * Obtener ejercicio por ID
     * GET /api/exercises/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EXERCISE_READ')")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable String id) {
        ExerciseResponseDTO exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);
    }

    /**
     * Obtener todos los ejercicios
     * GET /api/exercises/all
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('EXERCISE_READ')")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> exercises = exerciseService.findAll();
        return ResponseEntity.ok(exercises);
    }

    /**
     * Obtener ejercicios creados por un usuario
     * GET /api/exercises/byUser/{userId}
     */
    @GetMapping("/byUser/{userId}")
    @PreAuthorize("hasAuthority('EXERCISE_READ')")
    public ResponseEntity<List<Exercise>> getExercisesByUser(@PathVariable String userId) {
        List<Exercise> exercises = exerciseService.getExercisesByUserId(userId);
        return ResponseEntity.ok(exercises);
    }
}
