package com.community.communityservice.service;

import com.community.communityservice.entity.Community;

import java.util.List;

public interface CommunityService {

    List<Community> findAll();

    Community findById(String id);

    Community create(Community community);

    Community update(String id, Community community);

    void delete(String id);

    List<Community> findByStatus(String statusCd);
}