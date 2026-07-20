package com.community.aipredictservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.community.aipredictservice.websocket.DebateWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    private final DebateWebSocketHandler debateWebSocketHandler;

    public WebSocketConfig(DebateWebSocketHandler debateWebSocketHandler) {
        this.debateWebSocketHandler = debateWebSocketHandler;
        log.info("========================================");
        log.info("WebSocketConfig initialized");
        log.info("DebateWebSocketHandler injected: {}", debateWebSocketHandler != null ? "OK" : "NULL");
        log.info("========================================");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("========================================");
        log.info("Registering WebSocket handlers...");
        log.info("WebSocket endpoint: /ws/debate");
        log.info("Allowed origins: *");
        log.info("========================================");
        
        registry.addHandler(debateWebSocketHandler, "/ws/debate")
                .setAllowedOriginPatterns("*");
        
        log.info("WebSocket handler registration completed successfully");
    }
}