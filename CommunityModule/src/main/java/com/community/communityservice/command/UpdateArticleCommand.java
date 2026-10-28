package com.community.communityservice.command;

import com.community.communityservice.entity.Article;
import com.community.communityservice.service.ArticleService;

/**
 * ConcreteCommand：更新公告命令
 * execute -> 更新公告内容
 * undo    -> 恢复更新前的旧数据
 */
public class UpdateArticleCommand implements Command {

    private final ArticleService articleService;
    private final String rowguid;
    private final Article newArticle;
    private Article oldArticle;

    public UpdateArticleCommand(ArticleService articleService, String rowguid, Article newArticle) {
        this.articleService = articleService;
        this.rowguid = rowguid;
        this.newArticle = newArticle;
    }

    @Override
    public void execute() {
        this.oldArticle = articleService.findById(rowguid);
        articleService.update(rowguid, newArticle);
    }

    @Override
    public void undo() {
        if (oldArticle != null) {
            articleService.update(rowguid, oldArticle);
        }
    }

    @Override
    public String getCommandName() {
        return "修改公告：" + rowguid;
    }
}
