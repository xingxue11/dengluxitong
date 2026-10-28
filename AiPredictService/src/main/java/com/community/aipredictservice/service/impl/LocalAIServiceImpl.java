package com.community.aipredictservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.community.aipredictservice.dto.ChatRequest;
import com.community.aipredictservice.dto.ChatResponse;
import com.community.aipredictservice.enums.ModelType;
import com.community.aipredictservice.service.AgentService;
import com.community.aipredictservice.service.LocalAIService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocalAIServiceImpl implements LocalAIService {

    private final Map<ModelType, ChatClient> chatClientMap;
    private final AgentService agentService;

    public LocalAIServiceImpl(Map<ModelType, ChatClient> chatClientMap, AgentService agentService) {
        this.chatClientMap = chatClientMap;
        this.agentService = agentService;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            String message = request.getMessage();
            
            if (isWeatherQuery(message)) {
                log.info("Detected weather query, using AgentService");
                String response = agentService.chat(message);
                long duration = System.currentTimeMillis() - startTime;
                return ChatResponse.success(response, "本地模型(工具调用)", duration);
            }

            ModelType modelType = getModelType(request.getModel());
            ChatClient chatClient = chatClientMap.get(modelType);
            
            if (chatClient == null) {
                log.error("Model {} is not available", modelType);
                return ChatResponse.error("模型不可用: " + modelType.getName());
            }

            List<org.springframework.ai.chat.messages.Message> messages = new ArrayList<>();
            messages.add(new SystemMessage("你是一个友好的AI助手，请用简洁明了的方式回答问题。"));

            if (request.getHistory() != null) {
                for (ChatRequest.Message msg : request.getHistory()) {
                    if ("user".equals(msg.getRole())) {
                        messages.add(new UserMessage(msg.getContent()));
                    } else if ("assistant".equals(msg.getRole())) {
                        messages.add(new org.springframework.ai.chat.messages.AssistantMessage(msg.getContent()));
                    }
                }
            }

            messages.add(new UserMessage(request.getMessage()));

            Prompt prompt = new Prompt(messages);
            org.springframework.ai.chat.model.ChatResponse response = chatClient.prompt(prompt)
                    .call()
                    .chatResponse();

            String content = response.getResult().getOutput().getText();
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("Local AI chat completed, model: {}, duration: {}ms", modelType.getName(), duration);
            
            return ChatResponse.success(content, modelType.getName(), duration);
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Local AI chat failed, duration: {}ms, error: {}", duration, e.getMessage(), e);
            return ChatResponse.error("对话失败: " + e.getMessage());
        }
    }

    private boolean isWeatherQuery(String message) {
        if (message == null) {
            return false;
        }
        String[] weatherKeywords = {"天气", "气温", "温度", "晴", "雨", "雪", "风", "阴"};
        for (String keyword : weatherKeywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private ModelType getModelType(String modelCode) {
        if (modelCode == null) {
            return ModelType.LOCAL;
        }
        try {
            return ModelType.valueOf(modelCode.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ModelType.LOCAL;
        }
    }
}