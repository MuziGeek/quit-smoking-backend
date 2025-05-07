package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息响应VO
 */
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 开始戒烟日期
     */
    private LocalDate quitDate;
    
    /**
     * 戒烟天数
     */
    private Integer quitDays;

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

    /**
     * 节省金额
     */
    private Double savedMoney;

    /**
     * 未吸香烟数量
     */
    private Integer nonSmokingCount;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;
} 