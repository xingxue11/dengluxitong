package com.community.communityservice.service;

import com.community.communityservice.entity.FlowTask;

import java.util.List;

public interface FlowTaskService {

    FlowTask createTask(String instanceguid, String nodeid,String nodelabel, String nodetype, String assignee, String roleguid, String createuser);

    FlowTask findById(String id);

    List<FlowTask> findByInstanceguid(String instanceguid);

    List<FlowTask> findByAssignee(String assignee);

    void completeTask(String taskguid, String finishuser, String comment);

    void updateTaskStatus(String taskguid, String status);

    void update(FlowTask task);
}