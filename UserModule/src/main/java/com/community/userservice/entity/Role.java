package com.community.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("zl_roleinfo")
public class Role {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("rolename")
    private String rolename;

    @TableField("addtime")
    private LocalDateTime addtime;
}