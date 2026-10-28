package com.community.communityservice.bridge;

import org.springframework.stereotype.Component;

/**
 * 邮件发送渠道
 */
@Component
public class EmailSender implements MessageSender {
    @Override
    public void send(String messageType, String content) {
        System.out.println("[邮件渠道] 发送" + messageType + "：" + content);
    }
}
