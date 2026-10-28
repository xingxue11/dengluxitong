package com.community.communityservice.bridge;

/**
 * 图片消息
 */
public class ImageMessage extends NotificationMessage {
    private String imageUrl;
    private String caption;

    public ImageMessage(MessageSender sender, String imageUrl, String caption) {
        super(sender);
        this.imageUrl = imageUrl;
        this.caption = caption;
    }

    @Override
    public void send() {
        sender.send("图片消息", "[图片:" + imageUrl + "] " + caption);
    }
}
