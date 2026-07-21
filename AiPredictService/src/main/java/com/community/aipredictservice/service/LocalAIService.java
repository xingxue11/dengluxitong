package com.community.aipredictservice.service;

import com.community.aipredictservice.dto.ChatRequest;
import com.community.aipredictservice.dto.ChatResponse;

public interface LocalAIService {

    ChatResponse chat(ChatRequest request);
}