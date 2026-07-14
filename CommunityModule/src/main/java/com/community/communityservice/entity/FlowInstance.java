package com.community.communityservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("abs_flowinstance")
public class FlowInstance {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("flowguid")
    private String flowguid;

    @TableField("applyguid")
    private String applyguid;

    @TableField("status")
    private String status;

    @TableField("currentnode")
    private String currentnode;

    @TableField("datajson")
    private String datajson;

    @TableField("createtime")
    private LocalDateTime createtime;

    @TableField("endtime")
    private LocalDateTime endtime;

    public FlowInstance() {
    }

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getFlowguid() {
        return flowguid;
    }

    public void setFlowguid(String flowguid) {
        this.flowguid = flowguid;
    }

    public String getApplyguid() {
        return applyguid;
    }

    public void setApplyguid(String applyguid) {
        this.applyguid = applyguid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentnode() {
        return currentnode;
    }

    public void setCurrentnode(String currentnode) {
        this.currentnode = currentnode;
    }

    public String getDatajson() {
        return datajson;
    }

    public void setDatajson(String datajson) {
        this.datajson = datajson;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public LocalDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDateTime endtime) {
        this.endtime = endtime;
    }
}