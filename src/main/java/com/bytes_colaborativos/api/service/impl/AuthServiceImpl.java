package com.bytes_colaborativos.api.service.impl;

import com.bytes_colaborativos.api.commons.dto.LoginRequest;
import com.bytes_colaborativos.api.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.commons.dto.UserEntityRequest;
import com.bytes_colaborativos.api.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.commons.model.enums.UserRole;
import com.bytes_colaborativos.api.repository.UserEntityRepository;
import com.bytes_colaborativos.api.service.AuthService;
import com.bytes_colaborativos.api.service.JwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse createUser(@Valid UserEntityRequest userEntityRequest) {
        log.info("Intentando crear usuario para email: {}", userEntityRequest.getEmail());

        if (userEntityRepository.findByEmail(userEntityRequest.getEmail()).isPresent()){
            log.warn("Intento de crear usuario con email existente: {}", userEntityRequest.getEmail());
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        try{
            UserEntity userToSave = mapToEntity(userEntityRequest);
            UserEntity userCreated = userEntityRepository.save(userToSave);
            log.info("Usuario creado exitosamente con ID: {}", userCreated.getId());

            String roleName = userCreated.getRole().name();
            return jwtService.generateToken(userCreated.getId(), roleName);

        } catch (Exception e) {
            log.error("Error durante la creación del usuario para email {}: {}", userEntityRequest.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Error interno al crear el usuario.", e);
        }
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        log.info("Intentando login para usuario: {}", loginRequest.getEmail());
        UserEntity user = userEntityRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->{
                    log.warn("Intento de login fallido - Usuario no encontrado: {}", loginRequest.getEmail());
                    return new IllegalArgumentException("Usuario o contraseña inválidos.");
                });
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            log.warn("Intento de login fallido - Contraseña inválida para usuario: {}", loginRequest.getEmail());
            throw new IllegalArgumentException("Usuario o contraseña inválidos.");
        }

        String roleName = user.getRole().name();
        log.info("Login exitoso para usuario: {}", user.getEmail());
        return jwtService.generateToken(user.getId(), roleName);
    }

    private UserEntity mapToEntity(UserEntityRequest userEntityRequest){
        UserRole requestedRole = userEntityRequest.getRole();

        if (requestedRole == null) {
            log.error("El rol llegó nulo para el usuario {}", userEntityRequest.getEmail());
            requestedRole = UserRole.ADMIN;
        }

        log.debug("Mapeando DTO a Entidad con Rol: {}", requestedRole);

        return UserEntity.builder()
                .email(userEntityRequest.getEmail())
                .password(passwordEncoder.encode(userEntityRequest.getPassword()))
                .role(requestedRole)
                .build();
    }
}