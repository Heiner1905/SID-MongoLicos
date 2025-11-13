package com.example.proyectosid.scheduler;

import com.example.proyectosid.services.IStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsScheduler {

    private final IStatisticsService statisticsService;

    /**
     * Se ejecuta el primer día de cada mes a las 00:01
     * Calcula estadísticas del mes anterior
     */
    @Scheduled(cron = "0 1 0 1 * ?")
    public void calculateMonthlyStatistics() {
        log.info("🕐 Iniciando cálculo automático de estadísticas mensuales");

        try {
            // Calcular estadísticas del mes anterior
            YearMonth lastMonth = YearMonth.now().minusMonths(1);
            int year = lastMonth.getYear();
            int month = lastMonth.getMonthValue();

            statisticsService.calculateAndSaveMonthlyStatistics(year, month);

            log.info("✅ Estadísticas mensuales calculadas exitosamente para {}/{}", year, month);

        } catch (Exception e) {
            log.error("❌ Error al calcular estadísticas mensuales: {}", e.getMessage(), e);
        }
    }

    /**
     * Se ejecuta cada día a las 23:00
     * Actualiza las estadísticas del mes actual
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void updateCurrentMonthStatistics() {
        log.info("🕐 Actualizando estadísticas del mes actual");

        try {
            statisticsService.calculateCurrentMonthStatistics();
            log.info("✅ Estadísticas del mes actual actualizadas");

        } catch (Exception e) {
            log.error("❌ Error al actualizar estadísticas: {}", e.getMessage(), e);
        }
    }
}
