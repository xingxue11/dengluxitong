package com.community.communityservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("article")
public class Article {

    @TableId(value = "rowguid", type = IdType.INPUT)
    private String rowguid;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("author")
    private String author;

    @TableField("status")
    private String status;

    @TableField("addtime")
    private LocalDateTime addtime;

    public Article() {
    }

    public String getRowguid() { return rowguid; }
    public void setRowguid(String rowguid) { this.rowguid = rowguid; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getAddtime() { return addtime; }
    public void setAddtime(LocalDateTime addtime) { this.addtime = addtime; }
}
