package com.example.proyectosid.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExerciseRequest {
    private String name;
    private String description;
    private String type; 
    private Integer duration;  
    private Integer difficulty; 
    private List<String> videos;
    private String createdByUserId;
    private String createdByName;
    private Boolean isPredefined;
}
