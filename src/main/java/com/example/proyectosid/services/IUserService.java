package com.example.proyectosid.services;

import com.example.proyectosid.dto.AssignStudentRequestDTO;
import com.example.proyectosid.dto.AssignmentResponseDTO;
import com.example.proyectosid.dto.ChangeRoleRequestDTO;
import com.example.proyectosid.dto.UserResponseDTO;

import java.util.List;

public interface IUserService {
    
    /**
     * Asignar un estudiante a un entrenador
     * @param request DTO con studentUsername y trainerUsername
     * @param adminUsername Username del admin que realiza la asignación
     * @return AssignmentResponseDTO con la información de la asignación
     */
    AssignmentResponseDTO assignStudentToTrainer(AssignStudentRequestDTO request, String adminUsername);
    
    /**
     * Cambiar el rol de un usuario (entre user y trainer)
     * @param username Username del usuario a modificar
     * @param request DTO con el nuevo rol
     * @return UserResponseDTO con la información actualizada del usuario
     */
    UserResponseDTO changeUserRole(String username, ChangeRoleRequestDTO request);
    
    /**
     * Obtener todos los entrenadores
     * @return Lista de UserResponseDTO con información de entrenadores
     */
    List<UserResponseDTO> getAllTrainers();
    
    /**
     * Obtener todos los estudiantes
     * @return Lista de UserResponseDTO con información de estudiantes
     */
    List<UserResponseDTO> getAllStudents();
    
    /**
     * Obtener todas las asignaciones activas
     * @return Lista de AssignmentResponseDTO con información de asignaciones
     */
    List<AssignmentResponseDTO> getAllActiveAssignments();
    
    /**
     * Obtener asignaciones de un entrenador específico
     * @param trainerUsername Username del entrenador
     * @return Lista de AssignmentResponseDTO con información de asignaciones
     */
    List<AssignmentResponseDTO> getAssignmentsByTrainer(String trainerUsername);
    
    /**
     * Obtener asignación de un estudiante específico
     * @param studentUsername Username del estudiante
     * @return AssignmentResponseDTO con información de la asignación activa o null
     */
    AssignmentResponseDTO getAssignmentByStudent(String studentUsername);
    
    /**
     * Desasignar un estudiante de su entrenador actual
     * @param studentUsername Username del estudiante
     * @param adminUsername Username del admin que realiza la desasignación
     */
    void unassignStudent(String studentUsername, String adminUsername);
}

