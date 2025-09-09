package com.bytes_colaborativos.api.auth.service;

import com.bytes_colaborativos.api.auth.commons.dto.request.ChangeRoleRequest;
import com.bytes_colaborativos.api.auth.commons.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserManagementService {
    List<UserResponse> listUsers();
    UserResponse changeUserRole(UUID userId, ChangeRoleRequest request);
}
