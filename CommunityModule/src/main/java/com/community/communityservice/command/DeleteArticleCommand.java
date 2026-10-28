package com.community.communityservice.command;

import com.community.communityservice.entity.Article;
import com.community.communityservice.service.ArticleService;

/**
 * ConcreteCommand：删除/撤销公告命令
 * execute -> 删除公告
 * undo    -> 把删掉的公告恢复回来
 */
public class DeleteArticleCommand implements Command {

    private final ArticleService articleService;
    private final String rowguid;
    private Article deletedArticle;

    public DeleteArticleCommand(ArticleService articleService, String rowguid) {
        this.articleService = articleService;
        this.rowguid = rowguid;
    }

    @Override
    public void execute() {
        this.deletedArticle = articleService.findById(rowguid);
        articleService.delete(rowguid);
    }

    @Override
    public void undo() {
        if (deletedArticle != null) {
            articleService.create(deletedArticle);
        }
    }

    @Override
    public String getCommandName() {
        return "删除公告：" + rowguid;
    }
}
