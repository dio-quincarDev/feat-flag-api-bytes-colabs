package com.bytes_colaborativos.api.service.impl;

import com.bytes_colaborativos.api.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);
    private final SecretKey secretKey;
    private static final long EXPIRATION_TIME = 864_000_000; // 10 días

    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
        if (secret.getBytes().length < 32) {
            throw new IllegalArgumentException("La clave secreta de JWT debe tener al menos 32 caracteres.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public TokenResponse generateToken(UUID userEntityId, String role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        String normalizedRole = role.startsWith("ROLE_") ? role.substring(5) : role;

        String token = Jwts.builder()
                .subject(userEntityId.toString())
                .claim("userEntityId", userEntityId.toString()) // Guardar como String
                .claim("role", normalizedRole)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();

        return TokenResponse.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error al parsear JWT: {}, Causa: {}", e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : "N/A");
            throw new IllegalArgumentException("Token JWT inválido o expirado", e);
        }
    }

    @Override
    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            // Si hay cualquier error al parsear, se considera expirado/inválido.
            return true;
        }
    }

    @Override
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    @Override
    public UUID extractUserEntityId(String token) {
        final Claims claims = getClaims(token);
        String userIdAsString = claims.get("userEntityId", String.class);

        if (userIdAsString == null) {
            // Fallback por si el claim personalizado no estuviera, usamos el subject.
            userIdAsString = claims.getSubject();
        }

        if (userIdAsString == null) {
            throw new IllegalArgumentException("El token no contiene el ID de usuario (userEntityId o subject).");
        }

        return UUID.fromString(userIdAsString);
    }
}