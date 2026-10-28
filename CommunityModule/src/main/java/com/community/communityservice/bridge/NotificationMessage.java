package com.community.communityservice.bridge;

/**
 * Abstraction：消息抽象类（桥接核心）
 * 持有 MessageSender 引用，与发送渠道解耦
 */
public abstract class NotificationMessage {
    protected MessageSender sender;

    public NotificationMessage(MessageSender sender) {
        this.sender = sender;
    }

    public abstract void send();
}
