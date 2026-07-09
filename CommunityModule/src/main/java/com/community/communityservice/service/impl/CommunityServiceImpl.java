package com.community.communityservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.communityservice.entity.Community;
import com.community.communityservice.mapper.CommunityMapper;
import com.community.communityservice.service.CommunityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;

    public CommunityServiceImpl(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    @Override
    public List<Community> findAll() {
        LambdaQueryWrapper<Community> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Community::getCreateTime);
        return communityMapper.selectList(queryWrapper);
    }

    @Override
    public Community findById(String id) {
        return communityMapper.selectById(id);
    }

    @Override
    public Community create(Community community) {
        community.setCommunityId(UUID.randomUUID().toString().replace("-", "").substring(0, 30));
        community.setCreateTime(new Timestamp(System.currentTimeMillis()));
        
        if (community.getbId() == null) {
            community.setbId("-1");
        }
        if (community.getStatusCd() == null) {
            community.setStatusCd("0");
        }
        if (community.getState() == null) {
            community.setState("1000");
        }
        if (community.getCommunityArea() == null) {
            community.setCommunityArea(BigDecimal.ZERO);
        }
        if (community.getPayFeeMonth() == null) {
            community.setPayFeeMonth(12L);
        }
        if (community.getFeePrice() == null) {
            community.setFeePrice(BigDecimal.ZERO);
        }
        
        communityMapper.insert(community);
        return community;
    }

    @Override
    public Community update(String id, Community community) {
        Community existing = communityMapper.selectById(id);
        if (existing != null) {
            if (community.getName() != null) {
                existing.setName(community.getName());
            }
            if (community.getAddress() != null) {
                existing.setAddress(community.getAddress());
            }
            if (community.getNearbyLandmarks() != null) {
                existing.setNearbyLandmarks(community.getNearbyLandmarks());
            }
            if (community.getCityCode() != null) {
                existing.setCityCode(community.getCityCode());
            }
            if (community.getMapX() != null) {
                existing.setMapX(community.getMapX());
            }
            if (community.getMapY() != null) {
                existing.setMapY(community.getMapY());
            }
            if (community.getStatusCd() != null) {
                existing.setStatusCd(community.getStatusCd());
            }
            if (community.getState() != null) {
                existing.setState(community.getState());
            }
            if (community.getCommunityArea() != null) {
                existing.setCommunityArea(community.getCommunityArea());
            }
            if (community.getTel() != null) {
                existing.setTel(community.getTel());
            }
            if (community.getPayFeeMonth() != null) {
                existing.setPayFeeMonth(community.getPayFeeMonth());
            }
            if (community.getFeePrice() != null) {
                existing.setFeePrice(community.getFeePrice());
            }
            if (community.getQrCode() != null) {
                existing.setQrCode(community.getQrCode());
            }
            
            communityMapper.updateById(existing);
            return existing;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        Community community = communityMapper.selectById(id);
        if (community != null) {
            community.setStatusCd("1");
            communityMapper.updateById(community);
        }
    }

    @Override
    public List<Community> findByStatus(String statusCd) {
        LambdaQueryWrapper<Community> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Community::getStatusCd, statusCd);
        queryWrapper.orderByDesc(Community::getCreateTime);
        return communityMapper.selectList(queryWrapper);
    }
}