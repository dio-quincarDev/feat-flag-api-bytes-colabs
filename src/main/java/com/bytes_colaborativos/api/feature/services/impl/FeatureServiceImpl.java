package com.bytes_colaborativos.api.feature.services.impl;

import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.dto.FeatureToogleRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.model.FeatureConfig;
import com.bytes_colaborativos.api.feature.model.enums.Environment;
import com.bytes_colaborativos.api.feature.repositories.FeatureConfigRepository;
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
    private final FeatureConfigRepository featureConfigRepository;

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

    @Override
    public void toggleFeature(String featureId, FeatureToogleRequest request, boolean enable) {
        if (request.environment() == null && (request.clientId() == null || request.clientId().isBlank())) {
            throw new IllegalArgumentException("Debe proporcionar un 'environment' o un 'clientId'.");
        }
        if (request.environment() != null && request.clientId() != null && !request.clientId().isBlank()) {
            throw new IllegalArgumentException("No puede proporcionar 'environment' y 'clientId' simultáneamente.");
        }

        UUID featureUUID = UUID.fromString(featureId);
        Feature feature = featureRepository.findById(featureUUID)
                .orElseThrow(() -> new EntityNotFoundException("Feature no encontrada con id: " + featureId));

        Optional<FeatureConfig> existingConfig;

        if (request.environment() != null) {
            existingConfig = feature.getConfigs().stream()
                    .filter(c -> c.getEnvironment() != null && c.getEnvironment() == request.environment())
                    .findFirst();
        } else {
            existingConfig = feature.getConfigs().stream()
                    .filter(c -> c.getClientId() != null && c.getClientId().equals(request.clientId()))
                    .findFirst();
        }

        if (existingConfig.isPresent()) {
            existingConfig.get().setEnabled(enable);
        } else {
            FeatureConfig newConfig = new FeatureConfig();
            newConfig.setFeature(feature);
            newConfig.setEnabled(enable);
            if (request.environment() != null) {
                newConfig.setEnvironment(request.environment());
            } else {
                newConfig.setClientId(request.clientId());
            }
            feature.getConfigs().add(newConfig);
        }

        featureRepository.save(feature);
    }

    @Override
    public boolean isFeatureActive(String featureName, String clientId, Environment environment) {
        Feature feature = featureRepository.findByName(featureName)
                .orElseThrow(() -> new EntityNotFoundException("Feature no encontrada con el nombre: " + featureName));

        if (clientId != null && !clientId.isBlank()) {
            Optional<FeatureConfig> clientConfig = featureConfigRepository.findFirstByFeature_NameAndClientId(featureName, clientId);
            if (clientConfig.isPresent()) {
                return clientConfig.get().isEnabled();
            }
        }

        if (environment != null) {
            Optional<FeatureConfig> envConfig = featureConfigRepository.findFirstByFeature_NameAndEnvironment(featureName, environment);
            if (envConfig.isPresent()) {
                return envConfig.get().isEnabled();
            }
        }

        return feature.isEnabledByDefault();
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
