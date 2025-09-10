package com.bytes_colaborativos.api.feature.services.impl;

import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.repositories.FeatureRepository;
import com.bytes_colaborativos.api.feature.services.FeatureService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private static final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository featureRepository;

    @Override
    public Feature createFeature(FeatureDto featureRequest) {

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

    @Override
    public List<FeatureDto> findAll() {
        List<Feature> features = (List<Feature>)featureRepository.findAll();
        return features.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public FeatureDto findById(String id) {
        return featureRepository.findById(UUID.fromString(id))
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Feature no encontrada con id: "+id));
    }

    private Feature mapToEntity(FeatureDto dto){
        return Feature.builder()
                .name(dto.name())
                .description(dto.description())
                .enabledByDefault(Optional.ofNullable(dto.enabledByDefault()).orElse(true))
                .build();
    }

    private FeatureDto mapToDto(Feature feature){
        return new FeatureDto(
                feature.getId(),
                feature.getName(),
                feature.getDescription(),
                feature.isEnabledByDefault());
    }
}
