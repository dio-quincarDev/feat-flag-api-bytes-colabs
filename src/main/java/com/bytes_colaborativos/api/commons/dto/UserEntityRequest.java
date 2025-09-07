package com.bytes_colaborativos.api.commons.dto;

import com.bytes_colaborativos.api.commons.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para la creación de un usuario")
public class UserEntityRequest {

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El email no puede estar vacío")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Schema(description = "Contraseña del usuario", example = "passwordSegura123")
    private String password;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;

    @NotNull(message = "El rol no puede ser nulo")
    @Schema(description = "Rol del usuario", example = "USER")
    private UserRole role;
}