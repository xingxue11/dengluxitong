package com.community.workflowservice.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.workflowservice.entity.FlowInfo;

@Mapper
public interface FlowInfoMapper extends BaseMapper<FlowInfo> {
}