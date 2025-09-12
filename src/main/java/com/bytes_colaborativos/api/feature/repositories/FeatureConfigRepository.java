package com.bytes_colaborativos.api.feature.repositories;

import com.bytes_colaborativos.api.feature.model.FeatureConfig;
import com.bytes_colaborativos.api.feature.model.enums.Environment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface FeatureConfigRepository extends CrudRepository<FeatureConfig, UUID> {

    Optional<FeatureConfig> findFirstByFeature_NameAndClientId(String featureName, String clientId);

    Optional<FeatureConfig> findFirstByFeature_NameAndEnvironment(String featureName, Environment environment);
}
