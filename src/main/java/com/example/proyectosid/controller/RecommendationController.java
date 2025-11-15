package com.example.proyectosid.controller;

import com.example.proyectosid.model.mongodb.Recommendation;
import com.example.proyectosid.services.mongodb.IRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final IRecommendationService recommendationService;

    /**
     * Crear recomendación (solo trainers)
     * POST /api/recommendations
     */
    @PostMapping
    @PreAuthorize("hasAuthority('RECOMMENDATION_CREATE')")
    public ResponseEntity<Recommendation> createRecommendation(
            @RequestParam String userId,
            @RequestParam(required = false) String routineId,
            @RequestParam String message,
            Authentication authentication) {

        String trainerId = authentication.getName();
        Recommendation recommendation = recommendationService.createRecommendation(
                userId, trainerId, routineId, message);

        return ResponseEntity.status(HttpStatus.CREATED).body(recommendation);
    }

    /**
     * Obtener recomendaciones de un usuario
     * GET /api/recommendations/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId) {
        List<Recommendation> recommendations = recommendationService.getUserRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    /**
     * Obtener recomendaciones no leídas
     * GET /api/recommendations/unread/{userId}
     */
    @GetMapping("/unread/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Recommendation>> getUnreadRecommendations(@PathVariable String userId) {
        List<Recommendation> recommendations = recommendationService.getUnreadRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    /**
     * Marcar como leída
     * PUT /api/recommendations/{id}/read
     */
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Recommendation> markAsRead(@PathVariable String id) {
        Recommendation recommendation = recommendationService.markAsRead(id);
        return ResponseEntity.ok(recommendation);
    }

    /**
     * Contar no leídas
     * GET /api/recommendations/unread/{userId}/count
     */
    @GetMapping("/unread/{userId}/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countUnread(@PathVariable String userId) {
        Long count = recommendationService.countUnreadByUser(userId);
        return ResponseEntity.ok(count);
    }
}
