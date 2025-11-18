package com.example.proyectosid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeRoleRequestDTO {

    @NotBlank(message = "El nuevo rol es obligatorio")
    @Pattern(regexp = "^(user|trainer)$", message = "El rol debe ser 'user' o 'trainer'")
    private String newRole;
}

