package com.example.proyectosid.repository.mongodb;

import com.example.proyectosid.model.mongodb.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    // Buscar asignación activa de un usuario
    @Query("{ 'userId': ?0, 'isActive': true }")
    Optional<Assignment> findActiveAssignmentByUser(String userId);

    // Buscar todos los usuarios asignados a un entrenador (activos)
    @Query("{ 'trainerId': ?0, 'isActive': true }")
    List<Assignment> findActiveAssignmentsByTrainer(String trainerId);

    // Historial completo de asignaciones de un usuario
    List<Assignment> findByUserIdOrderByAssignedAtDesc(String userId);

    // Historial completo de un entrenador
    List<Assignment> findByTrainerIdOrderByAssignedAtDesc(String trainerId);

    // Contar usuarios activos de un entrenador
    @Query(value = "{ 'trainerId': ?0, 'isActive': true }", count = true)
    Long countActiveAssignmentsByTrainer(String trainerId);

    // Verificar si un usuario tiene asignación activa
    @Query(value = "{ 'userId': ?0, 'isActive': true }", exists = true)
    boolean hasActiveAssignment(String userId);

}
