package com.bytes_colaborativos.api.feature.dto;

import com.bytes_colaborativos.api.feature.model.enums.Environment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para activar o desactivar un feature flag para un entorno o cliente específico")
public record FeatureToogleRequest(
        @Schema(description = "Entorno para el que se activa o desactiva el feature", example = "DEV")
        @NotNull(message = "El ambiente no puede ser nulo")
        Environment environment,
        @Schema(description = "ID del cliente para el que se activa o desactiva el feature", example = "client-123")
        String clientId
) {}