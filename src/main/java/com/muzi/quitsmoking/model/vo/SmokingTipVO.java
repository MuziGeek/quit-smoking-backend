package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 戒烟建议VO
 */
@Data
public class SmokingTipVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 建议ID
     */
    private Long id;

    /**
     * 建议标题
     */
    private String title;

    /**
     * 建议内容
     */
    private String content;

    /**
     * 建议分类
     */
    private String category;

    /**
     * 适用阶段：early-初期，middle-中期，later-后期
     */
    private String stage;
} 