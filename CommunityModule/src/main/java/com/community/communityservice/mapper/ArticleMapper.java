package com.community.communityservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.communityservice.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
