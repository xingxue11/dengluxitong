package com.community.communityservice.bridge;

/**
 * Implementor：发送渠道接口
 */
public interface MessageSender {
    void send(String messageType, String content);
}
