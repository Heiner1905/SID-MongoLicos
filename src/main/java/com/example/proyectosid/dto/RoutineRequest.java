package com.example.proyectosid.dto;

import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.RoutineExercise;
import lombok.Data;

import java.util.List;

@Data
public class RoutineRequest {

    private String name;
    private String description;
    private Boolean isPredefined;
    private String originalRoutineId;
    private CreatedBy createdBy; // trainer o user que crea
    private List<RoutineExercise> exercises;
    private Boolean isCertified;
}
