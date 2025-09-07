package com.bytes_colaborativos.api.controller;

import com.bytes_colaborativos.api.commons.constants.ApiPathConstants;
import com.bytes_colaborativos.api.commons.dto.LoginRequest;
import com.bytes_colaborativos.api.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.commons.dto.UserEntityRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints para autenticación de usuarios")
@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface AuthApi {

    @PostMapping("/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserEntityRequest userEntityRequest);

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") @Valid String userEntityId);
}
