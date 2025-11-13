package com.example.proyectosid.services.impl.mongodb;

import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.repository.mongodb.RoutineRepository;
import com.example.proyectosid.services.mongodb.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements IRoutineService {

    private final RoutineRepository routineRepository;

    @Override
    public List<Routine> getRoutinesByUserId(String userId) {
        return routineRepository.findAllRoutinesByUserId(userId);
    }

    @Override
    public List<Routine> getCertifiedTemplates() {
        return routineRepository.findCertifiedTemplates();
    }

    @Override
    public List<Routine> findAll() {
        return routineRepository.findAll();
    }

    @Override
    public Routine createRoutine(Routine routine) {
        // Establecer fechas
        routine.setCreatedAt(LocalDateTime.now());
        routine.setUpdatedAt(LocalDateTime.now());

        // Si no se especifica, establecer valores por defecto
        if (routine.getIsActive() == null) {
            routine.setIsActive(true);
        }
        if (routine.getIsPredefined() == null) {
            routine.setIsPredefined(false);
        }
        if (routine.getIsCertified() == null) {
            routine.setIsCertified(false);
        }

        return routineRepository.save(routine);
    }

    @Override
    public Routine getRoutineById(String id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada con ID: " + id));
    }

    @Override
    public Routine updateRoutine(String id, Routine routine) {
        // Verificar que existe
        Routine existing = getRoutineById(id);

        // Actualizar campos
        existing.setName(routine.getName());
        existing.setDescription(routine.getDescription());
        existing.setExercises(routine.getExercises());
        existing.setUpdatedAt(LocalDateTime.now());

        // Actualizar campos opcionales si vienen
        if (routine.getIsActive() != null) {
            existing.setIsActive(routine.getIsActive());
        }
        if (routine.getIsCertified() != null) {
            existing.setIsCertified(routine.getIsCertified());
        }

        return routineRepository.save(existing);
    }

    @Override
    public Routine deactivateRoutine(String id) {
        Routine routine = getRoutineById(id);
        routine.setIsActive(false);
        routine.setUpdatedAt(LocalDateTime.now());
        return routineRepository.save(routine);
    }

    @Override
    public void deleteRoutine(String id) {
        Routine routine = getRoutineById(id);
        routineRepository.delete(routine);
    }
}
