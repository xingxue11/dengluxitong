package com.community.userservice.service;

import com.community.userservice.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findById(String id);

    Role create(Role role);

    Role update(String id, Role role);

    void delete(String id);
}