package com.example.proyectosid.repository.postgresql;


import com.example.proyectosid.model.postgresql.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer> {
    // Buscar por nombre
    Optional<Campus> findByName(String name);
}
