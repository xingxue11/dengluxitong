package com.community.aipredictservice.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DebateWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(DebateWebSocketHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public DebateWebSocketHandler() {
        log.info("========================================");
        log.info("DebateWebSocketHandler initialized");
        log.info("WebSocket handler is ready to accept connections");
        log.info("========================================");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        log.info("WebSocket connection established: {}", sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        log.info("WebSocket connection closed: {}", sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received WebSocket message from {}: {}", session.getId(), message.getPayload());
    }

    public void sendMessage(String sessionId, Object message) {
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                log.error("Failed to send message to session {}: {}", sessionId, e.getMessage());
            }
        }
    }

    public void broadcastMessage(Object message) {
        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (IOException e) {
            log.error("Failed to serialize message: {}", e.getMessage());
            return;
        }

        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession session = entry.getValue();
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(json));
                } catch (IOException e) {
                    log.error("Failed to broadcast message to session {}: {}", entry.getKey(), e.getMessage());
                }
            }
        }
    }

    public boolean hasActiveSessions() {
        return !sessions.isEmpty();
    }

    public int getSessionCount() {
        return sessions.size();
    }
}