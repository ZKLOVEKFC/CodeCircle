package com.kz.coderscircle.controller;

import com.kz.coderscircle.pojo.Category;
import com.kz.coderscircle.pojo.PageBean;
import com.kz.coderscircle.pojo.Result;
import com.kz.coderscircle.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章分类功能接口
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增文章分类仅管理员可以新增，category 包含分类名称和别名的JSON对象*/
    @PostMapping
    public Result add(@RequestBody @Validated Category category) {
        // @Validated 注解会触发Category实体类中的校验规则如果校验失败，会由之前定义的GlobalExceptionHandler统一处理
        categoryService.add(category);
        return Result.success();
    }

    /**
     * 获取文章分类列表（分页）
     pageNum  当前页码, 默认为1
     pageSize 每页条数, 默认为5
     */
    @GetMapping
    public Result<PageBean<Category>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        PageBean<Category> pageBean = categoryService.list(pageNum, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据ID获取分类详情
     id 分类ID
     */
    @GetMapping("/{id}")
    public Result<Category> detail(@PathVariable Long id) {
        try {
            Category category = categoryService.findById(id);
            if (category == null) {
                return Result.error("查询的分类不存在");
            }
            return Result.success(category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新文章分类 (仅管理员)
     category 包含分类ID、名称和别名的JSON对象
     */
    @PutMapping
    public Result update(@RequestBody @Validated Category category) {
        try {
            categoryService.update(category);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID删除文章分类 (仅管理员)
     id 要删除的分类ID
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}