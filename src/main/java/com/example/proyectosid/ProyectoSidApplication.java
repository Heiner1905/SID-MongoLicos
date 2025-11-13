package com.example.proyectosid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProyectoSidApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoSidApplication.class, args);
    }

}
