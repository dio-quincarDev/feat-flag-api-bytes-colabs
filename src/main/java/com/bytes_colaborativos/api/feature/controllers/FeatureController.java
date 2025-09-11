package com.bytes_colaborativos.api.feature.controllers;

import com.bytes_colaborativos.api.constants.ApiPathConstants;
import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.dto.FeatureToogleRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.services.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPathConstants.V1_ROUTE+ApiPathConstants.FEATURE_ROUTE)
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    @PostMapping
    public ResponseEntity<Feature> createFeature(@RequestBody FeatureDto featureRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createFeature(featureRequest));
    }

    @GetMapping
    public ResponseEntity<List<FeatureDto>> getAllFeatures(){
        return ResponseEntity.status(HttpStatus.OK).body(featureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureDto> getById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(featureService.findById(id));
    }

    @PostMapping("/{id}/enable")
    @PreAuthorize("hasRole(SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> enableFeature(@PathVariable String id, @RequestBody FeatureToogleRequest request) {
        featureService.toggleFeature(id, request, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disable")
    @PreAuthorize("hasRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> disableFeature(@PathVariable String id, @RequestBody FeatureToogleRequest request) {
        featureService.toggleFeature(id, request, false);
        return ResponseEntity.noContent().build();
    }

}
