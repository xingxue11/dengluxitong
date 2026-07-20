package com.community.aipredictservice.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.community.aipredictservice.dto.DebateMessage;
import com.community.aipredictservice.dto.DebateRequest;
import com.community.aipredictservice.dto.DebateResponse;
import com.community.aipredictservice.dto.WebSocketDebateMessage;
import com.community.aipredictservice.enums.ModelType;
import com.community.aipredictservice.service.DebateService;
import com.community.aipredictservice.websocket.DebateWebSocketHandler;

@Service
public class DebateServiceImpl implements DebateService {

    private static final int DEFAULT_MAX_ROUNDS = 4;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(DebateServiceImpl.class);

    private final Map<ModelType, ChatClient> chatClientMap;
    private final Map<ModelType, String> modelBaseUrlMap;
    private final DebateWebSocketHandler webSocketHandler;

    public DebateServiceImpl(Map<ModelType, ChatClient> chatClientMap, Map<ModelType, String> modelBaseUrlMap, DebateWebSocketHandler webSocketHandler) {
        this.chatClientMap = chatClientMap;
        this.modelBaseUrlMap = modelBaseUrlMap;
        this.webSocketHandler = webSocketHandler;
        log.info("DebateServiceImpl initialized, WebSocketHandler: {}", webSocketHandler != null ? "available" : "null");
        if (webSocketHandler != null) {
            log.info("Active WebSocket sessions: {}", webSocketHandler.getSessionCount());
        }
    }

