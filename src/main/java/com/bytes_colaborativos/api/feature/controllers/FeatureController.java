package com.bytes_colaborativos.api.feature.controllers;

import com.bytes_colaborativos.api.constants.ApiPathConstants;
import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.dto.FeatureToogleRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.model.enums.Environment;
import com.bytes_colaborativos.api.feature.services.FeatureService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiPathConstants.V1_ROUTE+ApiPathConstants.FEATURE_ROUTE)
@RequiredArgsConstructor
@Validated
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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> enableFeature(@PathVariable String id, @RequestBody FeatureToogleRequest request) {
        featureService.toggleFeature(id, request, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> disableFeature(@PathVariable String id, @RequestBody FeatureToogleRequest request) {
        featureService.toggleFeature(id, request, false);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFeature(
            @RequestParam @NotBlank String feature,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) Environment env) {
        boolean isActive = featureService.isFeatureActive(feature, clientId, env);
        return ResponseEntity.ok(Collections.singletonMap("isActive", isActive));
    }

}
