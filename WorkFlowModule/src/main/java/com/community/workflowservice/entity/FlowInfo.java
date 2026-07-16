package com.community.workflowservice.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("abs_flowinfo")
public class FlowInfo {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("groupname")
    private String groupname;

    @TableField("grouptype")
    private String grouptype;

    @TableField("flowname")
    private String flowname;

    @TableField("userguid")
    private String userguid;

    @TableField("description")
    private String description;

    @TableField("status")
    private String status;

    @TableField("startrole")
    private String startrole;

    @TableField("startname")
    private String startname;

    @TableField("flowtype")
    private String flowtype;

    @TableField("typename")
    private String typename;

    @TableField("addtime")
    private Date addtime;
}