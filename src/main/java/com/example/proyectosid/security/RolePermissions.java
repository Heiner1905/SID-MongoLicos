// security/RolePermissions.java
package com.example.proyectosid.security;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RolePermissions {

    // Permisos para STUDENT y EMPLOYEE (son los mismos)
    public static final Set<Permission> USER_PERMISSIONS = Set.of(
            Permission.EXERCISE_READ,
            Permission.ROUTINE_READ,
            Permission.ROUTINE_CREATE,
            Permission.ROUTINE_UPDATE_OWN,
            Permission.ROUTINE_DELETE_OWN,
            Permission.ROUTINE_ADOPT,
            Permission.PROGRESS_CREATE,
            Permission.PROGRESS_READ_OWN
    );

    // Permisos para TRAINER (incluye todos los de USER + adicionales)
    public static final Set<Permission> TRAINER_PERMISSIONS = Stream.concat(
            USER_PERMISSIONS.stream(),
            Stream.of(
                    Permission.EXERCISE_CREATE_PREDEFINED,
                    Permission.ROUTINE_CREATE_CERTIFIED,
                    Permission.ASSIGNMENT_CREATE,
                    Permission.ASSIGNMENT_READ,
                    Permission.RECOMMENDATION_CREATE,
                    Permission.RECOMMENDATION_READ_ALL,
                    Permission.STATISTICS_READ_ALL
            )
    ).collect(Collectors.toSet());

    /**
     * Obtiene los permisos según el rol
     */
    public static Set<Permission> getPermissions(String role) {
        if (role == null) {
            return Set.of();
        }

        String normalizedRole = role.toUpperCase().trim();

        switch (normalizedRole) {
            case "TRAINER":
            case "ENTRENADOR":
                return TRAINER_PERMISSIONS;

            case "USER":
            case "USUARIO":
            case "STUDENT":
            case "ESTUDIANTE":
            case "EMPLOYEE":
            case "EMPLEADO":
                return USER_PERMISSIONS;

            default:
                return Set.of();
        }
    }

    /**
     * Verifica si un rol tiene un permiso específico
     */
    public static boolean hasPermission(String role, Permission permission) {
        return getPermissions(role).contains(permission);
    }
}
