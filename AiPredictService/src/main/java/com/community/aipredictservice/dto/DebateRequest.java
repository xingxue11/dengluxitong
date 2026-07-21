package com.community.aipredictservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebateRequest {

    @NotBlank(message = "辩论场景不能为空")
    private String scenario;

    private Integer maxRounds = 4;

    private Boolean kimiEnabled = false;

    private Boolean doubaoEnabled = true;

    private Boolean qianwenEnabled = false;

    private Boolean zhipuEnabled = false;

    private Boolean localEnabled = true;
}