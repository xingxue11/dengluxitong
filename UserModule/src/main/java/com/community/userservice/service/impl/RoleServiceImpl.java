package com.community.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.userservice.entity.Role;
import com.community.userservice.mapper.RoleMapper;
import com.community.userservice.service.RoleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public List<Role> findAll() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Role::getAddtime);
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Role findById(String id) {
        return roleMapper.selectById(id);
    }

    @Override
    public Role create(Role role) {
        role.setRowguid(UUID.randomUUID().toString().replace("-", ""));
        role.setAddtime(LocalDateTime.now());
        roleMapper.insert(role);
        return role;
    }

    @Override
    public Role update(String id, Role role) {
        Role existingRole = roleMapper.selectById(id);
        if (existingRole != null) {
            existingRole.setRolename(role.getRolename());
            roleMapper.updateById(existingRole);
            return existingRole;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        roleMapper.deleteById(id);
    }
}