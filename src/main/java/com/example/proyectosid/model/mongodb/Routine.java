package com.example.proyectosid.model.mongodb;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Boolean isPredefined;
    private CreatedBy createdBy;
    private List<RoutineExercise> exercises;
    private Boolean isCertified;
    private List<RoutineAdoptedBy> owners;
    private List<Recommendation> recommendations;
}
