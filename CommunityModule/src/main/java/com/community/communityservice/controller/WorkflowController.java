package com.community.communityservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.communityservice.service.WorkflowService;

@RestController
@RequestMapping("/api/community/workflow")
public class WorkflowController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class.getName());

    @Autowired
    private WorkflowService workflowService;

    @GetMapping("/{flowId}")
    public ResponseEntity<Map<String, Object>> getWorkflow(@PathVariable String flowId) {
        logger.info("获取工作流配置, flowId: {}", flowId);
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> workflow = workflowService.getWorkflowByFlowId(flowId);
            response.put("success", true);
            response.put("data", workflow);
            response.put("message", "获取成功");
        } catch (Exception e) {
            logger.error("获取工作流配置失败", e);
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{flowId}")
    public ResponseEntity<Map<String, Object>> saveWorkflow(@PathVariable String flowId, @RequestBody Map<String, Object> workflowData) {
        logger.info("保存工作流配置, flowId: {}", flowId);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = workflowService.saveWorkflow(flowId, workflowData);
            if (result) {
                response.put("success", true);
                response.put("message", "保存成功");
            } else {
                response.put("success", false);
                response.put("message", "保存失败");
            }
        } catch (Exception e) {
            logger.error("保存工作流配置失败", e);
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}