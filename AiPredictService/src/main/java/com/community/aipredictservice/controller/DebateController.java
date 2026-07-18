package com.community.aipredictservice.controller;

import com.community.aipredictservice.dto.DebateRequest;
import com.community.aipredictservice.dto.DebateResponse;
import com.community.aipredictservice.service.DebateService;
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

    public DebateController(DebateService debateService) {
        this.debateService = debateService;
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
                "zhipu", Map.of("name", "智谱AI", "description", "智谱科技", "enabled", true)
        ));
        return ResponseEntity.ok(result);
    }
}