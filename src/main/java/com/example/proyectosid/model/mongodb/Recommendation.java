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
@Document(collection = "recommendations")
public class Recommendation {

    @Id
    private String id;

    private String userId;
    private String trainerId;
    private String routineId;
    private String message;
    private Boolean isRead;  // ← Cambia de "read" a "isRead"
    private LocalDateTime createdAt;
}
