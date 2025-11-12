package com.example.proyectosid.model.postgresql;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campuses")
public class Campus {
    @Id
    @Column(name = "code")
    private Integer code;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "city_code")
    private Integer cityCode;
}
