package com.bytes_colaborativos.api.feature.services;

import com.bytes_colaborativos.api.feature.dto.FeatureRequest;
import com.bytes_colaborativos.api.feature.model.Feature;

public interface FeatureService {

    public Feature createFeature(FeatureRequest request);

}