    @Override
    public DebateResponse startDebate(DebateRequest request) {
        long startTime = System.currentTimeMillis();

        String scenario = request.getScenario();
        int maxRounds = request.getMaxRounds() != null ? request.getMaxRounds() : DEFAULT_MAX_ROUNDS;

        List<ModelType> enabledModels = getEnabledModels(request);
        if (enabledModels.isEmpty()) {
            broadcastMessage(WebSocketDebateMessage.error("No AI models enabled"));
            return DebateResponse.builder()
                    .success(false)
                    .message("No AI models enabled")
                    .build();
        }

        List<DebateMessage> allMessages = new ArrayList<>();

        broadcastMessage(WebSocketDebateMessage.start(scenario, maxRounds));

        try {
            for (int round = 1; round <= maxRounds; round++) {
                log.info("Round {} starts", round);
                broadcastMessage(WebSocketDebateMessage.roundStart(round, maxRounds));

                for (ModelType modelType : enabledModels) {
                    ChatClient chatClient = chatClientMap.get(modelType);
                    if (chatClient == null) {
                        log.warn("Model {} not configured or disabled, skipping", modelType.getName());
                        continue;
                    }

                    try {
                        String prompt = buildDebatePrompt(scenario, allMessages, round, modelType);
                        String baseUrl = modelBaseUrlMap.get(modelType);
                        log.info("Model {} round {} calling API: {}", modelType.getName(), round, baseUrl);

                        ChatResponse response = chatClient.prompt(new Prompt(new UserMessage(prompt)))
                                .call()
                                .chatResponse();

                        String responseContent = response.getResult().getOutput().getText();

                        DebateMessage message = DebateMessage.builder()
                                .role(modelType.getName())
                                .content(responseContent.trim())
                                .round(round)
                                .modelCode(modelType.getCode())
                                .modelName(modelType.getName())
                                .timestamp(System.currentTimeMillis())
                                .build();

                        allMessages.add(message);
                        broadcastMessage(WebSocketDebateMessage.modelResponse(message, maxRounds));

                        log.info("Model {} round {} complete: {}", modelType.getName(), round,responseContent.length() > 100 ? responseContent.substring(0, 100) + "..." : responseContent);

                    } catch (Exception e) {
                        log.error("Model {} round {} failed: {}", modelType.getName(), round, e.getMessage());
                        DebateMessage errorMessage = DebateMessage.builder()
                                .role(modelType.getName())
                                .content("Call failed: " + e.getMessage())
                                .round(round)
                                .modelCode(modelType.getCode())
                                .modelName(modelType.getName())
                                .timestamp(System.currentTimeMillis())
                                .build();
                        allMessages.add(errorMessage);
                        broadcastMessage(WebSocketDebateMessage.modelResponse(errorMessage, maxRounds));
                    }
                }

                log.info("Round {} ends", round);
                broadcastMessage(WebSocketDebateMessage.roundEnd(round, maxRounds));
            }

            Map<String, String> report = generateReport(scenario, allMessages, maxRounds);

            long duration = System.currentTimeMillis() - startTime;

            broadcastMessage(WebSocketDebateMessage.complete(report, maxRounds));

            return DebateResponse.builder()
                    .success(true)
                    .message("Debate completed")
                    .scenario(scenario)
                    .totalRounds(maxRounds)
                    .messages(allMessages)
                    .durationMs(duration)
                    .report(report)
                    .build();

        } catch (Exception e) {
            log.error("Debate exception: {}", e.getMessage(), e);
            broadcastMessage(WebSocketDebateMessage.error("Debate failed: " + e.getMessage()));
            return DebateResponse.builder()
                    .success(false)
                    .message("Debate failed: " + e.getMessage())
                    .scenario(scenario)
                    .messages(allMessages)
                    .durationMs(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    private void broadcastMessage(WebSocketDebateMessage message) {
        if (webSocketHandler != null && webSocketHandler.hasActiveSessions()) {
            webSocketHandler.broadcastMessage(message);
        }
    }

    private List<ModelType> getEnabledModels(DebateRequest request) {
        List<ModelType> models = new ArrayList<>();
        if (Boolean.TRUE.equals(request.getKimiEnabled())) {
            models.add(ModelType.KIMI);
        }
        if (Boolean.TRUE.equals(request.getDoubaoEnabled())) {
            models.add(ModelType.DOUBAO);
        }
        if (Boolean.TRUE.equals(request.getQianwenEnabled())) {
            models.add(ModelType.QIANWEN);
        }
        if (Boolean.TRUE.equals(request.getZhipuEnabled())) {
            models.add(ModelType.ZHIPU);
        }
        return models;
    }

    private String buildDebatePrompt(String scenario, List<DebateMessage> history, int currentRound, ModelType currentModel) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a professional AI debate assistant named ").append(currentModel.getName()).append(".\n");
        prompt.append("Debate topic: ").append(scenario).append("\n");
        prompt.append("Current round: ").append(currentRound).append("\n");
        prompt.append("Participating AIs: ");
        for (ModelType model : ModelType.values()) {
            prompt.append(model.getName()).append(", ");
        }
        prompt.deleteCharAt(prompt.length() - 2);
        prompt.append(".\n\n");

        prompt.append("Debate rules:\n");
        prompt.append("1. Objectively refute or supplement based on previous AI statements, do not repeat expressed opinions;\n");
        prompt.append("2. Clear logic, sufficient evidence, maintain neutral and objective attitude;\n");
        prompt.append("3. If there are unreasonable points in previous statements, politely point them out and give your insights;\n");
        prompt.append("4. If you agree with a point, you can express support and add new evidence;\n");
        prompt.append("5. Keep statements concise and powerful, avoid verbosity, control within 500 words;\n");
        prompt.append("6. Do not directly quote or repeat other AI's original words, express in your own words.\n\n");

        if (!history.isEmpty()) {
            prompt.append("[History records]\n");
            for (DebateMessage msg : history) {
                prompt.append("Round").append(msg.getRound()).append(" - ").append(msg.getRole()).append(": ")
                        .append(msg.getContent()).append("\n");
            }
            prompt.append("\n");
        }

        prompt.append("Now please (").append(currentModel.getName()).append(") express your opinion:");

        return prompt.toString();
    }

    private Map<String, String> generateReport(String scenario, List<DebateMessage> messages, int totalRounds) {
        Map<String, String> report = new LinkedHashMap<>();

        report.put("summary", generateSummary(scenario, messages));
        report.put("conclusion", generateConclusion(messages));
        report.put("totalRounds", String.valueOf(totalRounds));
        report.put("totalMessages", String.valueOf(messages.size()));
        report.put("generatedAt", LocalDateTime.now().format(FORMATTER));

        return report;
    }

    private String generateSummary(String scenario, List<DebateMessage> messages) {
        StringBuilder summary = new StringBuilder();
        summary.append("This debate focuses on '").append(scenario).append("'.\n\n");

        Map<String, List<String>> modelOpinions = new LinkedHashMap<>();
        for (ModelType model : ModelType.values()) {
            modelOpinions.put(model.getName(), new ArrayList<>());
        }

        for (DebateMessage msg : messages) {
            List<String> opinions = modelOpinions.get(msg.getRole());
            if (opinions != null && !msg.getContent().contains("Call failed")) {
                opinions.add(msg.getContent());
            }
        }

        for (Map.Entry<String, List<String>> entry : modelOpinions.entrySet()) {
            List<String> opinions = entry.getValue();
            if (!opinions.isEmpty()) {
                summary.append(entry.getKey()).append(" main points:\n");
                for (int i = 0; i < opinions.size(); i++) {
                    String opinion = opinions.get(i);
                    summary.append((i + 1)).append(". ").append(opinion.length() > 150 ? opinion.substring(0, 150) + "..." : opinion).append("\n");
                }
                summary.append("\n");
            }
        }

        return summary.toString();
    }

    private String generateConclusion(List<DebateMessage> messages) {
        StringBuilder conclusion = new StringBuilder();
        conclusion.append("Based on all viewpoints, the following conclusions are drawn:\n\n");

        Map<String, Integer> modelParticipation = new LinkedHashMap<>();
        for (ModelType model : ModelType.values()) {
            modelParticipation.put(model.getName(), 0);
        }

        for (DebateMessage msg : messages) {
            if (!msg.getContent().contains("Call failed")) {
                modelParticipation.merge(msg.getRole(), 1, Integer::sum);
            }
        }

        conclusion.append("[Participation]\n");
        for (Map.Entry<String, Integer> entry : modelParticipation.entrySet()) {
            conclusion.append(entry.getKey()).append(" participated ").append(entry.getValue()).append(" rounds;\n");
        }

        conclusion.append("\n[Summary]\n");
        conclusion.append("After multiple rounds of intense debate, viewpoints from all parties collided and supplemented each other, forming a comprehensive analytical perspective.");

        return conclusion.toString();
    }
}