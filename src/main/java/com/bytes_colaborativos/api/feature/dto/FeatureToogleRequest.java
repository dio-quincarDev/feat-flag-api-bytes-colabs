package com.bytes_colaborativos.api.feature.dto;

import com.bytes_colaborativos.api.feature.model.enums.Environment;

public record FeatureToogleRequest(
        Environment environment,
        String clientId
) {
}