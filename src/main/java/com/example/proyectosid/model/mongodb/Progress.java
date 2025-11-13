package com.example.proyectosid.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "progress")
public class Progress {

    @Id
    private String id;

    private String userId;
    private String routineId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private ProgressExercise exercise;
    private ProgressMetrics metrics;
}
