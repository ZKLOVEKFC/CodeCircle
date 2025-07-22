package com.kz.coderscircle.service;

import com.kz.coderscircle.pojo.Article;
import com.kz.coderscircle.pojo.PageBean;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ArticleService {

    Article createArticle(Article article);

    Article findById(Long id);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Long categoryId, String keyword);

    void update(Article article);

    void delete(Long id);

    /**
     * 获取当前登录用户的文章列表（分页）
     pageNum  当前页码
     pageSize 每页条数
     */
    PageBean<Article> myList(Integer pageNum, Integer pageSize);
}