package com.community.communityservice.bridge;

import org.springframework.stereotype.Component;

/**
 * 微信发送渠道
 */
@Component
public class WeChatSender implements MessageSender {
    @Override
    public void send(String messageType, String content) {
        System.out.println("[微信渠道] 发送" + messageType + "：" + content);
    }
}
