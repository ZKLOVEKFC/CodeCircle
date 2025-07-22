package com.kz.coderscircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kz.coderscircle.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    Page<Article> list(Page<Article> page, @Param("categoryId") Long categoryId, @Param("state") String state, @Param("keyword") String keyword);

    Page<Article> myList(Page<Article> page, @Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("state") String state, @Param("keyword") String keyword);

    void incrementLikeCount(Long articleId);

    void decrementLikeCount(Long articleId);
}