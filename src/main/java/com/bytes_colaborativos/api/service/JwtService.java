package com.bytes_colaborativos.api.service;

import com.bytes_colaborativos.api.commons.dto.TokenResponse;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {
    TokenResponse generateToken (UUID userEntityId, String role);
    Claims getClaims (String token);
    boolean isExpired(String token);
    String extractRole(String token);
    UUID extractUserEntityId(String Token);
}
