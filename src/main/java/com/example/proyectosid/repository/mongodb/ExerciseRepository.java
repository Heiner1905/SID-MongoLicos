package com.example.proyectosid.repository.mongodb;

import com.example.proyectosid.model.mongodb.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {

    // Buscar ejercicios predefinidos
    List<Exercise> findByIsPredefined(Boolean isPredefined);

    // Buscar por tipo
    List<Exercise> findByType(String type);

    // Buscar por tipo y predefinidos
    List<Exercise> findByTypeAndIsPredefined(String type, Boolean isPredefined);

    // Buscar ejercicios creados por un usuario
    @Query("{ 'createdBy.userId': ?0 }")
    List<Exercise> findByCreatorUserId(String userId);

    // Buscar por dificultad
    List<Exercise> findByDifficultyLessThanEqual(Integer difficulty);

    // Buscar por nombre (búsqueda parcial - case insensitive)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Exercise> searchByName(String name);

    // Ejercicios predefinidos por tipo y dificultad
    @Query("{ 'isPredefined': true, 'type': ?0, 'difficulty': { $lte: ?1 } }")
    List<Exercise> findPredefinedByTypeAndMaxDifficulty(String type, Integer maxDifficulty);
}
