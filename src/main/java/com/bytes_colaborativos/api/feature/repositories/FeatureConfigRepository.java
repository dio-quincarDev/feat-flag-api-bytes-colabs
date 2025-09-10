package com.bytes_colaborativos.api.feature.repositories;

import com.bytes_colaborativos.api.feature.model.FeatureConfig;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FeatureConfigRepository extends CrudRepository<FeatureConfig, UUID> {
}
