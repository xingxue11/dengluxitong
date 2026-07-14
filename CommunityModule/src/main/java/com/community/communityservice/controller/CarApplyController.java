package com.community.communityservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.communityservice.entity.CarApply;
import com.community.communityservice.entity.FlowInfo;
import com.community.communityservice.entity.FlowInstance;
import com.community.communityservice.entity.FlowNode;
import com.community.communityservice.mapper.FlowInfoMapper;
import com.community.communityservice.mapper.FlowNodeMapper;
import com.community.communityservice.service.CarApplyService;
import com.community.communityservice.service.FlowInstanceService;
import com.community.communityservice.service.FlowTaskService;

@RestController
@RequestMapping("/api/car-apply")
public class CarApplyController {

    private static final Logger logger = LoggerFactory.getLogger(CarApplyController.class.getName());

    private final CarApplyService carApplyService;
    private final FlowInstanceService flowInstanceService;
    private final FlowTaskService flowTaskService;
    private final FlowInfoMapper flowInfoMapper;
    private final FlowNodeMapper flowNodeMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public CarApplyController(CarApplyService carApplyService, 
                              FlowInstanceService flowInstanceService,
                              FlowTaskService flowTaskService,
                              FlowInfoMapper flowInfoMapper,
                              FlowNodeMapper flowNodeMapper,
                              ObjectMapper objectMapper) {
        this.carApplyService = carApplyService;
        this.flowInstanceService = flowInstanceService;
        this.flowTaskService = flowTaskService;
        this.flowInfoMapper = flowInfoMapper;
        this.flowNodeMapper = flowNodeMapper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCarApplies() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarApply> carApplies = carApplyService.findAll();
            List<Map<String, Object>> listMap = new ArrayList<>();

            for (CarApply carApply : carApplies) {
                Map<String, Object> map = new HashMap<>();
                map.put("rowguid", carApply.getRowguid());
                map.put("userguid", carApply.getUserguid());
                map.put("username", carApply.getUsername());
                map.put("startdate", carApply.getStartdate());
                map.put("enddate", carApply.getEnddate());
                map.put("carnum", carApply.getCarnum());
                map.put("cartype", carApply.getCartype());
                map.put("phone", carApply.getPhone());
                map.put("idcard", carApply.getIdcard());
                map.put("createtime", carApply.getCreatetime());
                listMap.add(map);
            }

            response.put("success", true);
            response.put("data", listMap);
        } catch (Exception e) {
            logger.error("获取车位申请列表失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-applies")
    public ResponseEntity<Map<String, Object>> getMyApplies(
            @RequestParam(required = false) String userguid,
            @RequestParam(required = false) String roleguid) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();

     /*       if (userguid != null && !userguid.isEmpty()) {
                List<CarApply> carApplies = carApplyService.findByUserguid(userguid);
                for (CarApply carApply : carApplies) {
                    Map<String, Object> map = buildApplyMap(carApply);
                    List<FlowInstance> instances = flowInstanceService.findByApplyguid(carApply.getRowguid());
                    if (!instances.isEmpty()) {
                        FlowInstance instance = instances.get(0);
                        map.put("instanceguid", instance.getRowguid());
                        map.put("status", instance.getStatus());
                        map.put("currentnode", instance.getCurrentnode());
                        
                        List<com.community.communityservice.entity.FlowTask> tasks = flowTaskService.findByInstanceguid(instance.getRowguid());
                        map.put("tasks", tasks);
                    }
                    resultList.add(map);
                }
            } else if (roleguid != null && !roleguid.isEmpty()) {*/
                List<com.community.communityservice.entity.FlowTask> tasks = flowTaskService.findByAssignee(roleguid);
                for (com.community.communityservice.entity.FlowTask task : tasks) {
                    FlowInstance instance = flowInstanceService.findById(task.getInstanceguid());
                    if (instance != null) {
                        CarApply carApply = carApplyService.findById(instance.getApplyguid());
                        if (carApply != null) {
                            Map<String, Object> map = buildApplyMap(carApply);
                            map.put("instanceguid", instance.getRowguid());
                            map.put("status", instance.getStatus());
                            map.put("currentnode", instance.getCurrentnode());
                            map.put("taskguid", task.getRowguid());
                            map.put("taskStatus", task.getStatus());
                            map.put("taskNodelabel", task.getNodelabel());
                            resultList.add(map);
                        }
                    }
                }
           // }

            response.put("success", true);
            response.put("data", resultList);
        } catch (Exception e) {
            logger.error("获取我的车位申请失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> buildApplyMap(CarApply carApply) {
        Map<String, Object> map = new HashMap<>();
        map.put("rowguid", carApply.getRowguid());
        map.put("userguid", carApply.getUserguid());
        map.put("username", carApply.getUsername());
        map.put("startdate", carApply.getStartdate());
        map.put("enddate", carApply.getEnddate());
        map.put("carnum", carApply.getCarnum());
        map.put("cartype", carApply.getCartype());
        map.put("phone", carApply.getPhone());
        map.put("idcard", carApply.getIdcard());
        map.put("createtime", carApply.getCreatetime());
        return map;
    }

    @GetMapping("/list/{userguid}")
    public ResponseEntity<Map<String, Object>> getCarAppliesByUser(@PathVariable String userguid) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarApply> carApplies = carApplyService.findByUserguid(userguid);
            List<Map<String, Object>> listMap = new ArrayList<>();

            for (CarApply carApply : carApplies) {
                Map<String, Object> map = new HashMap<>();
                map.put("rowguid", carApply.getRowguid());
                map.put("userguid", carApply.getUserguid());
                map.put("username", carApply.getUsername());
                map.put("startdate", carApply.getStartdate());
                map.put("enddate", carApply.getEnddate());
                map.put("carnum", carApply.getCarnum());
                map.put("cartype", carApply.getCartype());
                map.put("phone", carApply.getPhone());
                map.put("idcard", carApply.getIdcard());
                map.put("createtime", carApply.getCreatetime());
                listMap.add(map);
            }

            response.put("success", true);
            response.put("data", listMap);
        } catch (Exception e) {
            logger.error("获取用户车位申请列表失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCarApplyById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            CarApply carApply = carApplyService.findById(id);
            if (carApply != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("rowguid", carApply.getRowguid());
                map.put("userguid", carApply.getUserguid());
                map.put("username", carApply.getUsername());
                map.put("startdate", carApply.getStartdate());
                map.put("enddate", carApply.getEnddate());
                map.put("carnum", carApply.getCarnum());
                map.put("cartype", carApply.getCartype());
                map.put("phone", carApply.getPhone());
                map.put("idcard", carApply.getIdcard());
                map.put("createtime", carApply.getCreatetime());
                response.put("success", true);
                response.put("data", map);
            } else {
                response.put("success", false);
                response.put("message", "车位申请不存在");
            }
        } catch (Exception e) {
            logger.error("获取车位申请详情失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> createCarApply(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            CarApply carApply = new CarApply();
            carApply.setUserguid((String) request.get("userguid"));
            carApply.setUsername((String) request.get("username"));
            carApply.setStartdate(java.time.LocalDateTime.parse((String) request.get("startdate")));
            carApply.setEnddate(java.time.LocalDateTime.parse((String) request.get("enddate")));
            carApply.setCarnum((String) request.get("carnum"));
            carApply.setCartype((String) request.get("cartype"));
            carApply.setPhone((String) request.get("phone"));
            carApply.setIdcard((String) request.get("idcard"));

            carApplyService.create(carApply);

            String applyGuid = carApply.getRowguid();

            FlowInfo flowInfo = flowInfoMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FlowInfo>()
                    .eq(FlowInfo::getFlowtype, "carapply")
                    .eq(FlowInfo::getStatus, "1")
            );

            if (flowInfo != null) {
                String dataJson = objectMapper.writeValueAsString(request);
                
                FlowInstance instance = flowInstanceService.createInstance(
                    flowInfo.getRowguid(), 
                    applyGuid, 
                    dataJson
                );

                List<FlowNode> nodes = flowNodeMapper.selectByFlowGuid(flowInfo.getRowguid());
                
                for (FlowNode node : nodes) {
                    if ("approval".equals(node.getNodetype())) {
                        flowTaskService.createTask(
                            instance.getRowguid(),
                            node.getNodelabel(),
                            node.getNodetype(),
                            node.getAssignee(),
                            node.getRoleguid(),
                            carApply.getUsername()
                        );
                        break;
                    }
                }

                response.put("instanceguid", instance.getRowguid());
            }

            response.put("success", true);
            response.put("message", "车位申请提交成功，流程已创建");
            response.put("rowguid", applyGuid);
        } catch (Exception e) {
            logger.error("提交车位申请失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCarApply(@PathVariable String id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            CarApply carApply = new CarApply();
            carApply.setUserguid((String) request.get("userguid"));
            carApply.setUsername((String) request.get("username"));
            carApply.setStartdate(java.time.LocalDateTime.parse((String) request.get("startdate")));
            carApply.setEnddate(java.time.LocalDateTime.parse((String) request.get("enddate")));
            carApply.setCarnum((String) request.get("carnum"));
            carApply.setCartype((String) request.get("cartype"));
            carApply.setPhone((String) request.get("phone"));
            carApply.setIdcard((String) request.get("idcard"));

            CarApply updated = carApplyService.update(id, carApply);

            if (updated != null) {
                response.put("success", true);
                response.put("message", "车位申请更新成功");
            } else {
                response.put("success", false);
                response.put("message", "车位申请不存在");
            }
        } catch (Exception e) {
            logger.error("更新车位申请失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCarApply(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            carApplyService.delete(id);
            response.put("success", true);
            response.put("message", "车位申请删除成功");
        } catch (Exception e) {
            logger.error("删除车位申请失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}