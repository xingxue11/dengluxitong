package com.community.communityservice.bridge;

/**
 * 文本消息
 */
public class TextMessage extends NotificationMessage {
    private String content;

    public TextMessage(MessageSender sender, String content) {
        super(sender);
        this.content = content;
    }

    @Override
    public void send() {
        sender.send("文本消息", content);
    }
}
