package com.community.workflowservice.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("abs_flownode")
public class FlowNode {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("flowguid")
    private String flowguid;

    @TableField("nodetype")
    private String nodetype;

    @TableField("nodelabel")
    private String nodelabel;

    @TableField("positionx")
    private Integer positionx;

    @TableField("positiony")
    private Integer positiony;

    @TableField("assignee")
    private String assignee;

    @TableField("roleguid")
    private String roleguid;

    @TableField("conditionexpr")
    private String conditionexpr;

    @TableField("isactive")
    private String isactive;

    @TableField("ordernum")
    private Integer ordernum;

    @TableField("addtime")
    private Date addtime;

    @TableField("updatetime")
    private Date updatetime;
}