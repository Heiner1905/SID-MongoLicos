package com.example.proyectosid.model.postgresql;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="students")
public class Student {

    @Id
    private String id;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "birth_place_code")
    private Integer birthPlaceCode;

    @Column(name = "campus_code")
    private Integer campusCode;
}
