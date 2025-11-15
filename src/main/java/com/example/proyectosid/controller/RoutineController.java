package com.example.proyectosid.controller;

import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.services.mongodb.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROUTINE_READ')")
    public ResponseEntity<List<Routine>> getRoutinesByUser(@PathVariable String userId) {
        List<Routine> routines = routineService.getRoutinesByUserId(userId);
        return ResponseEntity.ok(routines);
    }

    /**
     * Obtener todas las rutinas predefinidas (plantillas certificadas)
     * GET /api/routines/templates
     */
    @GetMapping("/templates")
    @PreAuthorize("hasAuthority('ROUTINE_READ')")
    public ResponseEntity<List<Routine>> getCertifiedTemplates() {
        List<Routine> templates = routineService.getCertifiedTemplates();
        return ResponseEntity.ok(templates);
    }

    /**
     * Obtener todas las rutinas
     * GET /api/routines/all
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROUTINE_READ')")
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List<Routine> routines = routineService.findAll();
        return ResponseEntity.ok(routines);
    }

    /**
     * Crear rutina
     * POST /api/routines
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROUTINE_CREATE')")
    public ResponseEntity<Routine> createRoutine(@RequestBody Routine routine) {
        Routine created = routineService.createRoutine(routine);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtener rutina por ID
     * GET /api/routines/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROUTINE_READ')")
    public ResponseEntity<Routine> getRoutineById(@PathVariable String id) {
        Routine routine = routineService.getRoutineById(id);
        return ResponseEntity.ok(routine);
    }

    /**
     * Actualizar rutina (solo el dueño)
     * PUT /api/routines/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROUTINE_UPDATE_OWN')")
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
    @PreAuthorize("hasAuthority('ROUTINE_UPDATE_OWN')")
    public ResponseEntity<Routine> deactivateRoutine(@PathVariable String id) {
        Routine deactivated = routineService.deactivateRoutine(id);
        return ResponseEntity.ok(deactivated);
    }

    /**
     * Eliminar rutina (solo el dueño)
     * DELETE /api/routines/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROUTINE_DELETE_OWN')")
    public ResponseEntity<Void> deleteRoutine(@PathVariable String id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adoptar una rutina predefinida
     * POST /api/routines/{routineId}/adopt
     */
    @PostMapping("/{routineId}/adopt")
    @PreAuthorize("hasAuthority('ROUTINE_ADOPT')")
    public ResponseEntity<Routine> adoptRoutine(
            @PathVariable String routineId,
            Authentication authentication) {

        String username = authentication.getName();
        Routine adopted = routineService.adoptRoutine(routineId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(adopted);
    }
}
