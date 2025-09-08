package com.bytes_colaborativos.api.auth.service.impl;

import com.bytes_colaborativos.api.auth.commons.dto.request.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.request.UserEntityRequest;
import com.bytes_colaborativos.api.auth.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.auth.commons.model.enums.UserRole;
import com.bytes_colaborativos.api.auth.repository.UserEntityRepository;
import com.bytes_colaborativos.api.auth.service.AuthService;
import com.bytes_colaborativos.api.auth.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bytes_colaborativos.api.exceptions.DuplicateEmailException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse createUser(@Valid UserEntityRequest userEntityRequest) {
        log.info("Intentando crear usuario para email: {}", userEntityRequest.getEmail());

        if (userEntityRepository.findByEmail(userEntityRequest.getEmail()).isPresent()) {
            log.warn("Intento de crear usuario con email existente: {}", userEntityRequest.getEmail());
            throw new DuplicateEmailException("El email ya está registrado.");
        }

        UserEntity userToSave = mapToEntity(userEntityRequest, UserRole.USER);
        UserEntity userCreated = userEntityRepository.save(userToSave);
        log.info("Usuario creado exitosamente con ID: {}", userCreated.getId());

        return jwtService.generateToken(userCreated.getEmail(), userCreated.getRole().name());
    }

    @Override
    public TokenResponse createSuperAdmin(UserEntityRequest userEntityRequest) {
        log.info("Intentando crear SUPER_ADMIN para email: {}", userEntityRequest.getEmail());

        if (userEntityRepository.existsByRole(UserRole.SUPER_ADMIN)) {
            log.warn("Intento de crear SUPER_ADMIN cuando ya existe uno.");
            throw new IllegalStateException("Ya existe un SUPER_ADMIN en el sistema.");
        }

        if (userEntityRepository.findByEmail(userEntityRequest.getEmail()).isPresent()) {
            log.warn("Intento de crear SUPER_ADMIN con email existente: {}", userEntityRequest.getEmail());
            throw new DuplicateEmailException("El email ya está registrado.");
        }

        UserEntity userToSave = mapToEntity(userEntityRequest, UserRole.SUPER_ADMIN);
        UserEntity userCreated = userEntityRepository.save(userToSave);
        log.info("SUPER_ADMIN creado exitosamente con ID: {}", userCreated.getId());

        return jwtService.generateToken(userCreated.getEmail(), userCreated.getRole().name());
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        log.info("Intentando login para usuario: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserEntity user = (UserEntity) authentication.getPrincipal();

        log.info("Login exitoso para usuario: {}", user.getEmail());
        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    private UserEntity mapToEntity(UserEntityRequest userEntityRequest, UserRole role) {
        return UserEntity.builder()
                .email(userEntityRequest.getEmail())
                .password(passwordEncoder.encode(userEntityRequest.getPassword()))
                .firstName(userEntityRequest.getFirstName())
                .lastName(userEntityRequest.getLastName())
                .role(role)
                .build();
    }
}