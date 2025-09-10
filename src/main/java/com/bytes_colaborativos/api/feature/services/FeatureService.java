package com.bytes_colaborativos.api.feature.services;

import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.dto.FeatureToogleRequest;
import com.bytes_colaborativos.api.feature.model.Feature;

import java.util.List;

public interface FeatureService {

    public Feature createFeature(FeatureDto request);

    public List<FeatureDto> findAll();

    public FeatureDto findById(String id);

    void toggleFeature(String featureId, FeatureToogleRequest request, boolean enable);

}
