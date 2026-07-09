package com.community.userservice.service;

import com.community.userservice.entity.Role;
import com.community.userservice.entity.UserRolePerm;

import java.util.List;

public interface UserRolePermService {

    List<Role> getRolesByUserGuid(String userguid);

    void assignRoleToUser(String userguid, String rolelguid);

    void removeRoleFromUser(String userguid, String rolelguid);

    void clearUserRoles(String userguid);
}