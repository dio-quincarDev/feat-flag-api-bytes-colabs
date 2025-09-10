package com.bytes_colaborativos.api.feature.repositories;

import com.bytes_colaborativos.api.feature.model.Feature;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface FeatureRepository extends CrudRepository<Feature, UUID> {

    Optional<Feature> findByName(String name);
    boolean existsByName(String name);

}
