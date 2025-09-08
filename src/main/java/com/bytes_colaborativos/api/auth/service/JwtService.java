package com.bytes_colaborativos.api.auth.service;

import com.bytes_colaborativos.api.auth.commons.dto.response.TokenResponse;
import io.jsonwebtoken.Claims;

public interface JwtService {
    TokenResponse generateToken(String email, String role);

    Claims getClaims(String token);

    boolean isExpired(String token);

    String extractRole(String token);

    String extractEmail(String token);
}