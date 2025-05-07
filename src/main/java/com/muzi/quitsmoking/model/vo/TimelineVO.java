package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 时间线VO
 */
@Data
public class TimelineVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间线项列表
     */
    private List<TimelineItem> items;

    @Data
    public static class TimelineItem {
        /**
         * 日期
         */
        private LocalDate date;

        /**
         * 标题
         */
        private String title;

        /**
         * 描述
         */
        private String description;

        /**
         * 类型：1-健康里程碑，2-成就获得，3-打卡记录
         */
        private Integer type;

        /**
         * 图标
         */
        private String icon;
    }
}