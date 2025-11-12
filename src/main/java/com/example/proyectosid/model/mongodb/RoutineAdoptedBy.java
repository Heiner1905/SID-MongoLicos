package com.example.proyectosid.model.mongodb;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineAdoptedBy {
    private String userId;
    private LocalDateTime date;
    private boolean isModified;
}
