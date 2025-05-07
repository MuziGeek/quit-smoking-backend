package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 评论表
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment {
    /**
     * 评论ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer comment_id;

    /**
     * 所属帖子ID
     */
    private Integer post_id;

    /**
     * 评论用户ID
     */
    private Integer user_id;

    /**
     * 父评论ID，回复某条评论时使用
     */
    private Integer parent_id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer like_count;

    /**
     * 是否匿名：0-否，1-是
     */
    private Integer is_anonymous;

    /**
     * 状态：0-删除，1-正常
     */
    private Integer status;

    /**
     * 评论时间
     */
    private Date created_at;
}