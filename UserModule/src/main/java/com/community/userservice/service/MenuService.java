package com.community.userservice.service;

import com.community.userservice.entity.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> findAll();

    List<Menu> findTree();

    Menu findById(String id);

    Menu create(Menu menu);

    Menu update(String id, Menu menu);

    void delete(String id);

    List<Menu> findChildren(String parentguid);

    List<Menu> findMenusByUserGuid(String userguid);
}