package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 健康状态VO
 */
@Data
public class HealthStatusVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前进度百分比
     */
    private Integer currentProgressPercent;

    /**
     * 下一个健康里程碑
     */
    private HealthMilestoneVO nextMilestone;

    /**
     * 已达成的健康里程碑
     */
    private List<HealthMilestoneVO> achievedMilestones;

    @Data
    public static class HealthMilestoneVO {
        /**
         * 里程碑ID
         */
        private Long id;

        /**
         * 里程碑标题
         */
        private String title;

        /**
         * 里程碑描述
         */
        private String description;

        /**
         * 所需天数
         */
        private Integer requiredDays;

        /**
         * 是否已达成
         */
        private Boolean achieved;

        /**
         * 达成百分比
         */
        private Integer achievedPercent;
    }
}