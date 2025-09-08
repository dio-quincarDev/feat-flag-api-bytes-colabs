package com.bytes_colaborativos.api.auth.controller;


import com.bytes_colaborativos.api.auth.commons.dto.request.ChangeRoleRequest;
import com.bytes_colaborativos.api.auth.commons.dto.request.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.request.UserEntityRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.response.UserResponse;
import com.bytes_colaborativos.api.constants.ApiPathConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Authentication & User Management", description = "Endpoints para autenticación y gestión de usuarios")
@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface AuthApi {

    @Operation(summary = "Register a new user", description = "Creates a new user with the USER role.")
    @PostMapping("/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserEntityRequest userEntityRequest);

    @Operation(summary = "Register the first SUPER_ADMIN",
            description = "Creates the initial SUPER_ADMIN user. This endpoint is conditional and can only be used once.")
    @PostMapping("/register-super-admin")
    ResponseEntity<TokenResponse> createSuperAdmin(@RequestBody @Valid UserEntityRequest userEntityRequest);

    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT.")
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    @Operation(summary = "Get current user info", description = "Retrieves basic information about the authenticated user.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") @Valid String userEntityId);

    @Operation(summary = "List all users (SUPER_ADMIN only)",
            description = "Retrieves a list of all users in the system. Requires SUPER_ADMIN role.",
            security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<List<UserResponse>> listAllUsers();

    @Operation(summary = "Change a user's role (SUPER_ADMIN only)",
            description = "Changes the role of a specific user. Requires SUPER_ADMIN role.",
            security = @SecurityRequirement(name = "JWT"))
    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<UserResponse> changeUserRole(@PathVariable UUID userId, @RequestBody @Valid ChangeRoleRequest request);
}

