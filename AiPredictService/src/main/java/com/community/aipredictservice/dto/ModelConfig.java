package com.community.aipredictservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelConfig {

    private String apiKey;

    private String baseUrl;

    private String modelName;

    private Integer timeout = 60000;

    private Integer maxTokens = 2000;

    private Double temperature = 0.7;

    private Boolean enabled = true;
}