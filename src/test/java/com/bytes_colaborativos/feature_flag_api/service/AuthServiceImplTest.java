package com.bytes_colaborativos.feature_flag_api.service;

import com.bytes_colaborativos.api.commons.dto.LoginRequest;
import com.bytes_colaborativos.api.commons.dto.TokenResponse;
import com.bytes_colaborativos.api.commons.dto.UserEntityRequest;
import com.bytes_colaborativos.api.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.commons.model.enums.UserRole;
import com.bytes_colaborativos.api.repository.UserEntityRepository;
import com.bytes_colaborativos.api.service.JwtService;
import com.bytes_colaborativos.api.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserEntityRequest userEntityRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntityRequest = UserEntityRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
    }

    @Test
    @DisplayName("Test Create User - Success")
    void testCreateUser_Success() {
        when(userEntityRepository.findByEmail(userEntityRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userEntityRequest.getPassword())).thenReturn("encodedPassword");
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtService.generateToken(any(UUID.class), anyString())).thenReturn(new TokenResponse("fake-token"));

        TokenResponse tokenResponse = authService.createUser(userEntityRequest);

        assertNotNull(tokenResponse);
        assertEquals("fake-token", tokenResponse.getAccessToken());
        verify(userEntityRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test Login - Success")
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userEntity);
        when(jwtService.generateToken(userEntity.getId(), userEntity.getRole().name())).thenReturn(new TokenResponse("fake-token"));

        TokenResponse tokenResponse = authService.login(loginRequest);

        assertNotNull(tokenResponse);
        assertEquals("fake-token", tokenResponse.getAccessToken());
    }

    @Test
    @DisplayName("Test Create User - Email Already Exists (Edge Case)")
    void testCreateUser_EmailAlreadyExists() {
        when(userEntityRepository.findByEmail(userEntityRequest.getEmail())).thenReturn(Optional.of(userEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.createUser(userEntityRequest);
        });

        assertEquals("El email ya está registrado.", exception.getMessage());
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test Login - Invalid Credentials (Edge Case)")
    void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrong-password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("Test Create User - Null Request (Edge Case)")
    void testCreateUser_NullRequest() {
        assertThrows(NullPointerException.class, () -> {
            authService.createUser(null);
        });
    }
}