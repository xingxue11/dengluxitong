package com.community.communityservice.bridge;

import org.springframework.stereotype.Component;

/**
 * 钉钉发送渠道
 */
@Component
public class DingTalkSender implements MessageSender {
    @Override
    public void send(String messageType, String content) {
        System.out.println("[钉钉渠道] 发送" + messageType + "：" + content);
    }
}
