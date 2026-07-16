package com.community.workflowservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.workflowservice.entity.FlowInstance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowInstanceMapper extends BaseMapper<FlowInstance> {
}