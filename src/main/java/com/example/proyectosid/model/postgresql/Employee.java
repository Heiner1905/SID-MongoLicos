package com.example.proyectosid.model.postgresql;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 15)
    private Integer id;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "email", length = 30)
    private String email;

    @Column(name = "contract_type", length = 30)
    private String contractType;

    @Column(name = "employee_type", length = 30)
    private String employeeType;

    @Column(name = "faculty_code")
    private Integer facultyCode;

    @Column(name = "campus_code")
    private Integer campusCode;

    @Column(name = "birth_place_code")
    private Integer birthPlaceCode;
}
