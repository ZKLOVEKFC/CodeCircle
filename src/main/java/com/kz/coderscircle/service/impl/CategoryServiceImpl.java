package com.kz.coderscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kz.coderscircle.mapper.CategoryMapper;
import com.kz.coderscircle.pojo.Category;
import com.kz.coderscircle.pojo.PageBean;
import com.kz.coderscircle.pojo.User;
import com.kz.coderscircle.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(Category category) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        category.setCreateUser(loginUser.getId());
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(category);
    }

    @Override
    public PageBean<Category> list(Integer pageNum, Integer pageSize) {
        Page<Category> page = Page.of(pageNum, pageSize);
        categoryMapper.selectPage(page, new LambdaQueryWrapper<Category>().orderByDesc(Category::getCreateTime));
        return new PageBean<>(page.getTotal(), page.getRecords());
    }

    @Override
    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}