package com.example.proyectosid.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // ← CAMBIADO: parser() en lugar de parserBuilder()
                .verifyWith(getSigningKey())  // ← CAMBIADO: verifyWith() en lugar de setSigningKey()
                .build()
                .parseSignedClaims(token)  // ← CAMBIADO: parseSignedClaims() en lugar de parseClaimsJws()
                .getPayload();  // ← CAMBIADO: getPayload() en lugar de getBody()
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)  // ← CAMBIADO: claims() en lugar de setClaims()
                .subject(subject)  // ← CAMBIADO: subject() en lugar de setSubject()
                .issuedAt(new Date(System.currentTimeMillis()))  // ← CAMBIADO: issuedAt() en lugar de setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + expiration))  // ← CAMBIADO: expiration() en lugar de setExpiration()
                .signWith(getSigningKey())  // ← CAMBIADO: ya no necesita especificar el algoritmo
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
