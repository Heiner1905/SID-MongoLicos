package com.example.proyectosid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsDTO {
    private Long id;
    private String userId;
    private Integer year;
    private Integer month;
    private Integer routinesStarted;
    private Integer progressLogsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
