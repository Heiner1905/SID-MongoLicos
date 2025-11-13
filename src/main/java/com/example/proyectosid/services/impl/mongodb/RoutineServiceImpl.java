package com.example.proyectosid.services.impl.mongodb;

import com.example.proyectosid.dto.RoutineCreateDTO;
import com.example.proyectosid.dto.RoutineExerciseDTO;
import com.example.proyectosid.dto.RoutineResponseDTO;
import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.model.mongodb.RoutineExercise;
import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.repository.mongodb.RoutineRepository;
import com.example.proyectosid.repository.postgresql.UserRepository;
import com.example.proyectosid.services.mongodb.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements IRoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    @Override
    public List<RoutineResponseDTO> getAvailableRoutines(String userId) {
        // 1. Obtener rutinas activas del usuario
        List<Routine> userRoutines = routineRepository.findActiveRoutinesByUser(userId);

        // 2. Extraer IDs de rutinas que ya tiene (incluyendo originales adoptadas)
        Set<String> userRoutineIds = userRoutines.stream()
                .map(routine -> {
                    // Si es adoptada, usar el ID de la original
                    if (routine.getOriginalRoutineId() != null) {
                        return routine.getOriginalRoutineId();
                    }
                    return routine.getId();
                })
                .collect(Collectors.toSet());

        // 3. Obtener plantillas certificadas
        List<Routine> templates = routineRepository.findCertifiedTemplates();

        // 4. Filtrar plantillas que NO ha adoptado
        return templates.stream()
                .filter(template -> !userRoutineIds.contains(template.getId()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoutineResponseDTO> getActiveRoutinesByUser(String userId) {
        List<Routine> routines = routineRepository.findActiveRoutinesByUser(userId);
        return routines.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoutineResponseDTO createRoutine(RoutineCreateDTO dto, String username) {
        // Obtener datos del usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear rutina
        Routine routine = new Routine();
        routine.setName(dto.getName());
        routine.setDescription(dto.getDescription());
        routine.setCreatedAt(LocalDateTime.now());
        routine.setUpdatedAt(LocalDateTime.now());
        routine.setIsActive(true);
        routine.setOriginalRoutineId(null);

        // CreatedBy
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(username);

        if (user.getStudent() != null) {
            createdBy.setName(user.getStudent().getFirstName() + " " + user.getStudent().getLastName());
        } else if (user.getEmployee() != null) {
            createdBy.setName(user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName());
        }

        routine.setCreatedBy(createdBy);

        // Si es entrenador, puede crear plantillas certificadas
        if ("trainer".equals(user.getRole())) {
            routine.setIsPredefined(true);
            routine.setIsCertified(true);
        } else {
            routine.setIsPredefined(false);
            routine.setIsCertified(false);
        }

        // Mapear ejercicios
        List<RoutineExercise> exercises = dto.getExercises().stream()
                .map(this::mapToRoutineExercise)
                .collect(Collectors.toList());
        routine.setExercises(exercises);

        Routine saved = routineRepository.save(routine);

        return mapToDTO(saved);
    }

    @Override
    public RoutineResponseDTO adoptRoutine(String templateId, String username) {
        // Obtener plantilla
        Routine template = routineRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        if (!template.getIsPredefined()) {
            throw new RuntimeException("Solo se pueden adoptar rutinas predefinidas");
        }

        // Obtener usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear nueva rutina (copia)
        Routine adopted = new Routine();
        adopted.setName(template.getName());
        adopted.setDescription(template.getDescription());
        adopted.setExercises(template.getExercises()); // Copia directa
        adopted.setOriginalRoutineId(templateId);
        adopted.setIsPredefined(false);
        adopted.setIsCertified(false);
        adopted.setIsActive(true);
        adopted.setCreatedAt(LocalDateTime.now());
        adopted.setUpdatedAt(LocalDateTime.now());

        // CreatedBy del usuario que adopta
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(username);

        if (user.getStudent() != null) {
            createdBy.setName(user.getStudent().getFirstName() + " " + user.getStudent().getLastName());
        } else if (user.getEmployee() != null) {
            createdBy.setName(user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName());
        }

        adopted.setCreatedBy(createdBy);

        Routine saved = routineRepository.save(adopted);

        return mapToDTO(saved);
    }

    @Override
    public RoutineResponseDTO getRoutineById(String id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        return mapToDTO(routine);
    }

    @Override
    public void deactivateRoutine(String id, String username) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        // Verificar que pertenece al usuario
        if (!routine.getCreatedBy().getUserId().equals(username)) {
            throw new RuntimeException("No tienes permiso para desactivar esta rutina");
        }

        routine.setIsActive(false);
        routine.setUpdatedAt(LocalDateTime.now());

        routineRepository.save(routine);
    }

    private RoutineExercise mapToRoutineExercise(RoutineExerciseDTO dto) {
        RoutineExercise exercise = new RoutineExercise();
        exercise.setExerciseId(dto.getExerciseId());
        exercise.setName(dto.getName());
        exercise.setSets(dto.getSets());
        exercise.setReps(dto.getReps());
        exercise.setDuration(dto.getDuration());
        return exercise;
    }

    private RoutineResponseDTO mapToDTO(Routine routine) {
        RoutineResponseDTO dto = new RoutineResponseDTO();
        dto.setId(routine.getId());
        dto.setName(routine.getName());
        dto.setDescription(routine.getDescription());
        dto.setCreatedAt(routine.getCreatedAt());
        dto.setUpdatedAt(routine.getUpdatedAt());
        dto.setIsPredefined(routine.getIsPredefined());
        dto.setOriginalRoutineId(routine.getOriginalRoutineId());
        dto.setCreatedByUsername(routine.getCreatedBy().getUserId());
        dto.setCreatedByName(routine.getCreatedBy().getName());
        dto.setIsCertified(routine.getIsCertified());
        dto.setIsActive(routine.getIsActive());

        // Mapear ejercicios
        List<RoutineExerciseDTO> exerciseDTOs = routine.getExercises().stream()
                .map(this::mapRoutineExerciseToDTO)
                .collect(Collectors.toList());
        dto.setExercises(exerciseDTOs);

        return dto;
    }

    private RoutineExerciseDTO mapRoutineExerciseToDTO(RoutineExercise exercise) {
        RoutineExerciseDTO dto = new RoutineExerciseDTO();
        dto.setExerciseId(exercise.getExerciseId());
        dto.setName(exercise.getName());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        dto.setDuration(exercise.getDuration());
        return dto;
    }
}
