package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 深呼吸练习VO
 */
@Data
public class BreathingExerciseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 练习ID
     */
    private Long id;

    /**
     * 练习名称
     */
    private String name;

    /**
     * 练习描述
     */
    private String description;

    /**
     * 练习步骤
     */
    private String steps;

    /**
     * 练习时长（分钟）
     */
    private Integer duration;

    /**
     * 练习难度：1-简单，2-中等，3-困难
     */
    private Integer difficulty;

    /**
     * 练习图片
     */
    private String image;
} 