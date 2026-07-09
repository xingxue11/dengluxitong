package com.community.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("zl_userrole_perm")
public class UserRolePerm {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("userguid")
    private String userguid;

    @TableField("rolelguid")
    private String rolelguid;
}