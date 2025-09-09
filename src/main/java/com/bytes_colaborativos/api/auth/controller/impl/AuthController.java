package com.bytes_colaborativos.api.auth.controller.impl;



import com.bytes_colaborativos.api.auth.commons.dto.request.ChangeRoleRequest;
import com.bytes_colaborativos.api.auth.commons.dto.request.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.request.UserEntityRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.response.UserResponse;
import com.bytes_colaborativos.api.auth.controller.AuthApi;
import com.bytes_colaborativos.api.auth.service.AuthService;

import com.bytes_colaborativos.api.auth.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final UserManagementService userManagementService;

    public AuthController(AuthService authService, UserManagementService userManagementService) {
        this.authService = authService;
        this.userManagementService = userManagementService;
    }

    @Override
    public ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserEntityRequest userEntityRequest) {
        TokenResponse response = authService.createUser(userEntityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @ConditionalOnProperty(name = "app.security.super-admin-registration.enabled", havingValue = "true", matchIfMissing = false)
    public ResponseEntity<TokenResponse> createSuperAdmin(@RequestBody @Valid UserEntityRequest userEntityRequest) {
        TokenResponse response = authService.createSuperAdmin(userEntityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @Override
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        List<UserResponse> users = userManagementService.listUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserResponse> changeUserRole(@PathVariable UUID userId, @RequestBody @Valid ChangeRoleRequest request) {
        UserResponse updatedUser = userManagementService.changeUserRole(userId, request);
        return ResponseEntity.ok(updatedUser);
    }
}
