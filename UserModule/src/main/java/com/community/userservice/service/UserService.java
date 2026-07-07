package com.community.userservice.service;

import com.community.userservice.dto.RegisterRequest;
import com.community.userservice.entity.User;

public interface UserService {

    User register(RegisterRequest request);

    String login(String username, String password);
}