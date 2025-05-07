package com.muzi.quitsmoking.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息更新DTO
 */
@Data
public class UserInfoUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 开始戒烟日期
     */
    private LocalDate quitDate;

    /**
     * 吸烟年数
     */
    private Integer smokingYears;

    /**
     * 每日吸烟量
     */
    private Integer cigarettesPerDay;

    /**
     * 每包香烟价格
     */
    private Double pricePerPack;

    /**
     * 每包香烟数量
     */
    private Integer cigarettesPerPack;
} 