package com.bytes_colaborativos.api.auth.controller.impl;

import com.bytes_colaborativos.api.auth.commons.dto.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.UserEntityRequest;
import com.bytes_colaborativos.api.auth.controller.AuthApi;
import com.bytes_colaborativos.api.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserEntityRequest userEntityRequest) {
        TokenResponse response = authService.createUser(userEntityRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") @Valid String userEntityId) {
        return ResponseEntity.ok(userEntityId);
    }
}
