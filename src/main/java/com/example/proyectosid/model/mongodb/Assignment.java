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
@Document(collection = "assignments")
public class Assignment {

    @Id
    private String id;

    private String userId;          // username del usuario
    private String trainerId;       // username del entrenador
    private LocalDateTime assignedAt;
    private LocalDateTime unassignedAt;  // null si sigue activo
    private Boolean isActive;
    private String assignedBy;      // username de la persona que asignó
    private LocalDateTime createdAt;
}
