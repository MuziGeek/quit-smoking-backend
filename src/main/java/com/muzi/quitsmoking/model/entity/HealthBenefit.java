package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 健康时间线收益表，记录每个时间点的具体健康收益
 * @TableName health_benefit
 */
@TableName(value ="health_benefit")
@Data
public class HealthBenefit {
    /**
     * 收益ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer benefit_id;

    /**
     * 关联的时间线ID
     */
    private Integer timeline_id;

    /**
     * 健康收益描述文本
     */
    private String benefit_text;

    /**
     * 显示顺序
     */
    private Integer order_num;
}