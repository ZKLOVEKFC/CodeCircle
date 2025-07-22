package com.kz.coderscircle.service;

public interface LikeService {
    /**
     * 切换文章的点赞状态（点赞/取消点赞）
     articleId 被操作的文章ID
     */
    void toggleLike(Long articleId);
}