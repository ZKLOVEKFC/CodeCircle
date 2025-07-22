package com.kz.coderscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kz.coderscircle.mapper.ArticleMapper;
import com.kz.coderscircle.mapper.ArticleLikeMapper;
import com.kz.coderscircle.pojo.ArticleLike;
import com.kz.coderscircle.pojo.User;
import com.kz.coderscircle.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private ArticleLikeMapper articleLikeMapper;
    @Autowired
    private ArticleMapper articleMapper;

    //切换文章的点赞状态，使用@Transactional注解确保操作的原子性。
    @Override
    @Transactional
    public void toggleLike(Long articleId) {
        //获取当前登录用户ID
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getId();

        //构造一个条件查询器，查询用户是否已经对该文章点过赞
        LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleLike::getUserId, userId);
        wrapper.eq(ArticleLike::getArticleId, articleId);
        ArticleLike likeRecord = articleLikeMapper.selectOne(wrapper);

        if (likeRecord == null) {
            //如果没有点过赞，则执行点赞操作
            ArticleLike newLike = new ArticleLike();
            newLike.setUserId(userId);
            newLike.setArticleId(articleId);
            articleLikeMapper.insert(newLike); // 在like表中插入记录

            // 更新文章表中的总点赞数
            articleMapper.incrementLikeCount(articleId);
        } else {
            //如果已经点过赞，则执行取消点赞操作
            articleLikeMapper.deleteById(likeRecord.getId()); // 从like表中删除记录

            // 更新文章表中的总点赞数
            articleMapper.decrementLikeCount(articleId);
        }
    }
}