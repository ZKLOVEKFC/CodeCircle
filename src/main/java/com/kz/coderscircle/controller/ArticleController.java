package com.kz.coderscircle.controller;

import com.kz.coderscircle.pojo.Article;
import com.kz.coderscircle.pojo.PageBean;
import com.kz.coderscircle.pojo.Result;
import com.kz.coderscircle.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章功能接口
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /*article 包含文章标题、内容、分类ID等信息的JSON对象*/
    @PostMapping
    public Result<Article> createArticle(@RequestBody @Validated Article article) {
        Article createdArticle = articleService.createArticle(article);
        return Result.success(createdArticle);
    }

    /*根据ID获取文章详情。id 文章ID，从URL路径中获取*/
    @GetMapping("/detail/{id}")
    public Result<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.findById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        return Result.success(article);
    }

    /*获取公开的文章列表（分页），支持按分类和关键字进行动态查询。pageNum当前页码, 默认为1;pageSize每页条数, 默认为10;categoryId分类ID;keyword搜索关键字(对标题和内容进行模糊搜索)*/
    @GetMapping
    public Result<PageBean<Article>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword
    ) {
        PageBean<Article> pageBean = articleService.list(pageNum, pageSize, categoryId, keyword);
        return Result.success(pageBean);
    }

    @GetMapping("/my")
    public Result<PageBean<Article>> myList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageBean<Article> pageBean = articleService.myList(pageNum, pageSize);
        return Result.success(pageBean);
    }
    /*更新文章。需要登录认证，且只能由作者本人或管理员操作。
    article 包含文章ID及要更新字段的JSON对象*/
    @PutMapping
    public Result<Void> update(@RequestBody @Validated Article article) {
        try {
            articleService.update(article);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID删除文章,只能由作者本人或管理员操作。
        id 要删除的文章ID*/
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            articleService.delete(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}