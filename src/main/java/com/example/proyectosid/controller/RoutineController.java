package com.example.proyectosid.controller;

import com.example.proyectosid.dto.RoutineRequest;
import com.example.proyectosid.model.mongodb.Routine;
import com.example.proyectosid.services.mongodb.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoutineController {

    private final IRoutineService routineService;

    // Rutinas por user_id
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Routine>> getRoutinesByUser(@PathVariable String userId) {
        List<Routine> routines = routineService.getRoutinesByUserId(userId);
        return ResponseEntity.ok(routines);
    }

    // Obtener todas las rutinas
    @GetMapping("/all")
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List<Routine> routines = routineService.findAll();
        return ResponseEntity.ok(routines);
    }

    // Crear rutina
    @PostMapping("/createRoutine")
    public ResponseEntity<Routine> createRoutine(@RequestBody RoutineRequest request) {
        Routine created = routineService.createRoutine(request);
        return ResponseEntity.ok(created);
    }
}
