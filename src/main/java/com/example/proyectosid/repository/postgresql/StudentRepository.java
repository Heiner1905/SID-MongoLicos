package com.example.proyectosid.repository.postgresql;


import com.example.proyectosid.model.postgresql.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    // Buscar por email
    Optional<Student> findByEmail(String email);

    // Buscar estudiantes por campus
    List<Student> findByCampusCode(Integer campusCode);

    // Buscar estudiantes por nombre
    List<Student> searchStudentsByName(String name);
}
