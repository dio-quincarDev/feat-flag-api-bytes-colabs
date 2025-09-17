package com.bytes_colaborativos.api.feature.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "Data Transfer Object para un Feature Flag")
public record FeatureDto(
        @Schema(description = "Identificador único del feature flag", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Nombre del feature flag", example = "new-awesome-feature")
        @NotBlank(message = "El nombre no puede estar vacío")
        String name,
        @Schema(description = "Descripción del feature flag", example = "Esta funcionalidad habilita una nueva e increíble funcionalidad")
        String description,
        @Schema(description = "Estado por defecto del feature flag", example = "true")
        Boolean enabledByDefault
) {}
