package com.community.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("zl_rolemenu_perm")
public class RoleMenuPerm {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    private String roleguid;

    private String menuguid;

    public RoleMenuPerm() {
    }

    public RoleMenuPerm(String rowguid, String roleguid, String menuguid) {
        this.rowguid = rowguid;
        this.roleguid = roleguid;
        this.menuguid = menuguid;
    }

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getRoleguid() {
        return roleguid;
    }

    public void setRoleguid(String roleguid) {
        this.roleguid = roleguid;
    }

    public String getMenuguid() {
        return menuguid;
    }

    public void setMenuguid(String menuguid) {
        this.menuguid = menuguid;
    }
}