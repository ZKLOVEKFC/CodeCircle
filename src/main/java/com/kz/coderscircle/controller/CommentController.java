package com.kz.coderscircle.controller;

import com.kz.coderscircle.pojo.Comment;
import com.kz.coderscircle.pojo.Result;
import com.kz.coderscircle.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result add(@RequestBody @Validated Comment comment) {
        commentService.add(comment);
        return Result.success();
    }

    @GetMapping
    public Result<List<Comment>> list(@RequestParam Long articleId) {
        List<Comment> list = commentService.list(articleId);
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            commentService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}