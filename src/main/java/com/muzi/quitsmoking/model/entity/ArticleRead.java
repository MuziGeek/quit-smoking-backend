package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 文章阅读记录表
 * @TableName article_read
 */
@TableName(value ="article_read")
@Data
public class ArticleRead {
    /**
     * 阅读记录ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer read_id;

    /**
     * 文章ID
     */
    private Integer article_id;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 阅读时间
     */
    private Date read_time;

    /**
     * 是否点赞：0-否，1-是
     */
    private Integer is_liked;
}