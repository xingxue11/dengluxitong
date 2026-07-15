package com.community.communityservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.communityservice.entity.FlowEdge;

public interface FlowEdgeMapper extends BaseMapper<FlowEdge> {

    @Select("SELECT * FROM abs_flowedge WHERE flowguid = #{flowguid} AND isactive = '1' ORDER BY ordernum")
    List<FlowEdge> selectByFlowGuid(@Param("flowguid") String flowguid);

    @Delete("DELETE FROM abs_flowedge WHERE flowguid = #{flowguid}")
    int deleteByFlowGuid(@Param("flowguid") String flowguid);

    @Select("SELECT * FROM abs_flowedge WHERE flowguid = #{flowguid} AND sourcenode = #{sourcenode} AND isactive = '1'")
    List<FlowEdge> selectNextEdges(@Param("flowguid") String flowguid, @Param("sourcenode") String sourcenode);
}