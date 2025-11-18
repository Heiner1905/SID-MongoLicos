package com.example.proyectosid.controller;

import com.example.proyectosid.dto.AssignStudentRequestDTO;
import com.example.proyectosid.dto.AssignmentResponseDTO;
import com.example.proyectosid.dto.ChangeRoleRequestDTO;
import com.example.proyectosid.dto.UserResponseDTO;
import com.example.proyectosid.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final IUserService userService;

    /**
     * Asignar un estudiante a un entrenador
     * POST /api/users/assign
     * Solo admin
     */
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssignmentResponseDTO> assignStudentToTrainer(
            @Valid @RequestBody AssignStudentRequestDTO request) {
        String adminUsername = getCurrentUsername();
        AssignmentResponseDTO assignment = userService.assignStudentToTrainer(request, adminUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
    }

    /**
     * Cambiar el rol de un usuario
     * PUT /api/users/{username}/role
     * Solo admin
     */
    @PutMapping("/{username}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> changeUserRole(
            @PathVariable String username,
            @Valid @RequestBody ChangeRoleRequestDTO request) {
        UserResponseDTO updatedUser = userService.changeUserRole(username, request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Obtener todos los entrenadores
     * GET /api/users/trainers
     * Solo admin
     */
    @GetMapping("/trainers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllTrainers() {
        List<UserResponseDTO> trainers = userService.getAllTrainers();
        return ResponseEntity.ok(trainers);
    }

    /**
     * Obtener todos los estudiantes
     * GET /api/users/students
     * Solo admin
     */
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllStudents() {
        List<UserResponseDTO> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Obtener todas las asignaciones activas
     * GET /api/users/assignments
     * Solo admin
     */
    @GetMapping("/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssignmentResponseDTO>> getAllActiveAssignments() {
        List<AssignmentResponseDTO> assignments = userService.getAllActiveAssignments();
        return ResponseEntity.ok(assignments);
    }

    /**
     * Obtener asignaciones de un entrenador específico
     * GET /api/users/trainers/{trainerUsername}/assignments
     * Solo admin
     */
    @GetMapping("/trainers/{trainerUsername}/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByTrainer(
            @PathVariable String trainerUsername) {
        List<AssignmentResponseDTO> assignments = userService.getAssignmentsByTrainer(trainerUsername);
        return ResponseEntity.ok(assignments);
    }

    /**
     * Obtener asignación de un estudiante específico
     * GET /api/users/students/{studentUsername}/assignment
     * Solo admin
     */
    @GetMapping("/students/{studentUsername}/assignment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentByStudent(
            @PathVariable String studentUsername) {
        AssignmentResponseDTO assignment = userService.getAssignmentByStudent(studentUsername);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignment);
    }

    /**
     * Desasignar un estudiante de su entrenador actual
     * DELETE /api/users/students/{studentUsername}/assignment
     * Solo admin
     */
    @DeleteMapping("/students/{studentUsername}/assignment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unassignStudent(@PathVariable String studentUsername) {
        String adminUsername = getCurrentUsername();
        userService.unassignStudent(studentUsername, adminUsername);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método auxiliar para obtener el username del usuario autenticado
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}

