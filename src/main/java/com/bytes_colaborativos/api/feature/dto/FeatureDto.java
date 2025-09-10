package com.bytes_colaborativos.api.feature.dto;

import java.util.UUID;

public record FeatureDto(
        UUID id,
        String name,
        String description,
        Boolean enabledByDefault
) {
}
