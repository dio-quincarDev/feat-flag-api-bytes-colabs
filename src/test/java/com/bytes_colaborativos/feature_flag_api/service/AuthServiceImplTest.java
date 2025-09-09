package com.bytes_colaborativos.feature_flag_api.service;

import com.bytes_colaborativos.api.auth.commons.dto.request.LoginRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.TokenResponse;
import com.bytes_colaborativos.api.auth.commons.dto.request.UserEntityRequest;
import com.bytes_colaborativos.api.auth.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.auth.commons.model.enums.UserRole;
import com.bytes_colaborativos.api.auth.repository.UserEntityRepository;
import com.bytes_colaborativos.api.auth.service.JwtService;
import com.bytes_colaborativos.api.auth.service.impl.AuthServiceImpl;
import com.bytes_colaborativos.api.exceptions.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
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
        when(jwtService.generateToken(anyString(), anyString())).thenReturn(new TokenResponse("fake-token"));

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
        when(jwtService.generateToken(userEntity.getEmail(), userEntity.getRole().name())).thenReturn(new TokenResponse("fake-token"));

        TokenResponse tokenResponse = authService.login(loginRequest);

        assertNotNull(tokenResponse);
        assertEquals("fake-token", tokenResponse.getAccessToken());
    }

    @Test
    @DisplayName("Test Create User - Email Already Exists (Edge Case)")
    void testCreateUser_EmailAlreadyExists() {
        when(userEntityRepository.findByEmail(userEntityRequest.getEmail())).thenReturn(Optional.of(userEntity));

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
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
    @DisplayName("Test Create SUPER_ADMIN - Success")
    void testCreateSuperAdmin_Success() {
        // Arrange
        when(userEntityRepository.existsByRole(UserRole.SUPER_ADMIN)).thenReturn(false);
        when(userEntityRepository.findByEmail(userEntityRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userEntityRequest.getPassword())).thenReturn("encodedPassword");
        
        UserEntity superAdminEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .email(userEntityRequest.getEmail())
                .password("encodedPassword")
                .role(UserRole.SUPER_ADMIN)
                .build();
        
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(superAdminEntity);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn(new TokenResponse("super-admin-token"));

        // Act
        TokenResponse tokenResponse = authService.createSuperAdmin(userEntityRequest);

        // Assert
        assertNotNull(tokenResponse);
        assertEquals("super-admin-token", tokenResponse.getAccessToken());
        
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository, times(1)).save(userEntityCaptor.capture());
        assertEquals(UserRole.SUPER_ADMIN, userEntityCaptor.getValue().getRole());
    }

    @Test
    @DisplayName("Test Create SUPER_ADMIN - Already Exists (Edge Case)")
    void testCreateSuperAdmin_AlreadyExists() {
        // Arrange
        when(userEntityRepository.existsByRole(UserRole.SUPER_ADMIN)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authService.createSuperAdmin(userEntityRequest);
        });

        assertEquals("Ya existe un SUPER_ADMIN en el sistema.", exception.getMessage());
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test Create SUPER_ADMIN - Email Already Exists (Edge Case)")
    void testCreateSuperAdmin_EmailAlreadyExists() {
        // Arrange
        when(userEntityRepository.existsByRole(UserRole.SUPER_ADMIN)).thenReturn(false);
        when(userEntityRepository.findByEmail(userEntityRequest.getEmail())).thenReturn(Optional.of(userEntity));

        // Act & Assert
       DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
            authService.createSuperAdmin(userEntityRequest);
        });

        assertEquals("El email ya está registrado.", exception.getMessage());
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test Create User - Role Assignment Verification")
    void testCreateUser_AssignsUserRole() {
        // Arrange
        when(userEntityRepository.findByEmail(userEntityRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userEntityRequest.getPassword())).thenReturn("encodedPassword");
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn(new TokenResponse("fake-token"));

        // Act
        authService.createUser(userEntityRequest);

        // Assert
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository).save(userEntityCaptor.capture());
        UserEntity savedUser = userEntityCaptor.getValue();
        assertEquals(UserRole.USER, savedUser.getRole());
    }
}