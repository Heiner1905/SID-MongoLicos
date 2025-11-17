package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.dto.RoutineCreateDTO;
import com.example.proyectosid.dto.RoutineResponseDTO;

import java.util.List;

import com.example.proyectosid.dto.RoutineRequest;
import com.example.proyectosid.model.mongodb.Routine;

public interface IRoutineService {
    List<Routine> getRoutinesByUserId(String userId);

    List<Routine> getCertifiedTemplates();

    List<Routine> findAll();

    Routine createRoutine(Routine routine);

    Routine getRoutineById(String id);

    Routine updateRoutine(String id, Routine routine);

    Routine deactivateRoutine(String id);

    void deleteRoutine(String id);

    Routine adoptRoutine(String templateId, String username);

    /**
     * Obtener rutinas creadas por el usuario
     */
    List<Routine> getRoutinesByCreator(String username);

    /**
     * Obtener rutinas activas para el usuario (propias y adoptadas)
     */
    List<Routine> getActiveRoutinesForUser(String username);

    /**
     * Obtener rutinas disponibles (no creadas por el usuario)
     * Incluye rutinas predefinidas y certificadas de otros usuarios
     */
    List<Routine> getAvailableRoutinesForUser(String username);

}
