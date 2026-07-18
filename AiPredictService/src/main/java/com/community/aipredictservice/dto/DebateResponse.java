package com.community.aipredictservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebateResponse {

    private Boolean success;

    private String message;

    private String scenario;

    private Integer totalRounds;

    private List<DebateMessage> messages;

    private Long durationMs;

    private Map<String, String> report;
}