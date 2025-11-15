// security/Permission.java
package com.example.proyectosid.security;

public enum Permission {
    // Permisos básicos (todos los usuarios)
    EXERCISE_READ,
    ROUTINE_READ,
    ROUTINE_CREATE,
    ROUTINE_UPDATE_OWN,
    ROUTINE_DELETE_OWN,
    PROGRESS_CREATE,
    PROGRESS_READ_OWN,

    // Permisos de adopción
    ROUTINE_ADOPT,

    // Permisos de trainer
    EXERCISE_CREATE_PREDEFINED,
    ROUTINE_CREATE_CERTIFIED,
    ASSIGNMENT_CREATE,
    ASSIGNMENT_READ,
    RECOMMENDATION_CREATE,
    RECOMMENDATION_READ_ALL,
    STATISTICS_READ_ALL
}
