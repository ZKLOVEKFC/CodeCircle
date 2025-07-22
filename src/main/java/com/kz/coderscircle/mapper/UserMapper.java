package com.kz.coderscircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kz.coderscircle.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}