package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.dto.ProgressCreateDTO;
import com.example.proyectosid.dto.ProgressResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IProgressService {
    ProgressResponseDTO recordProgress(ProgressCreateDTO dto, String username);

    List<ProgressResponseDTO> getUserProgress(String userId);

    List<ProgressResponseDTO> getProgressByRoutine(String userId, String routineId);

    List<ProgressResponseDTO> getProgressByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);

    List<ProgressResponseDTO> getProgressByExerciseAndDateRange(String userId, String exerciseId, LocalDateTime startDate, LocalDateTime endDate);

    List<ProgressResponseDTO> getRecentProgress(String userId, int days);
}
