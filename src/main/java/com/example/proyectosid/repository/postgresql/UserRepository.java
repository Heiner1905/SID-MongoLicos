package com.example.proyectosid.repository.postgresql;

import com.example.proyectosid.model.postgresql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Buscar por username
    Optional<User> findByUsername(String username);

    // Buscar usuarios activos por rol
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findActiveUsersByRole(@Param("role") String role);

    // Verificar si existe un usuario
    boolean existsByUsername(String username);

    // Buscar usuarios por employee_id
    Optional<User> findByEmployeeId(Integer employeeId);

    // Buscar usuarios por student_id
    Optional<User> findByStudentId(String studentId);

    List<User> findByRole(String role);

    // Buscar usuarios por rol con relaciones cargadas (JOIN FETCH)
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.student LEFT JOIN FETCH u.employee WHERE u.role = :role")
    List<User> findByRoleWithRelations(@Param("role") String role);
}
