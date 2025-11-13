package com.example.proyectosid.services;

import com.example.proyectosid.dto.TrainerStatisticsDTO;
import com.example.proyectosid.dto.UserStatisticsDTO;

import java.util.List;

public interface IStatisticsService {

    // Estadísticas de usuarios
    List<UserStatisticsDTO> getUserStatistics(String userId);
    UserStatisticsDTO getUserStatisticsForMonth(String userId, int year, int month);
    List<UserStatisticsDTO> getTopUsersOfMonth(int year, int month, int limit);

    // Estadísticas de entrenadores
    List<TrainerStatisticsDTO> getTrainerStatistics(String trainerId);
    TrainerStatisticsDTO getTrainerStatisticsForMonth(String trainerId, int year, int month);
    List<TrainerStatisticsDTO> getTopTrainersOfMonth(int year, int month, int limit);

    // Cálculo automático
    void calculateAndSaveMonthlyStatistics(int year, int month);
    void calculateCurrentMonthStatistics();
}
