package com.example.proyectosid.controller;

import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.services.mongodb.IRoutineService;
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

    /**
     * Obtener rutinas activas del usuario
     * GET /api/routines/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Routine>> getRoutinesByUser(@PathVariable String userId) {
        List<Routine> routines = routineService.getRoutinesByUserId(userId);
        return ResponseEntity.ok(routines);
    }

    /**
     * Obtener todas las rutinas predefinidas (plantillas certificadas)
     * GET /api/routines/templates
     */
    @GetMapping("/templates")
    public ResponseEntity<List<Routine>> getCertifiedTemplates() {
        List<Routine> templates = routineService.getCertifiedTemplates();
        return ResponseEntity.ok(templates);
    }

    /**
     * Obtener todas las rutinas
     * GET /api/routines/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List<Routine> routines = routineService.findAll();
        return ResponseEntity.ok(routines);
    }

    /**
     * Crear rutina
     * POST /api/routines
     */
    @PostMapping
    public ResponseEntity<Routine> createRoutine(@RequestBody Routine routine) {
        Routine created = routineService.createRoutine(routine);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtener rutina por ID
     * GET /api/routines/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Routine> getRoutineById(@PathVariable String id) {
        Routine routine = routineService.getRoutineById(id);
        return ResponseEntity.ok(routine);
    }

    /**
     * Actualizar rutina
     * PUT /api/routines/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Routine> updateRoutine(
            @PathVariable String id,
            @RequestBody Routine routine) {

        Routine updated = routineService.updateRoutine(id, routine);
        return ResponseEntity.ok(updated);
    }

    /**
     * Desactivar rutina
     * PUT /api/routines/{id}/deactivate
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Routine> deactivateRoutine(@PathVariable String id) {
        Routine deactivated = routineService.deactivateRoutine(id);
        return ResponseEntity.ok(deactivated);
    }

    /**
     * Eliminar rutina
     * DELETE /api/routines/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable String id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adoptar una rutina predefinida
     * POST /api/routines/{routineId}/adopt
     */
    @PostMapping("/{routineId}/adopt")
    public ResponseEntity<Routine> adoptRoutine(
            @PathVariable String routineId,
            Authentication authentication) {

        String username = authentication.getName();
        Routine adopted = routineService.adoptRoutine(routineId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(adopted);
    }

}
