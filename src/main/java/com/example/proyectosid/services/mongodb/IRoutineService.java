package com.example.proyectosid.services.mongodb;

import java.util.List;

import com.example.proyectosid.dto.RoutineRequest;
import com.example.proyectosid.model.mongodb.Routine;

public interface IRoutineService {
    List<Routine> getRoutinesByUserId(String userId);
    List<Routine> findAll();
    Routine createRoutine(RoutineRequest request);
}
