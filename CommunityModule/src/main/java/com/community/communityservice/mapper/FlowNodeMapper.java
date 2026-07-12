package com.community.communityservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.communityservice.entity.FlowNode;

public interface FlowNodeMapper extends BaseMapper<FlowNode> {

    @Select("SELECT * FROM abs_flownode WHERE flowguid = #{flowguid} AND isactive = '1' ORDER BY ordernum")
    List<FlowNode> selectByFlowGuid(String flowguid);

    @Delete("DELETE FROM abs_flownode WHERE flowguid = #{flowguid}")
    int deleteByFlowGuid(String flowguid);
}