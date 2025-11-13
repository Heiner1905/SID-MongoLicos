package com.example.proyectosid.repository.mongodb;


import com.example.proyectosid.model.mongodb.Progress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgressRepository extends MongoRepository<Progress, String> {

    // Progreso de un usuario
    List<Progress> findByUserIdOrderByCreatedAtDesc(String userId);

    // Progreso de un usuario en una rutina específica
    @Query("{ 'userId': ?0, 'routineId': ?1 }")
    List<Progress> findByUserAndRoutine(String userId, String routineId);

    // Progreso de un usuario en un ejercicio específico
    @Query("{ 'userId': ?0, 'exercise.exerciseId': ?1 }")
    List<Progress> findByUserAndExercise(String userId, String exerciseId);

    // Progreso en un rango de fechas
    @Query("{ 'userId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<Progress> findByUserAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);

    // Progreso reciente de una rutina (últimos N días)
    @Query("{ 'userId': ?0, 'routineId': ?1, 'createdAt': { $gte: ?2 } }")
    List<Progress> findRecentProgressByRoutine(String userId, String routineId, LocalDateTime since);

    // Contar registros de progreso de un usuario en un mes
    @Query(value = "{ 'userId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }", count = true)
    Long countProgressInMonth(String userId, LocalDateTime monthStart, LocalDateTime monthEnd);

    // Progreso de un usuario en una rutina ordenado por fecha
    @Query("{ 'userId': ?0, 'routineId': ?1 }")
    List<Progress> findByUserAndRoutineOrderByCreatedAtAsc(String userId, String routineId);

    @Query("{ 'userId': ?0, 'exercise.exerciseId': ?1, 'createdAt': { $gte: ?2, $lte: ?3 } }")
    List<Progress> findByUserExerciseAndDateRange(String userId, String exerciseId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Progress> findByUserIdAndRoutineId(String userId, String routineId);

}
