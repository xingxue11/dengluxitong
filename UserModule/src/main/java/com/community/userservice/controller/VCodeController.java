package com.community.userservice.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/vcode")
public class VCodeController {

    private static final Logger logger = LoggerFactory.getLogger(VCodeController.class);

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final int EXPIRE_MINUTES = 5;

    @Autowired
    public VCodeController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public void getVCode(HttpServletResponse response) {
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String code = generateCode();
            
            String redisKey = "vcode:" + uuid;
            redisTemplate.opsForValue().set(redisKey, code.toLowerCase(), EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            BufferedImage image = generateImage(code);
            
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("X-VCode-Id", uuid);
            
            ImageIO.write(image, "png", response.getOutputStream());
            
            logger.info("Generated vcode: uuid={}, code={}", uuid, code);
            
        } catch (Exception e) {
            logger.error("Failed to generate vcode: {}", e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyVCode(@RequestBody Map<String, String> request) {
        String uuid = request.get("uuid");
        String code = request.get("code");
        
        Map<String, Object> response = new HashMap<>();
        
        if (uuid == null || uuid.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "验证码ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (code == null || code.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "验证码不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        
        String redisKey = "vcode:" + uuid;
        String storedCode = (String) redisTemplate.opsForValue().get(redisKey);
        
        if (storedCode == null) {
            response.put("success", false);
            response.put("message", "验证码已过期，请刷新");
            return ResponseEntity.ok(response);
        }
        
        if (storedCode.equals(code.toLowerCase())) {
            redisTemplate.delete(redisKey);
            response.put("success", true);
            response.put("message", "验证码验证成功");
            logger.info("VCode verified: uuid={}", uuid);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "验证码错误");
            logger.info("VCode verify failed: uuid={}, input={}, stored={}", uuid, code, storedCode);
            return ResponseEntity.ok(response);
        }
    }

    private String generateCode() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return code.toString();
    }

    private BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        
        Random random = new Random();
        
        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawOval(x, y, 1, 1);
        }
        
        for (int i = 0; i < 4; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            g.drawLine(x1, y1, x2, y2);
        }
        
        Font font = new Font("Arial", Font.BOLD, 28);
        g.setFont(font);
        
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            int x = 15 + i * 25;
            int y = 28 + random.nextInt(8) - 4;
            
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(180)));
            
            g.drawString(String.valueOf(c), x, y);
        }
        
        g.dispose();
        return image;
    }
}