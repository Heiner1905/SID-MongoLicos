package com.example.proyectosid.services.impl.mongodb;

import com.example.proyectosid.mapper.RoutineMapper;
import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.model.mongodb.RoutineAdoptedBy;
import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.repository.mongodb.RoutineRepository;
import com.example.proyectosid.repository.postgresql.UserRepository;
import com.example.proyectosid.services.mongodb.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements IRoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;
    private final RoutineMapper routineMapper;

    @Override
    public List<Routine> getRoutinesByUserId(String userId) {
        return routineRepository.findActiveRoutinesByUserId(userId);
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

    @Override
    public Routine adoptRoutine(String templateId, String username) {
        // Obtener plantilla original
        Routine template = getRoutineById(templateId);

        if (!template.getIsPredefined() || !template.getIsCertified()) {
            throw new RuntimeException("Solo se pueden adoptar rutinas certificadas");
        }

        List<Routine> userRoutines = routineRepository.findActiveRoutinesByUserId(username);
        boolean alreadyAdopted = userRoutines.stream()
                .anyMatch(r -> templateId.equals(r.getOriginalRoutineId()));

        if (alreadyAdopted) {
            throw new RuntimeException("Ya has adoptado esta rutina");
        }

        // Obtener datos del usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear nueva rutina para el usuario
        Routine newRoutine = new Routine();
        newRoutine.setName(template.getName());
        newRoutine.setDescription(template.getDescription());
        newRoutine.setExercises(template.getExercises());
        newRoutine.setOriginalRoutineId(templateId);
        newRoutine.setIsPredefined(false); // Ya no es plantilla
        newRoutine.setIsCertified(false);
        newRoutine.setIsActive(true);
        newRoutine.setUrlImg(template.getUrlImg());
        newRoutine.setCreatedAt(LocalDateTime.now());
        newRoutine.setUpdatedAt(LocalDateTime.now());

        // CreatedBy del usuario que adopta
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(username);
        if (user.getStudent() != null) {
            createdBy.setName(user.getStudent().getFirstName() + " " + user.getStudent().getLastName());
        } else if (user.getEmployee() != null) {
            createdBy.setName(user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName());
        }
        newRoutine.setCreatedBy(createdBy);

        // ✅ Agregar a adoptedBy de la plantilla original
        RoutineAdoptedBy adoptedBy = new RoutineAdoptedBy();
        adoptedBy.setUserId(username);
        adoptedBy.setAdoptedAt(LocalDateTime.now());
        adoptedBy.setIsActive(true);
        adoptedBy.setIsModified(false);

        if (template.getAdoptedBy() == null) {
            template.setAdoptedBy(new ArrayList<>());
        }
        template.getAdoptedBy().add(adoptedBy);
        routineRepository.save(template); // Actualizar plantilla

        return routineRepository.save(newRoutine);
    }

    @Override
    public List<Routine> getRoutinesByCreator(String username) {
        return routineRepository.findByCreatedByUserId(username);
    }

    @Override
    public List<Routine> getActiveRoutinesForUser(String username) {
        // Rutinas activas creadas por el usuario
        return routineRepository.findByCreatedByUserIdAndIsActive(username, true);
    }

    @Override
    public List<Routine> getAvailableRoutinesForUser(String username) {
        // Todas las rutinas que NO fueron creadas por el usuario
        // Incluye rutinas predefinidas y certificadas de otros
        List<Routine> allRoutines = routineRepository.findAll();

        return allRoutines.stream()
                .filter(routine -> !routine.getCreatedBy().getUserId().equals(username))
                .filter(routine -> routine.getIsPredefined() || routine.getIsCertified())
                .collect(Collectors.toList());
    }
}
