package com.community.communityservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.communityservice.entity.FlowTask;
import com.community.communityservice.mapper.FlowTaskMapper;
import com.community.communityservice.service.FlowTaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FlowTaskServiceImpl implements FlowTaskService {

    private final FlowTaskMapper flowTaskMapper;

    public FlowTaskServiceImpl(FlowTaskMapper flowTaskMapper) {
        this.flowTaskMapper = flowTaskMapper;
    }

    @Override
    public FlowTask createTask(String instanceguid, String nodelabel, String nodetype, String assignee, String roleguid, String createuser) {
        FlowTask task = new FlowTask();
        task.setRowguid(UUID.randomUUID().toString().replace("-", "").substring(0, 30));
        task.setInstanceguid(instanceguid);
        task.setNodelabel(nodelabel);
        task.setNodetype(nodetype);
        task.setAssignee(assignee);
        task.setRoleguid(roleguid);
        task.setStatus("pending");
        task.setCreateuser(createuser);
        task.setCreatetime(LocalDateTime.now());
        flowTaskMapper.insert(task);
        return task;
    }

    @Override
    public FlowTask findById(String id) {
        return flowTaskMapper.selectById(id);
    }

    @Override
    public List<FlowTask> findByInstanceguid(String instanceguid) {
        LambdaQueryWrapper<FlowTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowTask::getInstanceguid, instanceguid);
        queryWrapper.orderByAsc(FlowTask::getCreatetime);
        return flowTaskMapper.selectList(queryWrapper);
    }

    @Override
    public List<FlowTask> findByAssignee(String assignee) {
        LambdaQueryWrapper<FlowTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowTask::getRoleguid, assignee);
        queryWrapper.eq(FlowTask::getStatus, "pending");
        queryWrapper.orderByAsc(FlowTask::getCreatetime);
        return flowTaskMapper.selectList(queryWrapper);
    }

    @Override
    public void completeTask(String taskguid, String finishuser, String comment) {
        FlowTask task = flowTaskMapper.selectById(taskguid);
        if (task != null) {
            task.setStatus("completed");
            task.setFinishuser(finishuser);
            task.setFinishtime(LocalDateTime.now());
            task.setComment(comment);
            flowTaskMapper.updateById(task);
        }
    }

    @Override
    public void updateTaskStatus(String taskguid, String status) {
        FlowTask task = flowTaskMapper.selectById(taskguid);
        if (task != null) {
            task.setStatus(status);
            flowTaskMapper.updateById(task);
        }
    }
}