package com.example.proyectosid.controller;

import com.example.proyectosid.dto.RoutineCreateDTO;
import com.example.proyectosid.dto.RoutineResponseDTO;
import com.example.proyectosid.dto.RoutineRequest;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.services.mongodb.IRoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoutineController {

    private final IRoutineService routineService;

    // Rutinas por user_id
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Routine>> getRoutinesByUser(@PathVariable String userId) {
        List<Routine> routines = routineService.getRoutinesByUserId(userId);
    /**
     * 3. Obtener rutinas que NO tiene el usuario (plantillas disponibles)
     * GET /api/routines/available/{userId}
     */
    @GetMapping("/available/{userId}")
    public ResponseEntity<List<RoutineResponseDTO>> getAvailableRoutines(
            @PathVariable String userId) {

        List<RoutineResponseDTO> routines = routineService.getAvailableRoutines(userId);
        return ResponseEntity.ok(routines);
    }

    // Obtener todas las rutinas
    @GetMapping("/all")
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List<Routine> routines = routineService.findAll();
    /**
     * Obtener rutinas activas del usuario
     * GET /api/routines/active/{userId}
     */
    @GetMapping("/active/{userId}")
    public ResponseEntity<List<RoutineResponseDTO>> getActiveRoutines(
            @PathVariable String userId) {

        List<RoutineResponseDTO> routines = routineService.getActiveRoutinesByUser(userId);
        return ResponseEntity.ok(routines);
    }

    // Crear rutina
    @PostMapping("/createRoutine")
    public ResponseEntity<Routine> createRoutine(@RequestBody RoutineRequest request) {
        Routine created = routineService.createRoutine(request);
        return ResponseEntity.ok(created);
    /**
     * Crear rutina personalizada
     * POST /api/routines
     */
    @PostMapping
    public ResponseEntity<RoutineResponseDTO> createRoutine(
            @Valid @RequestBody RoutineCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        RoutineResponseDTO created = routineService.createRoutine(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Adoptar una rutina predefinida
     * POST /api/routines/{templateId}/adopt
     */
    @PostMapping("/{templateId}/adopt")
    public ResponseEntity<RoutineResponseDTO> adoptRoutine(
            @PathVariable String templateId,
            Authentication authentication) {

        String username = authentication.getName();
        RoutineResponseDTO adopted = routineService.adoptRoutine(templateId, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(adopted);
    }

    /**
     * Obtener rutina por ID
     * GET /api/routines/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoutineResponseDTO> getRoutineById(@PathVariable String id) {
        RoutineResponseDTO routine = routineService.getRoutineById(id);
        return ResponseEntity.ok(routine);
    }

    /**
     * Desactivar rutina
     * PUT /api/routines/{id}/deactivate
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRoutine(
            @PathVariable String id,
            Authentication authentication) {

        String username = authentication.getName();
        routineService.deactivateRoutine(id, username);

        return ResponseEntity.noContent().build();
    }
}
