package com.bytes_colaborativos.api.feature.controllers;

import com.bytes_colaborativos.api.constants.ApiPathConstants;
import com.bytes_colaborativos.api.feature.dto.FeatureRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.services.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.V1_ROUTE+ApiPathConstants.FEATURE_ROUTE)
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    @PostMapping
    public ResponseEntity<Feature> createFeature(@RequestBody FeatureRequest featureRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createFeature(featureRequest));
    }

}
