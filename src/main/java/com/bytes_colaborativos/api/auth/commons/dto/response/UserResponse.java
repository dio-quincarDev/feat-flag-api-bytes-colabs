package com.bytes_colaborativos.api.auth.commons.dto.response;

import com.bytes_colaborativos.api.auth.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.auth.commons.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    public static UserResponse fromEntity(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .build();
    }
}
