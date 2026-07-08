package com.community.userservice.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.community.userservice.dto.RegisterRequest;
import com.community.userservice.entity.User;
import com.community.userservice.service.UserService;
import com.community.userservice.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, RedisTemplate<String, Object> redisTemplate, JwtUtil jwtUtil) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("收到用户注册请求: username={}, email={}, password=******", 
                    request.getUsername(), request.getEmail());
        try {
            User registeredUser = userService.register(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("user", registeredUser);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(String ss) {
        System.out.println("我执行了");
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            logger.info("收到用户登录请求: username={}, password=******", username);

            String token = userService.login(username, password);
            
            String redisKey = "token:" + username;
            redisTemplate.opsForValue().set(redisKey, token, 86400, TimeUnit.SECONDS);
            logger.info("Token已存入Redis: key={}, value={}", redisKey, token);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userService.findAll();
        
        
        
        List<Map<String,Object>> list_map=new ArrayList<Map<String,Object>>();
        
        for(User user:users) {
        	
        	
			/*
			 * Date date=new Date();
			 * 
			 * SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
			 * ss=sdf.format(date);
			 */
        	
           LocalDateTime localDateTime=user.getAddtime();
           
           
           DateTimeFormatter sdf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           
           
           
           
           
           String ss=localDateTime.format(sdf);
           
           
           Map<String,Object> map=new HashMap<String, Object>();
           
           
           map.put("email", user.getEmail());
           map.put("password", user.getPassword());
           map.put("rowguid", user.getRowguid());
           map.put("username", user.getUsername());
           map.put("addtime", ss);
           
           list_map.add(map);
        	
        	
        	
        	
        	
        }
        
        
        
        
        return ResponseEntity.ok(list_map);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "未提供有效Token");
                return ResponseEntity.ok(response);
            }
            
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            String redisKey = "token:" + username;
            String storedToken = (String) redisTemplate.opsForValue().get(redisKey);
            
            if (storedToken != null && storedToken.equals(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "Token已过期或不存在");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Token验证失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Token格式无效");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                String redisKey = "token:" + username;
                redisTemplate.delete(redisKey);
                logger.info("用户退出登录: username={}", username);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "退出登录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("退出登录失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "退出登录失败");
            return ResponseEntity.ok(response);
        }
    }
}