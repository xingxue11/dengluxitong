CREATE TABLE IF NOT EXISTS `abs_flowinstance` (
  `rowguid` varchar(50) NOT NULL COMMENT '实例ID',
  `flowguid` varchar(50) DEFAULT NULL COMMENT '流程定义ID',
  `applyguid` varchar(50) DEFAULT NULL COMMENT '申请单ID',
  `status` varchar(20) DEFAULT NULL COMMENT '状态：running/completed/cancelled',
  `currentnode` varchar(50) DEFAULT NULL COMMENT '当前节点',
  `datajson` text DEFAULT NULL COMMENT '流程数据JSON',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `endtime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`rowguid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程实例表';

CREATE TABLE IF NOT EXISTS `abs_flowtask` (
  `rowguid` varchar(50) NOT NULL COMMENT '任务ID',
  `instanceguid` varchar(50) DEFAULT NULL COMMENT '实例ID',
  `nodelabel` varchar(100) DEFAULT NULL COMMENT '节点标签',
  `nodetype` varchar(20) DEFAULT NULL COMMENT '节点类型',
  `assignee` varchar(50) DEFAULT NULL COMMENT '处理人',
  `roleguid` varchar(50) DEFAULT NULL COMMENT '处理角色',
  `status` varchar(20) DEFAULT NULL COMMENT '状态：pending/completed',
  `comment` varchar(500) DEFAULT NULL COMMENT '处理意见',
  `createuser` varchar(50) DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `finishuser` varchar(50) DEFAULT NULL COMMENT '完成人',
  `finishtime` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`rowguid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程任务表';

INSERT INTO `abs_flowinfo` (`rowguid`, `groupname`, `grouptype`, `flowname`, `userguid`, `description`, `status`, `startrole`, `startname`, `flowtype`, `typename`, `addtime`) 
VALUES ('car_apply_flow', '车位申请流程', 'flow', '车位申请流程', '1', '车位申请审批流程', '1', '1', '业主', 'carapply', '车位申请', NOW());