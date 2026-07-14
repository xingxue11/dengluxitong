package com.community.communityservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.communityservice.entity.FlowInstance;
import com.community.communityservice.mapper.FlowInstanceMapper;
import com.community.communityservice.service.FlowInstanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FlowInstanceServiceImpl implements FlowInstanceService {

    private final FlowInstanceMapper flowInstanceMapper;

    public FlowInstanceServiceImpl(FlowInstanceMapper flowInstanceMapper) {
        this.flowInstanceMapper = flowInstanceMapper;
    }

    @Override
    public FlowInstance createInstance(String flowguid, String applyguid, String datajson) {
        FlowInstance instance = new FlowInstance();
        instance.setRowguid(UUID.randomUUID().toString().replace("-", "").substring(0, 30));
        instance.setFlowguid(flowguid);
        instance.setApplyguid(applyguid);
        instance.setStatus("running");
        instance.setCurrentnode("start");
        instance.setDatajson(datajson);
        instance.setCreatetime(LocalDateTime.now());
        flowInstanceMapper.insert(instance);
        return instance;
    }

    @Override
    public FlowInstance findById(String id) {
        return flowInstanceMapper.selectById(id);
    }

    @Override
    public List<FlowInstance> findByApplyguid(String applyguid) {
        LambdaQueryWrapper<FlowInstance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowInstance::getApplyguid, applyguid);
        return flowInstanceMapper.selectList(queryWrapper);
    }

    @Override
    public List<FlowInstance> findByFlowguid(String flowguid) {
        LambdaQueryWrapper<FlowInstance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowInstance::getFlowguid, flowguid);
        return flowInstanceMapper.selectList(queryWrapper);
    }

    @Override
    public void updateStatus(String instanceguid, String status, String currentnode) {
        FlowInstance instance = flowInstanceMapper.selectById(instanceguid);
        if (instance != null) {
            instance.setStatus(status);
            instance.setCurrentnode(currentnode);
            if ("completed".equals(status)) {
                instance.setEndtime(LocalDateTime.now());
            }
            flowInstanceMapper.updateById(instance);
        }
    }
}