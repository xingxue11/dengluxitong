package com.community.communityservice.controller;

import com.community.communityservice.bridge.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息通知系统控制器（桥接模式）
 */
@RestController
@RequestMapping("/api/notify")
public class NotifyController {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private DingTalkSender dingTalkSender;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private WeChatSender weChatSender;

    private MessageSender getSender(String channel) {
        return switch (channel) {
            case "email" -> emailSender;
            case "dingtalk" -> dingTalkSender;
            case "sms" -> smsSender;
            case "wechat" -> weChatSender;
            default -> emailSender;
        };
    }

    /**
     * 发送消息
     * @param type text/image/template
     * @param channel email/dingtalk/sms/wechat
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(
            @RequestParam String type,
            @RequestParam String channel,
            @RequestBody Map<String, String> body) {

        MessageSender sender = getSender(channel);
        NotificationMessage message;

        switch (type) {
            case "text" -> message = new TextMessage(sender, body.get("content"));
            case "image" -> message = new ImageMessage(sender, body.get("imageUrl"), body.get("caption"));
            case "template" -> message = new TemplateMessage(sender, body.get("templateName"), body);
            default -> {
                Map<String, Object> err = new HashMap<>();
                err.put("success", false);
                err.put("message", "不支持的消息类型");
                return ResponseEntity.ok(err);
            }
        }

        message.send();

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "发送成功：" + type + " -> " + channel);
        return ResponseEntity.ok(resp);
    }
}
