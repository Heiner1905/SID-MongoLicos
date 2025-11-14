// services/impl/mongodb/AssignmentServiceImpl.java
package com.example.proyectosid.services.impl.mongodb;

import com.example.proyectosid.model.mongodb.Assignment;
import com.example.proyectosid.repository.mongodb.AssignmentRepository;
import com.example.proyectosid.services.mongodb.IAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements IAssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Override
    public Assignment assignTrainerToUser(String userId, String trainerId) {
        // Desactivar asignación anterior si existe
        assignmentRepository.findActiveAssignmentByUser(userId)
                .ifPresent(existing -> {
                    existing.setIsActive(false);
                    assignmentRepository.save(existing);
                });

        // Crear nueva asignación
        Assignment assignment = new Assignment();
        assignment.setUserId(userId);
        assignment.setTrainerId(trainerId);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setIsActive(true);

        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment getActiveAssignment(String userId) {
        return assignmentRepository.findActiveAssignmentByUser(userId)
                .orElse(null);
    }

    @Override
    public List<Assignment> getTrainerUsers(String trainerId) {
        return assignmentRepository.findActiveAssignmentsByTrainer(trainerId);
    }

    @Override
    public Assignment deactivateAssignment(String userId) {
        Assignment assignment = assignmentRepository.findActiveAssignmentByUser(userId)
                .orElseThrow(() -> new RuntimeException("No hay asignación activa"));

        assignment.setIsActive(false);
        return assignmentRepository.save(assignment);
    }

    @Override
    public Long countActiveUsersByTrainer(String trainerId) {
        return assignmentRepository.countActiveAssignmentsByTrainer(trainerId);
    }

    @Override
    public Long countAssignmentsByTrainerInPeriod(String trainerId, LocalDateTime start, LocalDateTime end) {
        return assignmentRepository.countAssignmentsByTrainerInPeriod(trainerId, start, end);
    }
}
