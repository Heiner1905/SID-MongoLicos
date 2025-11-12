package com.example.proyectosid.repository.postgresql;


import com.example.proyectosid.model.postgresql.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Buscar por email
    Optional<Employee> findByEmail(String email);

    // Buscar todos los instructores
    List<Employee> findByEmployeeType(String employeeType);

    // Buscar entrenadores por campus
    List<Employee> findEmployeesByCampusCode( Integer campusCode);

    // Buscar por nombre
    List<Employee> searchEmployeesByFirstName(String name);
}
