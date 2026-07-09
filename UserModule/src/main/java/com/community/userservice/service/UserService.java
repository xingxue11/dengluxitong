package com.community.userservice.service;

import com.community.userservice.dto.RegisterRequest;
import com.community.userservice.entity.User;

import java.util.List;

public interface UserService {

    User register(RegisterRequest request);

    String login(String username, String password);

    List<User> findAll();

    User findById(String id);

    User update(String id, String username, String password, String email);

    void delete(String id);
}