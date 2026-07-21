package com.community.aipredictservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private boolean success;
    
    private String content;
    
    private String model;
    
    private Long durationMs;
    
    private String error;

    public static ChatResponse success(String content, String model, Long durationMs) {
        return ChatResponse.builder()
                .success(true)
                .content(content)
                .model(model)
                .durationMs(durationMs)
                .build();
    }

    public static ChatResponse error(String error) {
        return ChatResponse.builder()
                .success(false)
                .error(error)
                .build();
    }
}