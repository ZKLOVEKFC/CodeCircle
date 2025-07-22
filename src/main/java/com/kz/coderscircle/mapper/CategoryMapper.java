package com.kz.coderscircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kz.coderscircle.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}