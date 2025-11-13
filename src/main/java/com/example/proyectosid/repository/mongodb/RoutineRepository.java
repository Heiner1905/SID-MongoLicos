package com.example.proyectosid.repository.mongodb;

import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.model.postgresql.Campus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoutineRepository extends MongoRepository<Routine, String> {
    // Buscar rutinas predefinidas
    List<Routine> findByIsPredefined(Boolean isPredefined);

    // Buscar rutinas activas de un usuario
    @Query("{ 'createdBy.userId': ?0, 'isActive': true }")
    List<Routine> findActiveRoutinesByUser(String userId);

    // Buscar todas las rutinas de un usuario (activas e inactivas)
    @Query("{ 'createdBy.userId': ?0 }")
    List<Routine> findAllRoutinesByUser(String userId);

    // Buscar rutinas predefinidas certificadas
    @Query("{ 'isPredefined': true, 'isCertified': true }")
    List<Routine> findCertifiedTemplates();

    // Contar cuántos usuarios adoptaron una rutina predefinida
    @Query(value = "{ 'originalRoutineId': ?0 }", count = true)
    Long countAdoptionsByTemplate(String templateId);

    // Buscar por nombre (búsqueda parcial)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Routine> searchByName(String name);

    // Rutinas creadas por entrenadores (certificadas)
    @Query("{ 'createdBy.role': 'trainer', 'isCertified': true }")
    List<Routine> findAllCertifiedByTrainers();

     // Rutinas creadas por el usuario
    @Query("{ 'createdBy.userId': ?0 }")
    List<Routine> findRoutinesCreatedByUser(String userId);

    // Rutinas adoptadas por el usuario
    @Query("{ 'owners.userId': ?0 }")
    List<Routine> findRoutinesAdoptedByUser(String userId);

    // O combinadas (ambas en una sola consulta)
    @Query("{ '$or': [ { 'createdBy.userId': ?0 }, { 'owners.userId': ?0 } ] }")
    List<Routine> findAllRoutinesByUserId(String userId);

    List<Routine> findAll();
}
