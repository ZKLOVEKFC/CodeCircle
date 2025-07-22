package com.kz.coderscircle.service;

import com.kz.coderscircle.pojo.Category;
import com.kz.coderscircle.pojo.PageBean;
import org.springframework.security.access.prepost.PreAuthorize; // 引入注解

public interface CategoryService {

    @PreAuthorize("hasRole('ADMIN')")
    void add(Category category);

    PageBean<Category> list(Integer pageNum, Integer pageSize);

    Category findById(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    void update(Category category);

    @PreAuthorize("hasRole('ADMIN')")
    void delete(Long id);
}