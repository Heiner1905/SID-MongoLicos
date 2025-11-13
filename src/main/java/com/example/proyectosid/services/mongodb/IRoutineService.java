package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.dto.RoutineCreateDTO;
import com.example.proyectosid.dto.RoutineResponseDTO;

import java.util.List;

import com.example.proyectosid.dto.RoutineRequest;
import com.example.proyectosid.model.mongodb.Routine;

public interface IRoutineService {
    List<Routine> getRoutinesByUserId(String userId);
    List<Routine> findAll();
    Routine createRoutine(RoutineRequest request);
    List<RoutineResponseDTO> getAvailableRoutines(String userId);

    List<RoutineResponseDTO> getActiveRoutinesByUser(String userId);

    RoutineResponseDTO createRoutine(RoutineCreateDTO dto, String username);

    RoutineResponseDTO adoptRoutine(String templateId, String username);

    RoutineResponseDTO getRoutineById(String id);

    void deactivateRoutine(String id, String username);

}
