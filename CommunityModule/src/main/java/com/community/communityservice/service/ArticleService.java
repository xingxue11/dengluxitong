package com.community.communityservice.service;

import com.community.communityservice.entity.Article;
import com.community.communityservice.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Receiver：真正操作 article 表的数据库读写
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    public List<Article> findAll() {
        return articleMapper.selectList(null);
    }

    public Article findById(String rowguid) {
        return articleMapper.selectById(rowguid);
    }

    public Article create(Article article) {
        article.setRowguid(UUID.randomUUID().toString().replace("-", ""));
        if (article.getStatus() == null) {
            article.setStatus("1");
        }
        article.setAddtime(LocalDateTime.now());
        articleMapper.insert(article);
        return article;
    }

    public Article update(String rowguid, Article article) {
        article.setRowguid(rowguid);
        articleMapper.updateById(article);
        return articleMapper.selectById(rowguid);
    }

    public void delete(String rowguid) {
        articleMapper.deleteById(rowguid);
    }
}
