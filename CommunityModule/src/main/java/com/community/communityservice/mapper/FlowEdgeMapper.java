package com.community.communityservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.communityservice.entity.FlowEdge;

public interface FlowEdgeMapper extends BaseMapper<FlowEdge> {

    @Select("SELECT * FROM abs_flowedge WHERE flowguid = #{flowguid} AND isactive = '1' ORDER BY ordernum")
    List<FlowEdge> selectByFlowGuid(String flowguid);

    @Delete("DELETE FROM abs_flowedge WHERE flowguid = #{flowguid}")
    int deleteByFlowGuid(String flowguid);
}