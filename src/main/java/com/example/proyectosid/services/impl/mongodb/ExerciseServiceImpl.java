package com.example.proyectosid.services.impl.mongodb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.proyectosid.dto.ExerciseCreateDTO;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.model.mongodb.RoutineExercise;
import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.repository.mongodb.RoutineRepository;
import com.example.proyectosid.repository.postgresql.UserRepository;
import org.springframework.stereotype.Service;

import com.example.proyectosid.dto.ExerciseResponseDTO;
import com.example.proyectosid.model.mongodb.CreatedBy;
import com.example.proyectosid.model.mongodb.Exercise;
import com.example.proyectosid.repository.mongodb.ExerciseRepository;
import com.example.proyectosid.services.mongodb.IExerciseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements IExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    @Override
    public ExerciseResponseDTO createExercise(ExerciseCreateDTO dto, String username) {
        // Obtener datos del usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear ejercicio
        Exercise exercise = new Exercise();
        exercise.setName(dto.getName());
        exercise.setDescription(dto.getDescription());
        exercise.setType(dto.getType());
        exercise.setDuration(dto.getDuration());
        exercise.setDifficulty(dto.getDifficulty());
        exercise.setVideos(dto.getVideos());
        exercise.setCreatedAt(LocalDateTime.now());

        // CreatedBy
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(username);

        // Nombre completo según rol
        if (user.getStudent() != null) {
            createdBy.setName(user.getStudent().getFirstName() + " " + user.getStudent().getLastName());
        } else if (user.getEmployee() != null) {
            createdBy.setName(user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName());
        }

        exercise.setCreatedBy(createdBy);

        // Si es entrenador, puede crear predefinidos
        exercise.setIsPredefined("trainer".equals(user.getRole()));

        Exercise saved = exerciseRepository.save(exercise);

        return mapToDTO(saved);
    }

    @Override
    public List<ExerciseResponseDTO> getComplementaryExercises(String userId) {
        // 1. Obtener rutinas activas del usuario
        List<Routine> activeRoutines = routineRepository.findActiveRoutinesByUserId(userId);

        // 2. Extraer IDs de ejercicios que ya tiene
        Set<String> userExerciseIds = activeRoutines.stream()
                .flatMap(routine -> routine.getExercises().stream())
                .map(RoutineExercise::getExerciseId)
                .collect(Collectors.toSet());

        // 3. Obtener TODOS los ejercicios predefinidos
        List<Exercise> allExercises = exerciseRepository.findByIsPredefined(true);

        // 4. Filtrar ejercicios que NO están en las rutinas del usuario
        return allExercises.stream()
                .filter(exercise -> !userExerciseIds.contains(exercise.getId()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<ExerciseResponseDTO> getAllPredefinedExercises() {
        return exerciseRepository.findByIsPredefined(true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseResponseDTO getExerciseById(String id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
        return mapToDTO(exercise);
    }

    private ExerciseResponseDTO mapToDTO(Exercise exercise) {
        ExerciseResponseDTO dto = new ExerciseResponseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setType(exercise.getType());
        dto.setDuration(exercise.getDuration());
        dto.setDifficulty(exercise.getDifficulty());
        dto.setVideos(exercise.getVideos());
        // ✅ Protección contra null
        if (exercise.getCreatedBy() != null) {
            dto.setCreatedByUsername(exercise.getCreatedBy().getUserId());
            dto.setCreatedByName(exercise.getCreatedBy().getName());
        }
        dto.setIsPredefined(exercise.getIsPredefined());
        dto.setCreatedAt(exercise.getCreatedAt());
        return dto;
    }

    @Override
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> getExercisesByUserId(String userId) {
        return exerciseRepository.findByCreatorUserId(userId);
    }
    
}
