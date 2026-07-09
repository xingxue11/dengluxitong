package com.community.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.userservice.entity.Role;
import com.community.userservice.entity.UserRolePerm;
import com.community.userservice.mapper.UserRolePermMapper;
import com.community.userservice.service.UserRolePermService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserRolePermServiceImpl implements UserRolePermService {

    private final UserRolePermMapper userRolePermMapper;

    public UserRolePermServiceImpl(UserRolePermMapper userRolePermMapper) {
        this.userRolePermMapper = userRolePermMapper;
    }

    @Override
    public List<Role> getRolesByUserGuid(String userguid) {
        return userRolePermMapper.selectRolesByUserGuid(userguid);
    }

    @Override
    public void assignRoleToUser(String userguid, String rolelguid) {
        LambdaQueryWrapper<UserRolePerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRolePerm::getUserguid, userguid);
        queryWrapper.eq(UserRolePerm::getRolelguid, rolelguid);
        
        UserRolePerm existing = userRolePermMapper.selectOne(queryWrapper);
        if (existing == null) {
            UserRolePerm userRolePerm = new UserRolePerm();
            userRolePerm.setRowguid(UUID.randomUUID().toString().replace("-", ""));
            userRolePerm.setUserguid(userguid);
            userRolePerm.setRolelguid(rolelguid);
            userRolePermMapper.insert(userRolePerm);
        }
    }

    @Override
    public void removeRoleFromUser(String userguid, String rolelguid) {
        LambdaQueryWrapper<UserRolePerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRolePerm::getUserguid, userguid);
        queryWrapper.eq(UserRolePerm::getRolelguid, rolelguid);
        userRolePermMapper.delete(queryWrapper);
    }

    @Override
    public void clearUserRoles(String userguid) {
        LambdaQueryWrapper<UserRolePerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRolePerm::getUserguid, userguid);
        userRolePermMapper.delete(queryWrapper);
    }
}