package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户戒烟记录表
 * @TableName smoking_record
 */
@TableName(value ="smoking_record")
@Data
public class SmokingRecord {
    /**
     * 记录ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer record_id;

    /**
     * 用户ID，关联user表
     */
    private Integer user_id;

    /**
     * 戒烟开始日期
     */
    private Date start_date;

    /**
     * 之前每天吸烟支数
     */
    private Integer cigarette_per_day;

    /**
     * 每包香烟价格
     */
    private BigDecimal price_per_pack;

    /**
     * 每包香烟支数
     */
    private Integer cigarettes_per_pack;

    /**
     * 戒烟目标天数
     */
    private Integer goal_days;

    /**
     * 当前目标天数
     */
    private Integer current_goal;

    /**
     * 当前已戒烟天数
     */
    private Integer current_days;

    /**
     * 最长连续戒烟天数
     */
    private Integer longest_streak;

    /**
     * 累计节省金额
     */
    private BigDecimal total_money_saved;

    /**
     * 累计未吸香烟数量
     */
    private Integer total_cigarettes_avoided;

    /**
     * 健康恢复百分比
     */
    private Integer health_percent;

    /**
     * 戒烟状态：0-失败，1-进行中，2-已完成
     */
    private Integer status;

    /**
     * 记录创建时间
     */
    private Date created_at;

    /**
     * 记录更新时间
     */
    private Date updated_at;
}