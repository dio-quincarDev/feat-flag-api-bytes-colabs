package com.bytes_colaborativos.api.feature.repositories;

import com.bytes_colaborativos.api.feature.model.Feature;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FeatureRepository extends CrudRepository<Feature, Long> {

    Optional<Feature> findByName(String name);
    boolean existsByName(String name);

}
