package com.bytes_colaborativos.feature_flag_api.service;


import com.bytes_colaborativos.api.auth.commons.dto.request.ChangeRoleRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.UserResponse;
import com.bytes_colaborativos.api.auth.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.auth.commons.model.enums.UserRole;
import com.bytes_colaborativos.api.auth.repository.UserEntityRepository;
import com.bytes_colaborativos.api.auth.service.impl.UserManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceImplTest {

    @Mock
    private UserEntityRepository userRepository;

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    private UserEntity user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = UserEntity.builder()
                .id(userId)
                .email("testuser@example.com")
                .role(UserRole.USER)
                .build();
    }

    @Test
    @DisplayName("Test Change Role from USER to ADMIN - Success")
    void testChangeRole_UserToAdmin_Success() {
        // Arrange
        ChangeRoleRequest request = new ChangeRoleRequest();
        request.setNewRole("ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponse response = userManagementService.changeUserRole(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals(UserRole.ADMIN, response.getRole());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(UserRole.ADMIN, userCaptor.getValue().getRole());
    }

    @Test
    @DisplayName("Test Change Role to SUPER_ADMIN - Failure")
    void testChangeRole_ToSuperAdmin_Failure() {
        // Arrange
        ChangeRoleRequest request = new ChangeRoleRequest();
        request.setNewRole("SUPER_ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userManagementService.changeUserRole(userId, request);
        });

        assertEquals("No se puede ascender a un usuario a SUPER_ADMIN por esta vía.", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test Change Role of Non-Existent User - Failure")
    void testChangeRole_UserNotFound_Failure() {
        // Arrange
        ChangeRoleRequest request = new ChangeRoleRequest();
        request.setNewRole("ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userManagementService.changeUserRole(userId, request);
        });
    }

    @Test
    @DisplayName("Test Change Role of SUPER_ADMIN - Failure")
    void testChangeRole_OfSuperAdmin_Failure() {
        // Arrange
        user.setRole(UserRole.SUPER_ADMIN);
        ChangeRoleRequest request = new ChangeRoleRequest();
        request.setNewRole("ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userManagementService.changeUserRole(userId, request);
        });

        assertEquals("No se puede modificar el rol de un SUPER_ADMIN.", exception.getMessage());
    }
}
