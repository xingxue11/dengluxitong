package com.community.communityservice.service;

import com.community.communityservice.entity.FlowInstance;

import java.util.List;

public interface FlowInstanceService {

    FlowInstance createInstance(String flowguid, String applyguid, String datajson);

    FlowInstance findById(String id);

    List<FlowInstance> findByApplyguid(String applyguid);

    List<FlowInstance> findByFlowguid(String flowguid);

    void updateStatus(String instanceguid, String status, String currentnode);

    void update(FlowInstance instance);
}