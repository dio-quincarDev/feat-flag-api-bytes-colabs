package com.bytes_colaborativos.api.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para la creación o actualización de un usuario")
public class UserEntityRequest {

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El email no puede estar vacío")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacío")
    @Schema(description = "Contraseña del usuario", example = "passwordSegura123")
    private String password;

}
