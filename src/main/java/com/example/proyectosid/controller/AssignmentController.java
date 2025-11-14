// controller/AssignmentController.java
package com.example.proyectosid.controller;

import com.example.proyectosid.model.mongodb.Assignment;
import com.example.proyectosid.services.mongodb.IAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssignmentController {

    private final IAssignmentService assignmentService;

    /**
     * Asignar entrenador a usuario
     * POST /api/assignments
     */
    @PostMapping
    public ResponseEntity<Assignment> assignTrainer(
            @RequestParam String userId,
            @RequestParam String trainerId) {

        Assignment assignment = assignmentService.assignTrainerToUser(userId, trainerId);
        return ResponseEntity.ok(assignment);
    }

    /**
     * Obtener asignación activa de un usuario
     * GET /api/assignments/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Assignment> getUserAssignment(@PathVariable String userId) {
        Assignment assignment = assignmentService.getActiveAssignment(userId);
        return ResponseEntity.ok(assignment);
    }

    /**
     * Obtener todos los usuarios asignados a un entrenador
     * GET /api/assignments/trainer/{trainerId}
     */
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<Assignment>> getTrainerUsers(@PathVariable String trainerId) {
        List<Assignment> users = assignmentService.getTrainerUsers(trainerId);
        return ResponseEntity.ok(users);
    }

    /**
     * Desactivar asignación de un usuario
     * PUT /api/assignments/deactivate/{userId}
     */
    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<Assignment> deactivateAssignment(@PathVariable String userId) {
        Assignment assignment = assignmentService.deactivateAssignment(userId);
        return ResponseEntity.ok(assignment);
    }

    /**
     * Contar usuarios activos de un entrenador
     * GET /api/assignments/trainer/{trainerId}/count
     */
    @GetMapping("/trainer/{trainerId}/count")
    public ResponseEntity<Long> countActiveUsers(@PathVariable String trainerId) {
        Long count = assignmentService.countActiveUsersByTrainer(trainerId);
        return ResponseEntity.ok(count);
    }
}
