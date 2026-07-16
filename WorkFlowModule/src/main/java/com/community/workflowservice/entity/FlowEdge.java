package com.community.workflowservice.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("abs_flowedge")
public class FlowEdge {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("flowguid")
    private String flowguid;

    @TableField("sourcenode")
    private String sourcenode;

    @TableField("targetnode")
    private String targetnode;

    @TableField("label")
    private String label;

    @TableField("isactive")
    private String isactive;

    @TableField("ordernum")
    private Integer ordernum;

    @TableField("addtime")
    private Date addtime;

    @TableField("updatetime")
    private Date updatetime;
}