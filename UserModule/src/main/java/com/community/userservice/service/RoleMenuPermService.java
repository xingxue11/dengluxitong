package com.community.userservice.service;

import com.community.userservice.entity.RoleMenuPerm;

import java.util.List;

public interface RoleMenuPermService {

    void assignMenus(String roleguid, List<String> menuGuids);

    List<String> getMenuGuidsByRole(String roleguid);

    void removeMenu(String roleguid, String menuguid);

    void removeAllMenus(String roleguid);
}