package com.kz.coderscircle.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章实体类，对应数据库中的article表
 */
@Data
@TableName("article")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    @TableField("cover_img")
    private String coverImg;

    private String state;

    @TableField("category_id")
    private Long categoryId;

    @TableField("create_user")
    private Long createUser;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    private Integer likeCount;  //被点赞计数，对应表中like_count

    @TableField(exist = false) // 声明此字段在数据库表中不存在，记录当前用户是否点赞了此帖
    private Boolean isLiked;
}