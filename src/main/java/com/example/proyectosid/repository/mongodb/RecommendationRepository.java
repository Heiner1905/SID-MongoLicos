package com.example.proyectosid.repository.mongodb;


import com.example.proyectosid.model.mongodb.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    // Recomendaciones de un usuario (no leídas primero)
    @Query("{ 'userId': ?0 }")
    List<Recommendation> findByUserIdOrderByIsReadAscCreatedAtDesc(String userId);

    // Recomendaciones no leídas de un usuario
    @Query("{ 'userId': ?0, 'isRead': false }")
    List<Recommendation> findUnreadByUser(String userId);

    // Recomendaciones hechas por un entrenador
    List<Recommendation> findByTrainerIdOrderByCreatedAtDesc(String trainerId);

    // Recomendaciones de una rutina específica
    List<Recommendation> findByRoutineIdOrderByCreatedAtDesc(String routineId);

    // Recomendaciones de un entrenador para un usuario específico
    @Query("{ 'trainerId': ?0, 'userId': ?1 }")
    List<Recommendation> findByTrainerAndUser(String trainerId, String userId);

    // Contar recomendaciones no leídas
    @Query(value = "{ 'userId': ?0, 'isRead': false }", count = true)
    Long countUnreadByUser(String userId);

    // Recomendaciones en un rango de fechas (para estadísticas)
    @Query(value = "{ 'trainerId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }", count = true)
    Long countRecommendationsByTrainerInPeriod(String trainerId, LocalDateTime start, LocalDateTime end);
}
