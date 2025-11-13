package com.example.proyectosid.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "routines")
public class Routine {

    @Id
    private String id;

    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Si es una plantilla que otros pueden adoptar
    private Boolean isPredefined;

    // Si esta rutina fue adoptada de una plantilla, aquí va el ID de la original
    private String originalRoutineId;

    private CreatedBy createdBy;
    private List<RoutineExercise> exercises;
    private Boolean isCertified;

    // Si el usuario aún usa esta rutina
    private Boolean isActive;
}
