package com.community.userservice.service.impl;

import com.community.userservice.entity.RoleMenuPerm;
import com.community.userservice.mapper.RoleMenuPermMapper;
import com.community.userservice.service.RoleMenuPermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleMenuPermServiceImpl implements RoleMenuPermService {

    private final RoleMenuPermMapper roleMenuPermMapper;

    public RoleMenuPermServiceImpl(RoleMenuPermMapper roleMenuPermMapper) {
        this.roleMenuPermMapper = roleMenuPermMapper;
    }

    @Override
    @Transactional
    public void assignMenus(String roleguid, List<String> menuGuids) {
        roleMenuPermMapper.deleteByRoleGuid(roleguid);
        
        if (menuGuids != null && !menuGuids.isEmpty()) {
            for (String menuguid : menuGuids) {
                RoleMenuPerm perm = new RoleMenuPerm();
                perm.setRowguid(UUID.randomUUID().toString().replace("-", ""));
                perm.setRoleguid(roleguid);
                perm.setMenuguid(menuguid);
                roleMenuPermMapper.insert(perm);
            }
        }
    }

    @Override
    public List<String> getMenuGuidsByRole(String roleguid) {
        return roleMenuPermMapper.findMenuGuidsByRoleGuid(roleguid);
    }

    @Override
    @Transactional
    public void removeMenu(String roleguid, String menuguid) {
        RoleMenuPerm perm = new RoleMenuPerm();
        perm.setRoleguid(roleguid);
        perm.setMenuguid(menuguid);
        roleMenuPermMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RoleMenuPerm>()
            .eq(RoleMenuPerm::getRoleguid, roleguid)
            .eq(RoleMenuPerm::getMenuguid, menuguid));
    }

    @Override
    @Transactional
    public void removeAllMenus(String roleguid) {
        roleMenuPermMapper.deleteByRoleGuid(roleguid);
    }
}