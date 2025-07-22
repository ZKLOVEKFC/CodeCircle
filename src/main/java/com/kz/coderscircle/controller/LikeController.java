package com.kz.coderscircle.controller;

import com.kz.coderscircle.pojo.Result;
import com.kz.coderscircle.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 对文章进行点赞或取消点赞
     id 被操作的文章ID
     */
    @PostMapping("/article/{id}")
    public Result toggleArticleLike(@PathVariable("id") Long id) {
        likeService.toggleLike(id);
        return Result.success();
    }
}