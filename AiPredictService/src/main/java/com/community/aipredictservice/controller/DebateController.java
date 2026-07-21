package com.community.aipredictservice.controller;

import com.community.aipredictservice.dto.ChatRequest;
import com.community.aipredictservice.dto.ChatResponse;
import com.community.aipredictservice.dto.DebateRequest;
import com.community.aipredictservice.dto.DebateResponse;
import com.community.aipredictservice.service.DebateService;
import com.community.aipredictservice.service.LocalAIService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/debate")
public class DebateController {

    private final DebateService debateService;
    private final LocalAIService localAIService;

    public DebateController(DebateService debateService, LocalAIService localAIService) {
        this.debateService = debateService;
        this.localAIService = localAIService;
    }

    @PostMapping("/start")
    public ResponseEntity<DebateResponse> startDebate(@Valid @RequestBody DebateRequest request) {
        log.info("收到辩论请求，场景: {}, 轮数: {}", request.getScenario(), request.getMaxRounds());
        DebateResponse response = debateService.startDebate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/models")
    public ResponseEntity<Map<String, Object>> getModels() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("models", Map.of(
                "kimi", Map.of("name", "Kimi", "description", "Moonshot AI", "enabled", true),
                "doubao", Map.of("name", "豆包", "description", "字节跳动", "enabled", true),
                "qianwen", Map.of("name", "通义千问", "description", "阿里巴巴", "enabled", true),
                "zhipu", Map.of("name", "智谱AI", "description", "智谱科技", "enabled", true),
                "local", Map.of("name", "本地模型", "description", "Ollama", "enabled", true)
        ));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("收到聊天请求，消息: {}", request.getMessage());
        ChatResponse response = localAIService.chat(request);
        return ResponseEntity.ok(response);
    }
}