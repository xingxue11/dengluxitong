package com.community.communityservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.community.communityservice.entity.FlowEdge;
import com.community.communityservice.entity.FlowNode;
import com.community.communityservice.mapper.FlowEdgeMapper;
import com.community.communityservice.mapper.FlowNodeMapper;
import com.community.communityservice.service.WorkflowService;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private FlowNodeMapper flowNodeMapper;

    @Autowired
    private FlowEdgeMapper flowEdgeMapper;

    @Override
    public Map<String, Object> getWorkflowByFlowId(String flowId) {
        Map<String, Object> result = new HashMap<>();
        result.put("flowId", flowId);

        List<FlowNode> nodes = flowNodeMapper.selectByFlowGuid(flowId);
        List<Map<String, Object>> nodeList = new ArrayList<>();
        for (FlowNode node : nodes) {
            Map<String, Object> nodeMap = new HashMap<>();
            nodeMap.put("id", node.getRowguid());
            nodeMap.put("type", node.getNodetype());
            nodeMap.put("label", node.getNodelabel());
            nodeMap.put("position", Map.of("x", node.getPositionx(), "y", node.getPositiony()));
            nodeMap.put("assignee", node.getAssignee());
            nodeMap.put("roleguid", node.getRoleguid());
            nodeMap.put("conditionexpr", node.getConditionexpr());
            nodeList.add(nodeMap);
        }
        result.put("nodes", nodeList);

        List<FlowEdge> edges = flowEdgeMapper.selectByFlowGuid(flowId);
        List<Map<String, Object>> edgeList = new ArrayList<>();
        for (FlowEdge edge : edges) {
            Map<String, Object> edgeMap = new HashMap<>();
            edgeMap.put("id", edge.getRowguid());
            edgeMap.put("source", edge.getSourcenode());
            edgeMap.put("target", edge.getTargetnode());
            edgeMap.put("label", edge.getLabel());
            edgeList.add(edgeMap);
        }
        result.put("edges", edgeList);

        return result;
    }

    @Override
    @Transactional
    public boolean saveWorkflow(String flowId, Map<String, Object> workflowData) {
        flowNodeMapper.deleteByFlowGuid(flowId);
        flowEdgeMapper.deleteByFlowGuid(flowId);

        List<Map<String, Object>> nodes = (List<Map<String, Object>>) workflowData.get("nodes");
        if (nodes != null) {
            int orderNum = 1;
            for (Map<String, Object> nodeMap : nodes) {
                FlowNode node = new FlowNode();
                String nodeId = (String) nodeMap.get("id");
                node.setRowguid(nodeId.startsWith("node-") ? UUID.randomUUID().toString() : nodeId);
                node.setFlowguid(flowId);
                node.setNodetype((String) nodeMap.get("type"));
                node.setNodelabel((String) nodeMap.get("label"));

                Map<String, Object> position = (Map<String, Object>) nodeMap.get("position");
                if (position != null) {
                    node.setPositionx(((Number) position.get("x")).intValue());
                    node.setPositiony(((Number) position.get("y")).intValue());
                }

                node.setAssignee((String) nodeMap.get("assignee"));
                node.setRoleguid((String) nodeMap.get("roleguid"));
                node.setConditionexpr((String) nodeMap.get("conditionexpr"));
                node.setIsactive("1");
                node.setOrdernum(orderNum++);
                node.setAddtime(new Date());
                node.setUpdatetime(new Date());

                flowNodeMapper.insert(node);
            }
        }

        List<Map<String, Object>> edges = (List<Map<String, Object>>) workflowData.get("edges");
        if (edges != null) {
            int orderNum = 1;
            for (Map<String, Object> edgeMap : edges) {
                FlowEdge edge = new FlowEdge();
                edge.setRowguid(UUID.randomUUID().toString());
                edge.setFlowguid(flowId);
                edge.setSourcenode((String) edgeMap.get("source"));
                edge.setTargetnode((String) edgeMap.get("target"));
                edge.setLabel((String) edgeMap.get("label"));
                edge.setIsactive("1");
                edge.setOrdernum(orderNum++);
                edge.setAddtime(new Date());
                edge.setUpdatetime(new Date());

                flowEdgeMapper.insert(edge);
            }
        }

        return true;
    }
}