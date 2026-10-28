package com.community.communityservice.bridge;

import org.springframework.stereotype.Component;

/**
 * 短信发送渠道
 */
@Component
public class SmsSender implements MessageSender {
    @Override
    public void send(String messageType, String content) {
        System.out.println("[短信渠道] 发送" + messageType + "：" + content);
    }
}
