package com.example.proyectosid.model.mongodb;


import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exercises")
public class Exercise {

    @Id
    private String id;

    private String name;
    private String description;
    private String type;           // "cardio", "fuerza", "movilidad"
    private LocalDateTime createdAt;
    private Integer duration;      // minutos
    private Integer difficulty;    // 1-5
    private List<String> videos;   // URLs
    private CreatedBy createdBy;
    private Boolean isPredefined;
}
