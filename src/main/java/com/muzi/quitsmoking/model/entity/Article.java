package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 戒烟资讯文章表
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article {
    /**
     * 文章ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer article_id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容，支持HTML格式
     */
    private String content;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源
     */
    private String source;

    /**
     * 分类：knowledge-知识，experience-经验，research-研究，news-新闻
     */
    private String category;

    /**
     * 阅读次数
     */
    private Integer view_count;

    /**
     * 点赞数
     */
    private Integer like_count;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer is_top;

    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date created_at;

    /**
     * 更新时间
     */
    private Date updated_at;
}