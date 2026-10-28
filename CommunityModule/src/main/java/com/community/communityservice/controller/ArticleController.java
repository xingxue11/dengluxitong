package com.community.communityservice.controller;

import com.community.communityservice.command.AddArticleCommand;
import com.community.communityservice.command.DeleteArticleCommand;
import com.community.communityservice.command.UpdateArticleCommand;
import com.community.communityservice.entity.Article;
import com.community.communityservice.invoker.CommandInvoker;
import com.community.communityservice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client：公告管理控制器
 * 创建命令对象，交给 Invoker 执行
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommandInvoker commandInvoker;

    /**
     * 查询公告列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        Map<String, Object> resp = new HashMap<>();
        List<Article> list = articleService.findAll();
        resp.put("success", true);
        resp.put("data", list);
        return ResponseEntity.ok(resp);
    }

    /**
     * 添加公告（命令模式）
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Article article) {
        Map<String, Object> resp = new HashMap<>();
        AddArticleCommand cmd = new AddArticleCommand(articleService, article);
        commandInvoker.executeCommand(cmd);
        resp.put("success", true);
        resp.put("message", "公告添加成功");
        resp.put("rowguid", cmd.getCreatedRowguid());
        return ResponseEntity.ok(resp);
    }

    /**
     * 修改公告（命令模式）
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable String id, @RequestBody Article article) {
        Map<String, Object> resp = new HashMap<>();
        UpdateArticleCommand cmd = new UpdateArticleCommand(articleService, id, article);
        commandInvoker.executeCommand(cmd);
        resp.put("success", true);
        resp.put("message", "公告修改成功");
        return ResponseEntity.ok(resp);
    }

    /**
     * 删除公告（命令模式）
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String id) {
        Map<String, Object> resp = new HashMap<>();
        DeleteArticleCommand cmd = new DeleteArticleCommand(articleService, id);
        commandInvoker.executeCommand(cmd);
        resp.put("success", true);
        resp.put("message", "公告删除成功");
        return ResponseEntity.ok(resp);
    }

    /**
     * 撤销上一步操作
     */
    @PostMapping("/undo")
    public ResponseEntity<Map<String, Object>> undo() {
        Map<String, Object> resp = new HashMap<>();
        boolean ok = commandInvoker.undoLast();
        if (ok) {
            resp.put("success", true);
            resp.put("message", "撤销成功");
        } else {
            resp.put("success", false);
            resp.put("message", "没有可撤销的操作");
        }
        resp.put("historySize", commandInvoker.getHistorySize());
        return ResponseEntity.ok(resp);
    }
}
