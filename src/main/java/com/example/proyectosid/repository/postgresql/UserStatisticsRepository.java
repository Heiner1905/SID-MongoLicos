package com.example.proyectosid.repository.postgresql;


import com.example.proyectosid.model.postgresql.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {

    // Buscar estadísticas de un usuario en un mes específico
    Optional<UserStatistics> findByUserIdAndYearAndMonth(String userId, Integer year, Integer month);

    // Obtener todas las estadísticas de un usuario
    List<UserStatistics> findByUserIdOrderByYearDescMonthDesc(String userId);

    // Obtener estadísticas de un año específico
    @Query("SELECT u FROM UserStatistics u WHERE u.userId = :userId AND u.year = :year ORDER BY u.month DESC")
    List<UserStatistics> findByUserIdAndYear(@Param("userId") String userId, @Param("year") Integer year);

    // Top usuarios más activos en un mes
    @Query("SELECT u FROM UserStatistics u WHERE u.year = :year AND u.month = :month " +
            "ORDER BY u.progressLogsCount DESC")
    List<UserStatistics> findTopActiveUsers(@Param("year") Integer year, @Param("month") Integer month);

    @Query(value = "SELECT * FROM user_statistics " +
            "WHERE year = ?1 AND month = ?2 " +
            "ORDER BY (routines_started + progress_logs_count) DESC " +
            "LIMIT ?3", nativeQuery = true)
    List<UserStatistics> findTopUsersByMonth(Integer year, Integer month, int limit);
}
