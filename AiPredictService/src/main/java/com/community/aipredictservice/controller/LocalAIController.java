package com.community.aipredictservice.controller;

import com.community.aipredictservice.dto.ChatRequest;
import com.community.aipredictservice.dto.ChatResponse;
import com.community.aipredictservice.service.LocalAIService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/local-ai")
public class LocalAIController {

    private final LocalAIService localAIService;

    public LocalAIController(LocalAIService localAIService) {
        this.localAIService = localAIService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("收到本地AI对话请求，消息: {}", request.getMessage());
        ChatResponse response = localAIService.chat(request);
        return ResponseEntity.ok(response);
    }
}