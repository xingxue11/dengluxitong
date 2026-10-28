package com.community.communityservice.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ConcreteImplementor：邮件发送
 */
@Component
public class EmailSender implements MessageSender {
    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    @Override
    public String send(String content) {
        log.info("[邮件发送] {}", content);
        return "邮件发送成功 → " + content;
    }

    @Override
    public String getChannelName() {
        return "邮件";
    }
}
