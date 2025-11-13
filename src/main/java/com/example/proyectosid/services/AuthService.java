package com.example.proyectosid.services;

import com.example.proyectosid.dto.LoginRequest;
import com.example.proyectosid.dto.LoginResponse;
import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.repository.postgresql.UserRepository;
import com.example.proyectosid.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Obtener datos del usuario
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // ✅ Determinar el ID real del usuario (student_id o employee_id)
            String userId;
            if (user.getStudent() != null) {
                userId = user.getStudent().getId();  // Ej: "A001"
            } else if (user.getEmployee() != null) {
                userId = String.valueOf(user.getEmployee().getId());  // Ej: "1", "2"
            } else {
                userId = user.getUsername();  // Fallback
            }

            // ✅ Generar token con username, role Y userId
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), userId);

            // Construir respuesta
            String fullName = "";
            String email = "";

            if (user.getStudent() != null) {
                fullName = user.getStudent().getFirstName() + " " + user.getStudent().getLastName();
                email = user.getStudent().getEmail();
            } else if (user.getEmployee() != null) {
                fullName = user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName();
                email = user.getEmployee().getEmail();
            }

            return LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .username(user.getUsername())
                    .userId(userId)  // ✅ Agregar userId a la respuesta
                    .role(user.getRole())
                    .email(email)
                    .fullName(fullName)
                    .build();

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
