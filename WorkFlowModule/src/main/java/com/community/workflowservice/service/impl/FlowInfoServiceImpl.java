package com.community.workflowservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.workflowservice.entity.FlowInfo;
import com.community.workflowservice.mapper.FlowInfoMapper;
import com.community.workflowservice.service.FlowInfoService;

@Service
public class FlowInfoServiceImpl extends ServiceImpl<FlowInfoMapper, FlowInfo> implements FlowInfoService {

    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Override
    public List<FlowInfo> getAllFlowInfos() {
        LambdaQueryWrapper<FlowInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FlowInfo::getAddtime);
        return flowInfoMapper.selectList(wrapper);
    }

    @Override
    public FlowInfo getFlowInfoById(String id) {
        return flowInfoMapper.selectById(id);
    }

    @Override
    public boolean createFlowInfo(FlowInfo flowInfo) {
        return flowInfoMapper.insert(flowInfo) > 0;
    }

    @Override
    public boolean updateFlowInfo(FlowInfo flowInfo) {
        return flowInfoMapper.updateById(flowInfo) > 0;
    }

    @Override
    public boolean deleteFlowInfo(String id) {
        return flowInfoMapper.deleteById(id) > 0;
    }

    @Override
    public List<FlowInfo> searchFlowInfos(String keyword) {
        LambdaQueryWrapper<FlowInfo> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(FlowInfo::getFlowname, keyword)
                   .or()
                   .like(FlowInfo::getDescription, keyword);
        }
        wrapper.orderByDesc(FlowInfo::getAddtime);
        return flowInfoMapper.selectList(wrapper);
    }
}