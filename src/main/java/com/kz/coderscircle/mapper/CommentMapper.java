package com.kz.coderscircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kz.coderscircle.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectWithUserInfo(Long articleId);
}