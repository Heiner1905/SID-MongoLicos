// security/CustomUserDetailsService.java
package com.example.proyectosid.security;

import com.example.proyectosid.model.postgresql.User;
import com.example.proyectosid.repository.postgresql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (!user.getIsActive()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                getAuthorities(user.getRole())
        );
    }

    /**
     * Convierte el rol y permisos en GrantedAuthorities
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        // Agregar el rol como ROLE_XXX
        Set<GrantedAuthority> authorities = RolePermissions.getPermissions(role)
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());

        // Agregar también el rol
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        return authorities;
    }
}
