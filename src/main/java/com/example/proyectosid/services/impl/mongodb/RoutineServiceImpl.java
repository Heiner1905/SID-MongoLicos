package com.example.proyectosid.services.impl.mongodb;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyectosid.dto.RoutineRequest;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.repository.mongodb.RoutineRepository;
import com.example.proyectosid.services.mongodb.IRoutineService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements IRoutineService {
    
    private final RoutineRepository repository;

    @Override
    public List<Routine> getRoutinesByUserId(String userId) {
        return repository.findAllRoutinesByUserId(userId);
    }

    @Override
    public List<Routine> findAll() {
        return repository.findAll();
    }

    @Override
    public Routine createRoutine(RoutineRequest request) {
        Routine routine = new Routine();

        routine.setName(request.getName());
        routine.setDescription(request.getDescription());
        routine.setIsPredefined(request.getIsPredefined() != null ? request.getIsPredefined() : false);
        routine.setOriginalRoutineId(request.getOriginalRoutineId());
        routine.setCreatedBy(request.getCreatedBy());
        routine.setExercises(request.getExercises());
        routine.setIsCertified(request.getIsCertified() != null ? request.getIsCertified() : false);
        routine.setIsActive(true);
        routine.setCreatedAt(LocalDateTime.now());
        routine.setUpdatedAt(LocalDateTime.now());

        return repository.save(routine);
    }
}
