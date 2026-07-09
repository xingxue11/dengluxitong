package com.community.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.userservice.entity.Menu;
import com.community.userservice.entity.Role;
import com.community.userservice.mapper.MenuMapper;
import com.community.userservice.service.MenuService;
import com.community.userservice.service.RoleMenuPermService;
import com.community.userservice.service.UserRolePermService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final UserRolePermService userRolePermService;
    private final RoleMenuPermService roleMenuPermService;

    public MenuServiceImpl(MenuMapper menuMapper, UserRolePermService userRolePermService, 
                          RoleMenuPermService roleMenuPermService) {
        this.menuMapper = menuMapper;
        this.userRolePermService = userRolePermService;
        this.roleMenuPermService = roleMenuPermService;
    }

    @Override
    public List<Menu> findAll() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getOrdernum);
        return menuMapper.selectList(queryWrapper);
    }

    @Override
    public List<Menu> findTree() {
        List<Menu> allMenus = findAll();
        return buildTree(allMenus);
    }

    private List<Menu> buildTree(List<Menu> menus) {
        List<Menu> rootMenus = menus.stream()
                .filter(m -> "1".equals(m.getIsroot()) || m.getParentguid() == null || m.getParentguid().isEmpty())
                .collect(Collectors.toList());

        for (Menu root : rootMenus) {
            root.setChildren(findChildrenRecursive(root.getRowguid(), menus));
        }

        return rootMenus;
    }

    private List<Menu> findChildrenRecursive(String parentguid, List<Menu> menus) {
        List<Menu> children = menus.stream()
                .filter(m -> parentguid.equals(m.getParentguid()))
                .collect(Collectors.toList());

        for (Menu child : children) {
            child.setChildren(findChildrenRecursive(child.getRowguid(), menus));
        }

        return children;
    }

    @Override
    public Menu findById(String id) {
        return menuMapper.selectById(id);
    }

    @Override
    public Menu create(Menu menu) {
        menu.setRowguid(UUID.randomUUID().toString().replace("-", ""));
        menu.setAddtime(LocalDateTime.now());
        if (menu.getOrdernum() == null) {
            menu.setOrdernum(0);
        }
        if (menu.getIsroot() == null) {
            menu.setIsroot("0");
        }
        menuMapper.insert(menu);
        return menu;
    }

    @Override
    public Menu update(String id, Menu menu) {
        Menu existingMenu = menuMapper.selectById(id);
        if (existingMenu != null) {
            if (menu.getMenuname() != null) {
                existingMenu.setMenuname(menu.getMenuname());
            }
            if (menu.getMenuicon() != null) {
                existingMenu.setMenuicon(menu.getMenuicon());
            }
            if (menu.getMenupage() != null) {
                existingMenu.setMenupage(menu.getMenupage());
            }
            if (menu.getIsroot() != null) {
                existingMenu.setIsroot(menu.getIsroot());
            }
            if (menu.getParentguid() != null) {
                existingMenu.setParentguid(menu.getParentguid());
            }
            if (menu.getOrdernum() != null) {
                existingMenu.setOrdernum(menu.getOrdernum());
            }
            menuMapper.updateById(existingMenu);
            return existingMenu;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        deleteMenuAndChildren(id);
    }

    private void deleteMenuAndChildren(String id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentguid, id);
        List<Menu> children = menuMapper.selectList(queryWrapper);

        for (Menu child : children) {
            deleteMenuAndChildren(child.getRowguid());
        }

        menuMapper.deleteById(id);
    }

    @Override
    public List<Menu> findChildren(String parentguid) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentguid, parentguid);
        queryWrapper.orderByAsc(Menu::getOrdernum);
        return menuMapper.selectList(queryWrapper);
    }

    @Override
    public List<Menu> findMenusByUserGuid(String userguid) {
        List<Role> roles = userRolePermService.getRolesByUserGuid(userguid);
        
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> menuGuids = new HashSet<>();
        for (Role role : roles) {
            List<String> roleMenuGuids = roleMenuPermService.getMenuGuidsByRole(role.getRowguid());
            if (roleMenuGuids != null) {
                menuGuids.addAll(roleMenuGuids);
            }
        }

        if (menuGuids.isEmpty()) {
            return new ArrayList<>();
        }

        List<Menu> allMenus = findAll();
        List<Menu> userMenus = allMenus.stream()
                .filter(m -> menuGuids.contains(m.getRowguid()))
                .collect(Collectors.toList());

        return buildTree(userMenus);
    }
}