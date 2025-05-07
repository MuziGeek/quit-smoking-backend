package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章VO
 */
@Data
public class ArticleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章作者
     */
    private String author;

    /**
     * 文章分类
     */
    private String category;

    /**
     * 文章封面图
     */
    private String coverImage;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 阅读量
     */
    private Integer readCount;

    /**
     * 点赞量
     */
    private Integer likeCount;
} 