package com.example.proyectosid.model.mongodb;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recommendations")
public class Recommendation {

    @Id
    private String id;

    private String userId;       // quien recibe
    private String trainerId;    // quien recomienda
    private String routineId;    // rutina sobre la que se recomienda
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead; // AGREGAR AL MODELO
}
