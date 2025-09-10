package com.bytes_colaborativos.api.feature.dto;

public record FeatureRequest(
        String name,
        String description,
        Boolean enabledByDefault
) {
}
