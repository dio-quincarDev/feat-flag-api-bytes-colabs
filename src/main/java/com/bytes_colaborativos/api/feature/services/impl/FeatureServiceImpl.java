package com.bytes_colaborativos.api.feature.services.impl;

import com.bytes_colaborativos.api.feature.dto.FeatureRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.repositories.FeatureRepository;
import com.bytes_colaborativos.api.feature.services.FeatureService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private static final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository featureRepository;

    @Override
    public Feature createFeature(FeatureRequest featureRequest) {

        if(featureRepository.existsByName(featureRequest.name())){
            log.warn("Ya existe el feature con el nombre: {}", featureRequest.name());
            throw new IllegalArgumentException("Ya existe el feature con el nombre: " + featureRequest.name());
        }

        if (featureRequest.description() == null || featureRequest.description().isBlank()) {
            log.warn("La descripción no puede estar vacía");
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }

        Feature newFeature =  mapToEntity(featureRequest);
        featureRepository.save(newFeature);
        return newFeature;
    }

    private Feature mapToEntity(FeatureRequest request){
        return Feature.builder()
                .name(request.name())
                .description(request.description())
                .enabledByDefault(Optional.ofNullable(request.enabledByDefault()).orElse(true))
                .build();
    }
}
