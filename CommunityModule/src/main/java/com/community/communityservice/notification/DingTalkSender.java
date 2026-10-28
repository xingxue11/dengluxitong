package com.community.communityservice.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ConcreteImplementor：钉钉发送
 */
@Component
public class DingTalkSender implements MessageSender {
    private static final Logger log = LoggerFactory.getLogger(DingTalkSender.class);

    @Override
    public String send(String content) {
        log.info("[钉钉发送] {}", content);
        return "钉钉发送成功 → " + content;
    }

    @Override
    public String getChannelName() {
        return "钉钉";
    }
}
