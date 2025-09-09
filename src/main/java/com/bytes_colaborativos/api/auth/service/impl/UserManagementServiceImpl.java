package com.bytes_colaborativos.api.auth.service.impl;

import com.bytes_colaborativos.api.auth.commons.dto.request.ChangeRoleRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.UserResponse;
import com.bytes_colaborativos.api.auth.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.auth.commons.model.enums.UserRole;
import com.bytes_colaborativos.api.auth.repository.UserEntityRepository;
import com.bytes_colaborativos.api.auth.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserEntityRepository userRepository;

    @Override
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse changeUserRole(UUID userId, ChangeRoleRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + userId));

        if (user.getRole() == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("No se puede modificar el rol de un SUPER_ADMIN.");
        }

        UserRole newRole;
        try {
            newRole = UserRole.valueOf(request.getNewRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + request.getNewRole());
        }

        if (newRole == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("No se puede ascender a un usuario a SUPER_ADMIN por esta vía.");
        }

        user.setRole(newRole);
        UserEntity updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);
    }
}
