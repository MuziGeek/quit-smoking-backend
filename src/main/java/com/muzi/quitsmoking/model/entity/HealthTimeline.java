package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 戒烟健康时间线表，记录不同时间点的健康恢复情况
 * @TableName health_timeline
 */
@TableName(value ="health_timeline")
@Data
public class HealthTimeline {
    /**
     * 时间线ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer timeline_id;

    /**
     * 时间点，如"20分钟"、"8小时"、"1个月"等
     */
    private String time_point;

    /**
     * 时间数值，转换为小时计算，用于排序和判断
     */
    private Integer time_value;

    /**
     * 健康效果标题
     */
    private String title;

    /**
     * 详细描述
     */
    private String description;

    /**
     * 图标名称
     */
    private String icon;

    /**
     * 显示顺序
     */
    private Integer order_num;

    /**
     * 需要的天数
     */
    private Integer days_required;
}