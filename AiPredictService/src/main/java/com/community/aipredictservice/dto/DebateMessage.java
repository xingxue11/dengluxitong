package com.community.aipredictservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebateMessage {

    private String role;
    private String content;
    private Integer round;
    private String modelCode;
    private String modelName;
    private Long timestamp;
}