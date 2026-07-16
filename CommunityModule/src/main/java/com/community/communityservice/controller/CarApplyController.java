package com.community.communityservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.community.userservice.entity.User;
import com.community.userservice.service.UserService;
import com.community.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
import com.community.communityservice.entity.FlowEdge;
import com.community.communityservice.entity.FlowInfo;
import com.community.communityservice.entity.FlowInstance;
import com.community.communityservice.entity.FlowNode;
import com.community.communityservice.mapper.FlowEdgeMapper;
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
    private final FlowEdgeMapper flowEdgeMapper;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public CarApplyController(CarApplyService carApplyService, 
                              FlowInstanceService flowInstanceService,
                              FlowTaskService flowTaskService,
                              FlowInfoMapper flowInfoMapper,
                              FlowNodeMapper flowNodeMapper,
                              FlowEdgeMapper flowEdgeMapper,
                              ObjectMapper objectMapper,
                              JwtUtil jwtUtil,
                              UserService userService) {
        this.carApplyService = carApplyService;
        this.flowInstanceService = flowInstanceService;
        this.flowTaskService = flowTaskService;
        this.flowInfoMapper = flowInfoMapper;
        this.flowNodeMapper = flowNodeMapper;
        this.flowEdgeMapper = flowEdgeMapper;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
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

    @GetMapping("/my-carapply")
    public ResponseEntity<Map<String, Object>> getMyCarApply(HttpServletRequest httpRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String authorizationHeader = httpRequest.getHeader("Authorization");
            if (authorizationHeader == null) {
                response.put("success", false);
                response.put("message", "请先登录");
                return ResponseEntity.ok(response);
            }
            
            String username = jwtUtil.extractUsername(authorizationHeader);
            
            List<User> users = userService.findAll();
            User currentUser = users.stream()
                    .filter(u -> username.equals(u.getUsername()))
                    .findFirst()
                    .orElse(null);
            
            if (currentUser == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }
            
            String userguid = currentUser.getRowguid();
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
                
                List<FlowInstance> instances = flowInstanceService.findByApplyguid(carApply.getRowguid());
                if (!instances.isEmpty()) {
                    FlowInstance instance = instances.get(0);
                    map.put("status", instance.getStatus());
                    map.put("currentnode", instance.getCurrentnode());
                    
                    List<com.community.communityservice.entity.FlowTask> tasks = flowTaskService.findByInstanceguid(instance.getRowguid());
                    for (com.community.communityservice.entity.FlowTask task : tasks) {
                        if ("pending".equals(task.getStatus())) {
                            map.put("taskNodelabel", task.getNodelabel());
                            break;
                        }
                    }
                } else {
                    map.put("status", "pending");
                }
                
                listMap.add(map);
            }
            
            response.put("success", true);
            response.put("data", listMap);
        } catch (Exception e) {
            logger.error("获取我的车位申请失败: {}", e.getMessage());
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

    @PostMapping("/my-applies/approve")
    public ResponseEntity<Map<String, Object>> approveTask(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        
        logger.info("当前用户传入参数：{}",request);
        
        try {
            String taskguid = (String) request.get("taskguid");
            String instanceguid = (String) request.get("instanceguid");
            String action = (String) request.get("action");
            String comment = (String) request.get("comment");
            String finishuser = (String) request.get("finishuser");

            if (taskguid == null || instanceguid == null || action == null) {
                response.put("success", false);
                response.put("message", "参数不完整");
                return ResponseEntity.ok(response);
            }

            com.community.communityservice.entity.FlowTask task = flowTaskService.findById(taskguid);
            if (task == null || !"pending".equals(task.getStatus())) {
                response.put("success", false);
                response.put("message", "任务不存在或已处理");
                return ResponseEntity.ok(response);
            }

            task.setStatus("completed");
            task.setComment(comment);
            task.setFinishuser(finishuser);
            task.setFinishtime(java.time.LocalDateTime.now());
            flowTaskService.update(task);

            FlowInstance instance = flowInstanceService.findById(instanceguid);
            if (instance == null) {
                response.put("success", false);
                response.put("message", "流程实例不存在");
                return ResponseEntity.ok(response);
            }

            
            //如果驳回整个流程直接结束
            if ("reject".equals(action)) {
                instance.setStatus("completed");
                instance.setEndtime(java.time.LocalDateTime.now());
                flowInstanceService.update(instance);
                response.put("success", true);
                response.put("message", "已驳回");
                return ResponseEntity.ok(response);
            }

            String currentNodeId = task.getNodeid();

            List<FlowEdge> edges = flowEdgeMapper.selectNextEdges(instance.getFlowguid(), currentNodeId);
            
            //当前节点后面没有边，整个流程结束
            if (edges.isEmpty()) {
                instance.setStatus("completed");
                instance.setEndtime(java.time.LocalDateTime.now());
                flowInstanceService.update(instance);
                response.put("success", true);
                response.put("message", "审批完成，流程结束");
                return ResponseEntity.ok(response);
            }
            
            
            //存在连线的情况

            for (FlowEdge edge : edges) {
                String targetNodeId = edge.getTargetnode();
                List<FlowNode> nodes = flowNodeMapper.selectByFlowGuid(instance.getFlowguid());
                
                
                
                for (FlowNode node : nodes) {
                	
                	
                	
                    if (targetNodeId.equals(node.getRowguid())) {
                        if ("approval".equals(node.getNodetype())) {
                            flowTaskService.createTask(
                                instanceguid,
                                node.getRowguid(),
                                node.getNodelabel(),
                                node.getNodetype(),
                                node.getAssignee(),
                                node.getRoleguid(),
                                finishuser
                            );
                            instance.setCurrentnode(node.getNodelabel());
                        } else if ("condition".equals(node.getNodetype())) {
                            String conditionExpr = node.getConditionexpr();
                            List<FlowEdge> conditionEdges = flowEdgeMapper.selectNextEdges(instance.getFlowguid(), node.getRowguid());
                            
                            String nextTargetNodeId = null;
                            for (FlowEdge condEdge : conditionEdges) {
                                String condEdgeLabel = condEdge.getLabel();
                                if ("通过".equals(condEdgeLabel) || "yes".equals(condEdgeLabel) || "true".equals(condEdgeLabel)) {
                                    if ("approve".equals(action)) {
                                        nextTargetNodeId = condEdge.getTargetnode();
                                    }
                                } else if ("驳回".equals(condEdgeLabel) || "no".equals(condEdgeLabel) || "false".equals(condEdgeLabel)) {
                                    if ("reject".equals(action)) {
                                        nextTargetNodeId = condEdge.getTargetnode();
                                    }
                                } else {
                                    if ("approve".equals(action)) {
                                        nextTargetNodeId = condEdge.getTargetnode();
                                    }
                                }
                            }
                            
                            if (nextTargetNodeId != null) {
                                for (FlowNode nextNode : nodes) {
                                    if (nextTargetNodeId.equals(nextNode.getRowguid())) {
                                        if ("approval".equals(nextNode.getNodetype())) {
                                            flowTaskService.createTask(
                                                instanceguid,
                                                nextNode.getRowguid(),
                                                nextNode.getNodelabel(),
                                                nextNode.getNodetype(),
                                                nextNode.getAssignee(),
                                                nextNode.getRoleguid(),
                                                finishuser
                                            );
                                            instance.setCurrentnode(nextNode.getNodelabel());
                                        } else if ("end".equals(nextNode.getNodetype())) {
                                            instance.setStatus("completed");
                                            instance.setEndtime(java.time.LocalDateTime.now());
                                        }
                                        break;
                                    }
                                }
                            }
                        } else if ("end".equals(node.getNodetype())) {
                            instance.setStatus("completed");
                            instance.setEndtime(java.time.LocalDateTime.now());
                        }
                        break;
                    }
                }
            }

            flowInstanceService.update(instance);
            response.put("success", true);
            response.put("message", "审批通过，已流转到下一环节");
        } catch (Exception e) {
            logger.error("审批失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Map<String, Object>> createCarApply(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String authorizationHeader = httpRequest.getHeader("Authorization");
            if (authorizationHeader == null) {
                response.put("success", false);
                response.put("message", "请先登录");
                return ResponseEntity.ok(response);
            }
            
            String username = jwtUtil.extractUsername(authorizationHeader);
            
            List<User> users = userService.findAll();
            User currentUser = users.stream()
                    .filter(u -> username.equals(u.getUsername()))
                    .findFirst()
                    .orElse(null);
            
            if (currentUser == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }
            
            String userguid = currentUser.getRowguid();
            
            CarApply carApply = new CarApply();
            carApply.setUserguid(userguid);
            carApply.setUsername(currentUser.getUsername());
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
                            node.getRowguid(),
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