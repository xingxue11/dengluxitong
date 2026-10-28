package com.community.communityservice.command;

import com.community.communityservice.entity.Article;
import com.community.communityservice.service.ArticleService;

/**
 * ConcreteCommand：添加公告命令
 * execute -> 新增一条公告
 * undo    -> 撤销就是把刚加的删掉
 */
public class AddArticleCommand implements Command {

    private final ArticleService articleService;
    private final Article article;
    private String createdRowguid;

    public AddArticleCommand(ArticleService articleService, Article article) {
        this.articleService = articleService;
        this.article = article;
    }

    @Override
    public void execute() {
        Article created = articleService.create(article);
        this.createdRowguid = created.getRowguid();
    }

    @Override
    public void undo() {
        if (createdRowguid != null) {
            articleService.delete(createdRowguid);
        }
    }

    @Override
    public String getCommandName() {
        return "添加公告：" + article.getTitle();
    }

    public String getCreatedRowguid() {
        return createdRowguid;
    }
}
