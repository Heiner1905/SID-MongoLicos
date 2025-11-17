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
import java.util.stream.Stream;

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

        Collection<GrantedAuthority> authorities = getAuthorities(user.getRole());

        System.out.println("Usuario: " + username);
        System.out.println("Rol: " + user.getRole());
        System.out.println("Authorities: " + authorities);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive())
                .build();
    }

    private Collection<GrantedAuthority> getAuthorities(String role) {
        Set<Permission> permissions = RolePermissions.getPermissions(role);

        Set<GrantedAuthority> permissionAuthorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());

        Set<GrantedAuthority> roleAuthorities = Set.of(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()),
                new SimpleGrantedAuthority(role.toLowerCase())
        );

        return Stream.concat(roleAuthorities.stream(), permissionAuthorities.stream())
                .collect(Collectors.toSet());
    }
}
