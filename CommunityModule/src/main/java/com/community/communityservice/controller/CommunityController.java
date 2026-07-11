package com.community.communityservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.communityservice.entity.Community;
import com.community.communityservice.service.CommunityService;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityController.class.getName());

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/communities")
    public ResponseEntity<Map<String, Object>> getCommunities() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Community> communities = communityService.findAll();
            List<Map<String, Object>> listMap = new ArrayList<>();
            
            for (Community community : communities) {
                Map<String, Object> map = new HashMap<>();
                map.put("communityId", community.getCommunityId());
                map.put("bId", community.getbId());
                map.put("name", community.getName());
                map.put("address", community.getAddress());
                map.put("nearbyLandmarks", community.getNearbyLandmarks());
                map.put("cityCode", community.getCityCode());
                map.put("mapX", community.getMapX());
                map.put("mapY", community.getMapY());
                map.put("createTime", community.getCreateTime() != null ? community.getCreateTime().toString() : "-");
                map.put("statusCd", community.getStatusCd());
                map.put("state", community.getState());
                map.put("communityArea", community.getCommunityArea());
                map.put("tel", community.getTel());
                map.put("payFeeMonth", community.getPayFeeMonth());
                map.put("feePrice", community.getFeePrice());
                map.put("qrCode", community.getQrCode());
                listMap.add(map);
            }
            
            response.put("success", true);
            response.put("data", listMap);
        } catch (Exception e) {
            logger.error("获取小区列表失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/communities/search")
    public ResponseEntity<Map<String, Object>> searchCommunities(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Community> communities = communityService.findAll();
            List<Map<String, Object>> listMap = new ArrayList<>();
            
            for (Community community : communities) {
                boolean match = false;
                if (community.getName() != null && community.getName().contains(keyword)) {
                    match = true;
                }
                if (!match && community.getAddress() != null && community.getAddress().contains(keyword)) {
                    match = true;
                }
                
                if (match) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("communityId", community.getCommunityId());
                    map.put("bId", community.getbId());
                    map.put("name", community.getName());
                    map.put("address", community.getAddress());
                    map.put("nearbyLandmarks", community.getNearbyLandmarks());
                    map.put("cityCode", community.getCityCode());
                    map.put("mapX", community.getMapX());
                    map.put("mapY", community.getMapY());
                    map.put("createTime", community.getCreateTime() != null ? community.getCreateTime().toString() : "-");
                    map.put("statusCd", community.getStatusCd());
                    map.put("state", community.getState());
                    map.put("communityArea", community.getCommunityArea());
                    map.put("tel", community.getTel());
                    map.put("payFeeMonth", community.getPayFeeMonth());
                    map.put("feePrice", community.getFeePrice());
                    map.put("qrCode", community.getQrCode());
                    listMap.add(map);
                }
            }
            
            response.put("success", true);
            response.put("data", listMap);
        } catch (Exception e) {
            logger.error("搜索小区失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/communities/{id}")
    public ResponseEntity<Map<String, Object>> getCommunityById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Community community = communityService.findById(id);
            if (community != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("communityId", community.getCommunityId());
                map.put("bId", community.getbId());
                map.put("name", community.getName());
                map.put("address", community.getAddress());
                map.put("nearbyLandmarks", community.getNearbyLandmarks());
                map.put("cityCode", community.getCityCode());
                map.put("mapX", community.getMapX());
                map.put("mapY", community.getMapY());
                map.put("createTime", community.getCreateTime() != null ? community.getCreateTime().toString() : "-");
                map.put("statusCd", community.getStatusCd());
                map.put("state", community.getState());
                map.put("communityArea", community.getCommunityArea());
                map.put("tel", community.getTel());
                map.put("payFeeMonth", community.getPayFeeMonth());
                map.put("feePrice", community.getFeePrice());
                map.put("qrCode", community.getQrCode());
                response.put("success", true);
                response.put("data", map);
            } else {
                response.put("success", false);
                response.put("message", "小区不存在");
            }
        } catch (Exception e) {
            logger.error("获取小区详情失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/communities")
    public ResponseEntity<Map<String, Object>> createCommunity(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Community community = new Community();
            community.setName((String) request.get("name"));
            community.setAddress((String) request.get("address"));
            community.setNearbyLandmarks((String) request.get("nearbyLandmarks"));
            community.setCityCode((String) request.get("cityCode"));
            community.setMapX((String) request.get("mapX"));
            community.setMapY((String) request.get("mapY"));
            community.setStatusCd((String) request.get("statusCd"));
            community.setState((String) request.get("state"));
            
            if (request.get("communityArea") != null) {
                community.setCommunityArea(new java.math.BigDecimal(request.get("communityArea").toString()));
            }
            community.setTel((String) request.get("tel"));
            
            if (request.get("payFeeMonth") != null) {
                community.setPayFeeMonth(Long.parseLong(request.get("payFeeMonth").toString()));
            }
            if (request.get("feePrice") != null) {
                community.setFeePrice(new java.math.BigDecimal(request.get("feePrice").toString()));
            }
            community.setQrCode((String) request.get("qrCode"));
            
            communityService.create(community);
            
            response.put("success", true);
            response.put("message", "小区创建成功");
            response.put("communityId", community.getCommunityId());
        } catch (Exception e) {
            logger.error("创建小区失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/communities/{id}")
    public ResponseEntity<Map<String, Object>> updateCommunity(@PathVariable String id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Community community = new Community();
            community.setName((String) request.get("name"));
            community.setAddress((String) request.get("address"));
            community.setNearbyLandmarks((String) request.get("nearbyLandmarks"));
            community.setCityCode((String) request.get("cityCode"));
            community.setMapX((String) request.get("mapX"));
            community.setMapY((String) request.get("mapY"));
            community.setStatusCd((String) request.get("statusCd"));
            community.setState((String) request.get("state"));
            
            if (request.get("communityArea") != null) {
                community.setCommunityArea(new java.math.BigDecimal(request.get("communityArea").toString()));
            }
            community.setTel((String) request.get("tel"));
            
            if (request.get("payFeeMonth") != null) {
                community.setPayFeeMonth(Long.parseLong(request.get("payFeeMonth").toString()));
            }
            if (request.get("feePrice") != null) {
                community.setFeePrice(new java.math.BigDecimal(request.get("feePrice").toString()));
            }
            community.setQrCode((String) request.get("qrCode"));
            
            Community updated = communityService.update(id, community);
            
            if (updated != null) {
                response.put("success", true);
                response.put("message", "小区更新成功");
            } else {
                response.put("success", false);
                response.put("message", "小区不存在");
            }
        } catch (Exception e) {
            logger.error("更新小区失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/communities/{id}")
    public ResponseEntity<Map<String, Object>> deleteCommunity(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            communityService.delete(id);
            response.put("success", true);
            response.put("message", "小区删除成功");
        } catch (Exception e) {
            logger.error("删除小区失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}