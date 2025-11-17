package com.example.proyectosid.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExerciseResponseDTO {
    private String id;
    private String name;
    private String description;
    private String type;
    private Integer duration;
    private Integer difficulty;
    private String urlImg;
    private List<String> videos;
    private String createdByUsername;
    private String createdByName;
    private Boolean isPredefined;
    private LocalDateTime createdAt;
}
