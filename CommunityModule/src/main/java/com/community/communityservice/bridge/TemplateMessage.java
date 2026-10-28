package com.community.communityservice.bridge;

import java.util.Map;

/**
 * 模板消息
 */
public class TemplateMessage extends NotificationMessage {
    private String templateName;
    private Map<String, String> params;

    public TemplateMessage(MessageSender sender, String templateName, Map<String, String> params) {
        super(sender);
        this.params = params;
        this.templateName = templateName;
    }

    @Override
    public void send() {
        String content = "模板[" + templateName + "] 参数: " + params.toString();
        sender.send("模板消息", content);
    }
}
