package com.community.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("zl_userinfo")
public class User {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("username")
    private String username;

    @JsonIgnore
    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("addtime")
    private LocalDateTime addtime;
}