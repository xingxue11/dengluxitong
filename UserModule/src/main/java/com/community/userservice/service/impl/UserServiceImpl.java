package com.community.userservice.service.impl;

import com.community.userservice.dto.RegisterRequest;
import com.community.userservice.entity.User;
import com.community.userservice.mapper.UserMapper;
import com.community.userservice.service.UserService;
import com.community.userservice.util.JwtUtil;
import com.community.userservice.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil,
                           AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User register(RegisterRequest request) {
        if (userMapper.countByUsername(request.getUsername()) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        if (userMapper.countByEmail(request.getEmail()) > 0) {
            throw new RuntimeException("邮箱已被注册");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        User user = new User();
        user.setRowguid(UUID.randomUUID().toString().replace("-", ""));
        user.setUsername(request.getUsername());
        user.setPassword(MD5Util.encrypt(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAddtime(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            if (authentication.isAuthenticated()) {
                return jwtUtil.generateToken(username);
            }
        } catch (AuthenticationException e) {
        	
        	e.printStackTrace();
            throw new RuntimeException("用户名或密码错误");
        }

        throw new RuntimeException("登录失败");
    }
}