package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 话题VO
 */
@Data
public class TopicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 话题ID
     */
    private Long id;

    /**
     * 话题名称
     */
    private String name;

    /**
     * 话题描述
     */
    private String description;

    /**
     * 话题图标
     */
    private String icon;

    /**
     * 使用次数
     */
    private Integer useCount;

    /**
     * 是否热门
     */
    private Boolean isHot;
} 