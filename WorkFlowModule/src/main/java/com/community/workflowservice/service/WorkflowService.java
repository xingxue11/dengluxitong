package com.community.workflowservice.service;

import java.util.Map;

public interface WorkflowService {

    Map<String, Object> getWorkflowByFlowId(String flowId);

    boolean saveWorkflow(String flowId, Map<String, Object> workflowData);
}