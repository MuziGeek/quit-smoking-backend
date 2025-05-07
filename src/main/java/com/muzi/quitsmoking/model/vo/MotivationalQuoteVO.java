package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 激励名言VO
 */
@Data
public class MotivationalQuoteVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 名言ID
     */
    private Long id;

    /**
     * 名言内容
     */
    private String content;

    /**
     * 名言作者
     */
    private String author;

    /**
     * 名言背景图
     */
    private String backgroundImage;
} 