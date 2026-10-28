package com.community.communityservice.notification;

/**
 * Implementor：发送渠道接口（实现部分）
 */
public interface MessageSender {
    String send(String content);
    String getChannelName();
}
