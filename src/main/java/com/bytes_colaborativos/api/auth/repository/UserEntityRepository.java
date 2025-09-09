package com.bytes_colaborativos.api.auth.repository;

import com.bytes_colaborativos.api.auth.commons.model.entity.UserEntity;
import com.bytes_colaborativos.api.auth.commons.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository <UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByRole(UserRole role);
}
