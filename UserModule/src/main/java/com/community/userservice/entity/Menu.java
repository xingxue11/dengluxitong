package com.community.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("zl_menuinfo")
public class Menu {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("menuname")
    private String menuname;

    @TableField("menuicon")
    private String menuicon;

    @TableField("menupage")
    private String menupage;

    @TableField("isroot")
    private String isroot;

    @TableField("parentguid")
    private String parentguid;

    @TableField("ordernum")
    private Integer ordernum;

    @TableField("addtime")
    private LocalDateTime addtime;

    @TableField(exist = false)
    private List<Menu> children;
}