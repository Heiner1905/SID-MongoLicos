// services/impl/mongodb/RecommendationServiceImpl.java
package com.example.proyectosid.services.impl.mongodb;

import com.example.proyectosid.model.mongodb.Recommendation;
import com.example.proyectosid.repository.mongodb.RecommendationRepository;
import com.example.proyectosid.services.mongodb.IRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements IRecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public Recommendation createRecommendation(String userId, String trainerId, String routineId, String message) {
        Recommendation recommendation = new Recommendation();
        recommendation.setUserId(userId);
        recommendation.setTrainerId(trainerId);
        recommendation.setRoutineId(routineId);
        recommendation.setMessage(message);
        recommendation.setIsRead(false);
        recommendation.setCreatedAt(LocalDateTime.now());

        return recommendationRepository.save(recommendation);
    }

    @Override
    public List<Recommendation> getUserRecommendations(String userId) {
        return recommendationRepository.findByUserIdOrderByIsReadAscCreatedAtDesc(userId);
    }

    @Override
    public List<Recommendation> getUnreadRecommendations(String userId) {
        return recommendationRepository.findUnreadByUser(userId);
    }

    @Override
    public Recommendation markAsRead(String recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada"));

        recommendation.setIsRead(true);
        return recommendationRepository.save(recommendation);
    }

    @Override
    public Long countUnreadByUser(String userId) {
        return recommendationRepository.countUnreadByUser(userId);
    }
}
