package com.community.communityservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("abs_flowtask")
public class FlowTask {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("instanceguid")
    private String instanceguid;

    @TableField("nodelabel")
    private String nodelabel;

    @TableField("nodetype")
    private String nodetype;
    
    @TableField("nodeid")
    private String nodeid;

    @TableField("assignee")
    private String assignee;

    @TableField("roleguid")
    private String roleguid;

    @TableField("status")
    private String status;

    @TableField("comment")
    private String comment;

    @TableField("createuser")
    private String createuser;

    @TableField("createtime")
    private LocalDateTime createtime;

    @TableField("finishuser")
    private String finishuser;

    @TableField("finishtime")
    private LocalDateTime finishtime;

    public FlowTask() {
    }

    public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getInstanceguid() {
        return instanceguid;
    }

    public void setInstanceguid(String instanceguid) {
        this.instanceguid = instanceguid;
    }

    public String getNodelabel() {
        return nodelabel;
    }

    public void setNodelabel(String nodelabel) {
        this.nodelabel = nodelabel;
    }

    public String getNodetype() {
        return nodetype;
    }

    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getRoleguid() {
        return roleguid;
    }

    public void setRoleguid(String roleguid) {
        this.roleguid = roleguid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public String getFinishuser() {
        return finishuser;
    }

    public void setFinishuser(String finishuser) {
        this.finishuser = finishuser;
    }

    public LocalDateTime getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(LocalDateTime finishtime) {
        this.finishtime = finishtime;
    }
}