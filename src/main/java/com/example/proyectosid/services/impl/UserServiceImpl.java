package com.example.proyectosid.services.impl;

import com.example.proyectosid.dto.AssignStudentRequestDTO;
import com.example.proyectosid.dto.AssignmentResponseDTO;
import com.example.proyectosid.dto.ChangeRoleRequestDTO;
import com.example.proyectosid.dto.UserResponseDTO;
import com.example.proyectosid.model.mongodb.Assignment;
import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.repository.mongodb.AssignmentRepository;
import com.example.proyectosid.repository.postgresql.UserRepository;
import com.example.proyectosid.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    @Transactional
    public AssignmentResponseDTO assignStudentToTrainer(AssignStudentRequestDTO request, String adminUsername) {
        // Validar que el estudiante existe y tiene rol 'user'
        User student = userRepository.findByUsername(request.getStudentUsername())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + request.getStudentUsername()));

        if (!student.getRole().equals("estudiante")) {
            throw new RuntimeException("El usuario especificado no es un estudiante (rol: " + student.getRole() + ")");
        }

        if (Boolean.FALSE.equals(student.getIsActive())) {
            throw new RuntimeException("El estudiante está inactivo");
        }

        // Validar que el entrenador existe y tiene rol 'trainer'
        User trainer = userRepository.findByUsername(request.getTrainerUsername())
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado: " + request.getTrainerUsername()));

        if (!trainer.getRole().equals("entrenador")) {
            throw new RuntimeException("El usuario especificado no es un entrenador (rol: " + trainer.getRole() + ")");
        }

        if (Boolean.FALSE.equals(trainer.getIsActive())) {
            throw new RuntimeException("El entrenador está inactivo");
        }

        // Verificar si el estudiante ya tiene una asignación activa
        Optional<Assignment> existingAssignment = assignmentRepository.findActiveAssignmentByUser(student.getUsername());
        
        if (existingAssignment.isPresent()) {
            // Si ya está asignado al mismo entrenador, retornar la asignación existente
            Assignment existing = existingAssignment.get();
            if (existing.getTrainerId().equals(trainer.getUsername())) {
                return buildAssignmentResponseDTO(existing, student, trainer);
            }
            // Si está asignado a otro entrenador, desasignar primero
            existing.setIsActive(false);
            existing.setUnassignedAt(LocalDateTime.now());
            assignmentRepository.save(existing);
        }

        // Crear nueva asignación
        Assignment assignment = new Assignment();
        assignment.setUserId(student.getUsername());
        assignment.setTrainerId(trainer.getUsername());
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setIsActive(true);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return buildAssignmentResponseDTO(savedAssignment, student, trainer);
    }

    @Override
    @Transactional
    public UserResponseDTO changeUserRole(String username, ChangeRoleRequestDTO request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        String currentRole = user.getRole();
        String newRole = request.getNewRole();

        // Validar que el cambio de rol es válido (solo entre user y trainer)
        if (currentRole.equals(newRole)) {
            throw new RuntimeException("El usuario ya tiene el rol: " + newRole);
        }

        if (!currentRole.equals("estudiante") && !currentRole.equals("entrenador")) {
            throw new RuntimeException("No se puede cambiar el rol de un usuario con rol: " + currentRole);
        }

        if (!newRole.equals("estudiante") && !newRole.equals("entrenador")) {
            throw new RuntimeException("El nuevo rol debe ser 'user' o 'trainer'");
        }

        // Si el usuario es un estudiante (tiene student_id) y se quiere cambiar a trainer
        if (newRole.equals("entrenador") && user.getStudentId() != null) {
            throw new RuntimeException("No se puede cambiar a entrenador un usuario que está asociado a un estudiante. Debe estar asociado a un empleado.");
        }

        // Si el usuario es un empleado (tiene employee_id) y se quiere cambiar a user
        if (newRole.equals("estudiante") && user.getEmployeeId() != null) {
            throw new RuntimeException("No se puede cambiar a estudiante un usuario que está asociado a un empleado. Debe estar asociado a un estudiante.");
        }

        // Si cambia de trainer a user, desasignar cualquier asignación activa donde era entrenador
        if (currentRole.equals("entrenador") && newRole.equals("estudiante")) {
            List<Assignment> activeAssignments = assignmentRepository.findActiveAssignmentsByTrainer(username);
            LocalDateTime now = LocalDateTime.now();
            for (Assignment assignment : activeAssignments) {
                assignment.setIsActive(false);
                assignment.setUnassignedAt(now);
                assignmentRepository.save(assignment);
            }
        }

        // Cambiar el rol
        user.setRole(newRole);
        User savedUser = userRepository.save(user);

        return buildUserResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllTrainers() {
        List<User> trainers = userRepository.findByRoleWithRelations("entrenador");
        return trainers.stream()
                .map(this::buildUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllStudents() {
        List<User> students = userRepository.findByRoleWithRelations("estudiante");
        return students.stream()
                .map(this::buildUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentResponseDTO> getAllActiveAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll()
                .stream()
                .filter(Assignment::getIsActive)
                .collect(Collectors.toList());

        return assignments.stream()
                .map(assignment -> {
                    User student = userRepository.findByUsername(assignment.getUserId())
                            .orElse(null);
                    User trainer = userRepository.findByUsername(assignment.getTrainerId())
                            .orElse(null);
                    return buildAssignmentResponseDTO(assignment, student, trainer);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentResponseDTO> getAssignmentsByTrainer(String trainerUsername) {
        // Validar que el entrenador existe
        User trainer = userRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado: " + trainerUsername));

        List<Assignment> assignments = assignmentRepository.findActiveAssignmentsByTrainer(trainerUsername);

        return assignments.stream()
                .map(assignment -> {
                    User student = userRepository.findByUsername(assignment.getUserId())
                            .orElse(null);
                    return buildAssignmentResponseDTO(assignment, student, trainer);
                })
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentResponseDTO getAssignmentByStudent(String studentUsername) {
        // Validar que el estudiante existe
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + studentUsername));

        Optional<Assignment> assignment = assignmentRepository.findActiveAssignmentByUser(studentUsername);

        if (assignment.isEmpty()) {
            return null;
        }

        Assignment activeAssignment = assignment.get();
        User trainer = userRepository.findByUsername(activeAssignment.getTrainerId())
                .orElse(null);

        return buildAssignmentResponseDTO(activeAssignment, student, trainer);
    }

    @Override
    @Transactional
    public void unassignStudent(String studentUsername, String adminUsername) {
        // Validar que el estudiante existe
        userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + studentUsername));

        Optional<Assignment> assignment = assignmentRepository.findActiveAssignmentByUser(studentUsername);

        if (assignment.isEmpty()) {
            throw new RuntimeException("El estudiante no tiene una asignación activa");
        }

        Assignment activeAssignment = assignment.get();
        activeAssignment.setIsActive(false);
        activeAssignment.setUnassignedAt(LocalDateTime.now());
        assignmentRepository.save(activeAssignment);
    }

    // Métodos auxiliares para construir DTOs
    private UserResponseDTO buildUserResponseDTO(User user) {
        String fullName = "";
        String email = "";
        String userId = "";

        if (user.getStudent() != null) {
            fullName = user.getStudent().getFirstName() + " " + user.getStudent().getLastName();
            email = user.getStudent().getEmail();
            userId = user.getStudent().getId();
        } else if (user.getEmployee() != null) {
            fullName = user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName();
            email = user.getEmployee().getEmail();
            userId = String.valueOf(user.getEmployee().getId());
        }

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .studentId(user.getStudentId())
                .employeeId(user.getEmployeeId())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .fullName(fullName)
                .email(email)
                .userId(userId)
                .build();
    }

    private AssignmentResponseDTO buildAssignmentResponseDTO(Assignment assignment, User student, User trainer) {
        String studentName = "";
        String trainerName = "";

        if (student != null) {
            if (student.getStudent() != null) {
                studentName = student.getStudent().getFirstName() + " " + student.getStudent().getLastName();
            } else if (student.getEmployee() != null) {
                studentName = student.getEmployee().getFirstName() + " " + student.getEmployee().getLastName();
            }
        }

        if (trainer != null) {
            if (trainer.getEmployee() != null) {
                trainerName = trainer.getEmployee().getFirstName() + " " + trainer.getEmployee().getLastName();
            } else if (trainer.getStudent() != null) {
                trainerName = trainer.getStudent().getFirstName() + " " + trainer.getStudent().getLastName();
            }
        }

        return AssignmentResponseDTO.builder()
                .id(assignment.getId())
                .userId(assignment.getUserId())
                .trainerId(assignment.getTrainerId())
                .assignedAt(assignment.getAssignedAt())
                .unassignedAt(assignment.getUnassignedAt())
                .isActive(assignment.getIsActive())
                .assignedBy(null)  // Campo no disponible en el modelo actual
                .createdAt(null)   // Campo no disponible en el modelo actual
                .studentName(studentName)
                .trainerName(trainerName)
                .build();
    }
}

