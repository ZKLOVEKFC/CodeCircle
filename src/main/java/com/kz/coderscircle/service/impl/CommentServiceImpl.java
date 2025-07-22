package com.kz.coderscircle.service.impl;

import com.kz.coderscircle.mapper.CommentMapper;
import com.kz.coderscircle.pojo.Comment;
import com.kz.coderscircle.pojo.User;
import com.kz.coderscircle.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void add(Comment comment) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setCreateUser(loginUser.getId());
        comment.setCreateTime(LocalDateTime.now());
        commentMapper.insert(comment);
    }

    @Override
    public List<Comment> list(Long articleId) {
        return commentMapper.selectWithUserInfo(articleId);
    }

    public Comment findById(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null)
            throw new RuntimeException("评论不存在");
        return comment;
    }

    @Override
    public void delete(Long id) {
        commentMapper.deleteById(id);
    }
}