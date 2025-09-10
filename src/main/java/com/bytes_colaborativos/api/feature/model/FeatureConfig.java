package com.bytes_colaborativos.api.feature.model;

import com.bytes_colaborativos.api.feature.model.enums.Environment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Environment environment;

    private String client;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

}
