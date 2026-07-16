package com.community.workflowservice.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.workflowservice.entity.FlowInfo;

public interface FlowInfoService extends IService<FlowInfo> {

    List<FlowInfo> getAllFlowInfos();

    FlowInfo getFlowInfoById(String id);

    boolean createFlowInfo(FlowInfo flowInfo);

    boolean updateFlowInfo(FlowInfo flowInfo);

    boolean deleteFlowInfo(String id);

    List<FlowInfo> searchFlowInfos(String keyword);
}