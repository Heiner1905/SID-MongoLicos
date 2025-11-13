package com.example.proyectosid.config;

import com.example.proyectosid.*;
import com.example.proyectosid.model.mongodb.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.InputStream;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MongoDataLoader {

    private final MongoTemplate mongoTemplate;

    @Bean
    CommandLineRunner loadMongoData() {
        return args -> {
            System.out.println("===== Verificando datos en MongoDB =====");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            // Cargar exercises
            if (mongoTemplate.getCollection("exercises").countDocuments() == 0) {
                System.out.println("Cargando exercises...");
                InputStream exercisesStream = new ClassPathResource("mongodb/exercises.json").getInputStream();
                List<Exercise> exercises = mapper.readValue(exercisesStream, new TypeReference<List<Exercise>>() {});
                mongoTemplate.insertAll(exercises);
                System.out.println("✓ Exercises cargados: " + exercises.size());
            } else {
                System.out.println("✓ Exercises ya existen: " + mongoTemplate.getCollection("exercises").countDocuments());
            }

            // Cargar routines
            if (mongoTemplate.getCollection("routines").countDocuments() == 0) {
                System.out.println("Cargando routines...");
                InputStream routinesStream = new ClassPathResource("mongodb/routines.json").getInputStream();
                List<Routine> routines = mapper.readValue(routinesStream, new TypeReference<List<Routine>>() {});
                mongoTemplate.insertAll(routines);
                System.out.println("✓ Routines cargadas: " + routines.size());
            } else {
                System.out.println("✓ Routines ya existen: " + mongoTemplate.getCollection("routines").countDocuments());
            }

            // Cargar progress
            if (mongoTemplate.getCollection("progress").countDocuments() == 0) {
                System.out.println("Cargando progress...");
                InputStream progressStream = new ClassPathResource("mongodb/progress.json").getInputStream();
                List<Progress> progress = mapper.readValue(progressStream, new TypeReference<List<Progress>>() {});
                mongoTemplate.insertAll(progress);
                System.out.println("✓ Progress cargado: " + progress.size());
            } else {
                System.out.println("✓ Progress ya existe: " + mongoTemplate.getCollection("progress").countDocuments());
            }

            // Cargar assignments
            if (mongoTemplate.getCollection("assignments").countDocuments() == 0) {
                System.out.println("Cargando assignments...");
                InputStream assignmentsStream = new ClassPathResource("mongodb/assignments.json").getInputStream();
                List<Assignment> assignments = mapper.readValue(assignmentsStream, new TypeReference<List<Assignment>>() {});
                mongoTemplate.insertAll(assignments);
                System.out.println("✓ Assignments cargados: " + assignments.size());
            } else {
                System.out.println("✓ Assignments ya existen: " + mongoTemplate.getCollection("assignments").countDocuments());
            }

            // Cargar recommendations
            if (mongoTemplate.getCollection("recommendations").countDocuments() == 0) {
                System.out.println("Cargando recommendations...");
                InputStream recommendationsStream = new ClassPathResource("mongodb/recommendations.json").getInputStream();
                List<Recommendation> recommendations = mapper.readValue(recommendationsStream, new TypeReference<List<Recommendation>>() {});
                mongoTemplate.insertAll(recommendations);
                System.out.println("✓ Recommendations cargadas: " + recommendations.size());
            } else {
                System.out.println("✓ Recommendations ya existen: " + mongoTemplate.getCollection("recommendations").countDocuments());
            }

            System.out.println("===== Datos de MongoDB verificados =====");
        };
    }
}
