package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 点赞记录表
 * @TableName like_record
 */
@TableName(value ="like_record")
@Data
public class LikeRecord {
    /**
     * 点赞ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer like_id;

    /**
     * 点赞用户ID
     */
    private Integer user_id;

    /**
     * 点赞目标ID（帖子ID或评论ID）
     */
    private Integer target_id;

    /**
     * 点赞目标类型：post-帖子，comment-评论
     */
    private String type;

    /**
     * 点赞时间
     */
    private Date created_at;
}