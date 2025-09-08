package com.bytes_colaborativos.api.auth.commons.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeRoleRequest {
    @NotBlank(message = "El nuevo rol no puede estar vacío.")
    private String newRole;
}
