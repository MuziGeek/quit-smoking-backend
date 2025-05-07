package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 打卡记录VO
 */
@Data
public class CheckinVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 打卡ID
     */
    private Long id;

    /**
     * 当日感受描述
     */
    private String feeling;

    /**
     * 当日心情：1-非常好，2-好，3-一般，4-差，5-非常差
     */
    private Integer mood;

    /**
     * 当日烟瘾程度：1-无，2-轻微，3-中等，4-强烈，5-非常强烈
     */
    private Integer cravingLevel;

    /**
     * 打卡日期
     */
    private LocalDateTime checkinTime;
} 