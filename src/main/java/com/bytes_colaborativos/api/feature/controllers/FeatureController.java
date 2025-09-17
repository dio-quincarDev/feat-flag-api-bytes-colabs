package com.bytes_colaborativos.api.feature.controllers;

import com.bytes_colaborativos.api.constants.ApiPathConstants;
import com.bytes_colaborativos.api.feature.dto.FeatureDto;
import com.bytes_colaborativos.api.feature.dto.FeatureToogleRequest;
import com.bytes_colaborativos.api.feature.model.Feature;
import com.bytes_colaborativos.api.feature.model.enums.Environment;
import com.bytes_colaborativos.api.feature.services.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Gestión de Feature Flags", description = "Endpoints para la gestión de feature flags")
public class FeatureController {

    private final FeatureService featureService;

    @Operation(summary = "Crear un nuevo feature flag", description = "Crea un nuevo feature flag.")
    @ApiResponse(responseCode = "201", description = "Feature flag creado exitosamente")
    @PostMapping
    public ResponseEntity<Feature> createFeature(@RequestBody @Valid FeatureDto featureRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createFeature(featureRequest));
    }

    @Operation(summary = "Obtener todos los feature flags", description = "Recupera una lista de todos los feature flags.")
    @GetMapping
    public ResponseEntity<List<FeatureDto>> getAllFeatures(){
        return ResponseEntity.status(HttpStatus.OK).body(featureService.findAll());
    }

    @Operation(summary = "Obtener un feature flag por ID", description = "Recupera un feature flag por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<FeatureDto> getById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(featureService.findById(id));
    }

    @Operation(summary = "Habilitar un feature flag", description = "Habilita un feature flag para un entorno o cliente específico. Requiere rol de SUPER_ADMIN o ADMIN.", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> enableFeature(@PathVariable String id, @RequestBody @Valid FeatureToogleRequest request) {
        featureService.toggleFeature(id, request, true);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deshabilitar un feature flag", description = "Deshabilita un feature flag para un entorno o cliente específico. Requiere rol de SUPER_ADMIN o ADMIN.", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> disableFeature(@PathVariable String id, @RequestBody @Valid FeatureToogleRequest request) {
        featureService.toggleFeature(id, request, false);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar si un feature está activo", description = "Verifica si un feature está activo para un contexto dado (cliente o entorno).")
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFeature(
            @RequestParam @NotBlank String feature,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) Environment env) {
        boolean isActive = featureService.isFeatureActive(feature, clientId, env);
        return ResponseEntity.ok(Collections.singletonMap("isActive", isActive));
    }

}
