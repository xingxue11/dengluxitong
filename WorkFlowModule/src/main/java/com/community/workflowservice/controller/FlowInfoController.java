package com.community.workflowservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.community.workflowservice.entity.FlowInfo;
import com.community.workflowservice.service.FlowInfoService;

@RestController
@RequestMapping("/api/community/flows")
public class FlowInfoController {

    private static final Logger logger = LoggerFactory.getLogger(FlowInfoController.class.getName());

    private final FlowInfoService flowInfoService;

    @Autowired
    public FlowInfoController(FlowInfoService flowInfoService) {
        this.flowInfoService = flowInfoService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFlowInfos() {
        logger.info("获取所有流程信息");
        Map<String, Object> response = new HashMap<>();
        try {
            List<FlowInfo> flowInfos = flowInfoService.getAllFlowInfos();
            response.put("success", true);
            response.put("data", flowInfos);
            response.put("message", "获取成功");
        } catch (Exception e) {
            logger.error("获取流程信息失败", e);
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFlowInfoById(@PathVariable String id) {
        logger.info("获取流程信息, id: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            FlowInfo flowInfo = flowInfoService.getFlowInfoById(id);
            if (flowInfo != null) {
                response.put("success", true);
                response.put("data", flowInfo);
                response.put("message", "获取成功");
            } else {
                response.put("success", false);
                response.put("message", "流程不存在");
            }
        } catch (Exception e) {
            logger.error("获取流程信息失败", e);
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFlowInfos(@RequestParam(required = false) String keyword) {
        logger.info("搜索流程信息, keyword: {}", keyword);
        Map<String, Object> response = new HashMap<>();
        try {
            List<FlowInfo> flowInfos = flowInfoService.searchFlowInfos(keyword);
            response.put("success", true);
            response.put("data", flowInfos);
            response.put("message", "获取成功");
        } catch (Exception e) {
            logger.error("搜索流程信息失败", e);
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createFlowInfo(@RequestBody FlowInfo flowInfo) {
        logger.info("创建流程信息, flowname: {}", flowInfo.getFlowname());
        Map<String, Object> response = new HashMap<>();
        try {
            flowInfo.setRowguid(UUID.randomUUID().toString());
            flowInfo.setAddtime(new Date());
            boolean result = flowInfoService.createFlowInfo(flowInfo);
            if (result) {
                response.put("success", true);
                response.put("data", flowInfo);
                response.put("message", "创建成功");
            } else {
                response.put("success", false);
                response.put("message", "创建失败");
            }
        } catch (Exception e) {
            logger.error("创建流程信息失败", e);
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFlowInfo(@PathVariable String id, @RequestBody FlowInfo flowInfo) {
        logger.info("更新流程信息, id: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            FlowInfo existing = flowInfoService.getFlowInfoById(id);
            if (existing == null) {
                response.put("success", false);
                response.put("message", "流程不存在");
                return ResponseEntity.ok(response);
            }

            flowInfo.setRowguid(id);
            flowInfo.setAddtime(existing.getAddtime());
            boolean result = flowInfoService.updateFlowInfo(flowInfo);
            if (result) {
                response.put("success", true);
                response.put("data", flowInfo);
                response.put("message", "更新成功");
            } else {
                response.put("success", false);
                response.put("message", "更新失败");
            }
        } catch (Exception e) {
            logger.error("更新流程信息失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFlowInfo(@PathVariable String id) {
        logger.info("删除流程信息, id: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            FlowInfo existing = flowInfoService.getFlowInfoById(id);
            if (existing == null) {
                response.put("success", false);
                response.put("message", "流程不存在");
                return ResponseEntity.ok(response);
            }

            boolean result = flowInfoService.deleteFlowInfo(id);
            if (result) {
                response.put("success", true);
                response.put("message", "删除成功");
            } else {
                response.put("success", false);
                response.put("message", "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除流程信息失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}