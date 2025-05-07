package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 戒烟统计数据VO
 */
@Data
public class SmokingStatsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 戒烟天数
     */
    private Integer quitDays;

    /**
     * 戒烟开始日期
     */
    private LocalDate quitDate;

    /**
     * 节省金额
     */
    private Double savedMoney;

    /**
     * 未吸香烟数量
     */
    private Integer nonSmokingCount;

    /**
     * 增加寿命(分钟)
     */
    private Integer increasedLifeMinutes;

    /**
     * 连续打卡天数
     */
    private Integer consecutiveCheckinDays;

    /**
     * 今日是否已打卡
     */
    private Boolean isTodayCheckin;
} 