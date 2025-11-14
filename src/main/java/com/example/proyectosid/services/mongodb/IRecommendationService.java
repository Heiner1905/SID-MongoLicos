package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.model.mongodb.Recommendation;
import java.util.List;

public interface IRecommendationService {
    Recommendation createRecommendation(String userId, String trainerId, String routineId, String message);
    List<Recommendation> getUserRecommendations(String userId);
    List<Recommendation> getUnreadRecommendations(String userId);
    Recommendation markAsRead(String recommendationId);
    Long countUnreadByUser(String userId);
}
