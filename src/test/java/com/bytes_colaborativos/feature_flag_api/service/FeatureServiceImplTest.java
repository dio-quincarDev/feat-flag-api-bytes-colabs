package com.bytes_colaborativos.feature_flag_api.service;

import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.dto.FeatureToogleRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.model.FeatureConfig;
import com.bytes_colaborativos.api.feature.model.enums.Environment;
import com.bytes_colaborativos.api.feature.repositories.FeatureConfigRepository;
import com.bytes_colaborativos.api.feature.repositories.FeatureRepository;
import com.bytes_colaborativos.api.feature.services.impl.FeatureServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeatureServiceImplTest {

    @Mock
    private FeatureRepository featureRepository;

    @Mock
    private FeatureConfigRepository featureConfigRepository;

    @InjectMocks
    private FeatureServiceImpl featureService;

    private FeatureDto featureDto;
    private Feature feature;

    @Mock
    private FeatureConfig featureConfig;

    @BeforeEach
    void setUp() {
        featureDto = new FeatureDto(UUID.randomUUID(), "new-feature", "A new feature", true);
        feature = Feature.builder()
                .id(featureDto.id())
                .name(featureDto.name())
                .description(featureDto.description())
                .enabledByDefault(featureDto.enabledByDefault())
                .configs(new java.util.ArrayList<>())
                .build();
    }

    @Test
    void createFeature_shouldSucceed() {
        when(featureRepository.existsByName("new-feature")).thenReturn(false);
        when(featureRepository.save(any(Feature.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Feature result = featureService.createFeature(featureDto);

        assertNotNull(result);
        assertEquals("new-feature", result.getName());
    }

    @Test
    void createFeature_shouldFail_whenNameExists() {
        when(featureRepository.existsByName("new-feature")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            featureService.createFeature(featureDto);
        });

        assertEquals("Ya existe el feature con el nombre: new-feature", exception.getMessage());
    }

    @Test
    void createFeature_shouldFail_whenDescriptionIsBlank() {
        featureDto = new FeatureDto(null, "new-feature", "", true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            featureService.createFeature(featureDto);
        });

        assertEquals("La descripción no puede estar vacía", exception.getMessage());
    }

    @Test
    void findAll_shouldReturnListOfFeatures() {
        when(featureRepository.findAll()).thenReturn(Collections.singletonList(feature));

        List<FeatureDto> result = featureService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("new-feature", result.get(0).name());
    }

    @Test
    void findById_shouldReturnFeature() {
        when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

        FeatureDto result = featureService.findById(feature.getId().toString());

        assertNotNull(result);
        assertEquals(feature.getId(), result.id());
    }

    @Test
    void findById_shouldFail_whenFeatureNotFound() {
        when(featureRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            featureService.findById(UUID.randomUUID().toString());
        });

        assertTrue(exception.getMessage().contains("Feature no encontrada con id:"));
    }

    @Test
    void toggleFeature_shouldFail_whenNoEnvironmentOrClientId() {
        FeatureToogleRequest request = new FeatureToogleRequest(null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            featureService.toggleFeature(feature.getId().toString(), request, true);
        });

        assertEquals("Debe proporcionar un 'environment' o un 'clientId'.", exception.getMessage());
    }

    @Test
    void toggleFeature_shouldFail_whenBothEnvironmentAndClientId() {
        FeatureToogleRequest request = new FeatureToogleRequest(Environment.DEV, "client1");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            featureService.toggleFeature(feature.getId().toString(), request, true);
        });

        assertEquals("No puede proporcionar 'environment' y 'clientId' simultáneamente.", exception.getMessage());
    }

    @Test
    void isFeatureActive_shouldReturnTrue_whenEnabledForClient() {
        when(featureRepository.findByName("new-feature")).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findFirstByFeature_NameAndClientId("new-feature", "client1"))
                .thenReturn(Optional.of(featureConfig));
        when(featureConfig.isEnabled()).thenReturn(true);

        boolean isActive = featureService.isFeatureActive("new-feature", "client1", null);

        assertTrue(isActive);
    }

    @Test
    void isFeatureActive_shouldReturnFalse_whenDisabledForClient() {
        when(featureRepository.findByName("new-feature")).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findFirstByFeature_NameAndClientId("new-feature", "client1"))
                .thenReturn(Optional.of(featureConfig));
        when(featureConfig.isEnabled()).thenReturn(false);

        boolean isActive = featureService.isFeatureActive("new-feature", "client1", null);

        assertFalse(isActive);
    }

    @Test
    void isFeatureActive_shouldReturnTrue_whenEnabledForEnvironment() {
        when(featureRepository.findByName("new-feature")).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findFirstByFeature_NameAndEnvironment("new-feature", Environment.DEV))
                .thenReturn(Optional.of(featureConfig));
        when(featureConfig.isEnabled()).thenReturn(true);

        boolean isActive = featureService.isFeatureActive("new-feature", null, Environment.DEV);

        assertTrue(isActive);
    }

    @Test
    void isFeatureActive_shouldReturnFalse_whenDisabledForEnvironment() {
        when(featureRepository.findByName("new-feature")).thenReturn(Optional.of(feature));
        when(featureConfigRepository.findFirstByFeature_NameAndEnvironment("new-feature", Environment.DEV))
                .thenReturn(Optional.of(featureConfig));
        when(featureConfig.isEnabled()).thenReturn(false);

        boolean isActive = featureService.isFeatureActive("new-feature", null, Environment.DEV);

        assertFalse(isActive);
    }

    @Test
    void isFeatureActive_shouldReturnDefault_whenNoConfigMatches() {
        when(featureRepository.findByName("new-feature")).thenReturn(Optional.of(feature));

        boolean isActive = featureService.isFeatureActive("new-feature", "client1", Environment.DEV);

        assertEquals(feature.isEnabledByDefault(), isActive);
    }

    @Test
    void isFeatureActive_shouldFail_whenFeatureNotFound() {
        when(featureRepository.findByName("non-existent-feature")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            featureService.isFeatureActive("non-existent-feature", null, null);
        });

        assertEquals("Feature no encontrada con el nombre: non-existent-feature", exception.getMessage());
    }
}
