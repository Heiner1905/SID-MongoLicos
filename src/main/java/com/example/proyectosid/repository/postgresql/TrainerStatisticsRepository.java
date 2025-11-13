package com.example.proyectosid.repository.postgresql;


import com.example.proyectosid.model.postgresql.TrainerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerStatisticsRepository extends JpaRepository<TrainerStatistics, Long> {

    // Buscar estadísticas de un entrenador en un mes específico
    Optional<TrainerStatistics> findByTrainerIdAndYearAndMonth(String trainerId, Integer year, Integer month);

    // Obtener todas las estadísticas de un entrenador
    List<TrainerStatistics> findByTrainerIdOrderByYearDescMonthDesc(String trainerId);

    // Obtener estadísticas de un año específico
    @Query("SELECT t FROM TrainerStatistics t WHERE t.trainerId = :trainerId AND t.year = :year ORDER BY t.month DESC")
    List<TrainerStatistics> findByTrainerIdAndYear(@Param("trainerId") String trainerId, @Param("year") Integer year);

    // Top entrenadores más activos
    @Query("SELECT t FROM TrainerStatistics t WHERE t.year = :year AND t.month = :month " +
            "ORDER BY t.recommendationsCount DESC")
    List<TrainerStatistics> findTopActiveTrainers(@Param("year") Integer year, @Param("month") Integer month);

    @Query(value = "SELECT * FROM trainer_statistics " +
            "WHERE year = ?1 AND month = ?2 " +
            "ORDER BY (new_assignments_count + recommendations_count) DESC " +
            "LIMIT ?3", nativeQuery = true)
    List<TrainerStatistics> findTopTrainersByMonth(Integer year, Integer month, int limit);
}
