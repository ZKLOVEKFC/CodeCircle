package com.kz.coderscircle.service;

import com.kz.coderscircle.pojo.Comment;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CommentService {

    void add(Comment comment);

    List<Comment> list(Long articleId);

    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "@commentServiceImpl.findById(#id).createUser == authentication.principal.id or " +
            "@articleServiceImpl.findById(@commentServiceImpl.findById(#id).articleId).createUser == authentication.principal.id")
    void delete(Long id);
}