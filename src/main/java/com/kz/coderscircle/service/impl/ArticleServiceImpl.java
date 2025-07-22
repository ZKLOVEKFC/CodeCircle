package com.kz.coderscircle.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kz.coderscircle.mapper.ArticleMapper;
import com.kz.coderscircle.pojo.Article;
import com.kz.coderscircle.pojo.PageBean;
import com.kz.coderscircle.pojo.User;
import com.kz.coderscircle.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service("articleServiceImpl")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Article createArticle(Article article) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setCreateUser(loginUser.getId());
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.insert(article);
        return article;
    }

    @Override
    public Article findById(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Long categoryId, String keyword) {
        Page<Article> page = Page.of(pageNum, pageSize);
        String state = "已发布";
        articleMapper.list(page, categoryId, state, keyword);
        return new PageBean<>(page.getTotal(), page.getRecords());
    }

    @Override
    public void update(Article article) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Article dbArticle = articleMapper.selectById(article.getId());
        if (dbArticle == null) {
            throw new RuntimeException("文章不存在");
        }
        if (!dbArticle.getCreateUser().equals(loginUser.getId())) {
            throw new RuntimeException("无权修改他人文章");
        }
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateById(article);
    }

    @Override
    public void delete(Long id) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = loginUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            Article dbArticle = articleMapper.selectById(id);
            if (dbArticle == null) {
                return;
            }
            if (!dbArticle.getCreateUser().equals(loginUser.getId())) {
                throw new RuntimeException("无权删除他人文章");
            }
        }
        articleMapper.deleteById(id);
    }

    /**
     * 获取当前登录用户的文章列表（分页）
     */
    @Override
    public PageBean<Article> myList(Integer pageNum, Integer pageSize) {
        Page<Article> page = Page.of(pageNum, pageSize);
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getId();

        // 调用Mapper的myList方法
        articleMapper.myList(page, userId, null, null, null);
        return new PageBean<>(page.getTotal(), page.getRecords());
    }
}