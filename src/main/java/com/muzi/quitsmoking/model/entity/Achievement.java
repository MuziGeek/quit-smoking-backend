package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 成就系统表，定义所有可获得的成就
 * @TableName achievement
 */
@TableName(value ="achievement")
@Data
public class Achievement {
    /**
     * 成就ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer achievement_id;

    /**
     * 成就标题
     */
    private String title;

    /**
     * 成就描述
     */
    private String description;

    /**
     * 成就图标
     */
    private String icon;

    /**
     * 成就类型：time-时间相关，health-健康相关，money-金钱相关，social-社交相关
     */
    private String type;

    /**
     * 条件类型：days-戒烟天数，saves-节省金额，posts-发帖数，etc
     */
    private String condition_type;

    /**
     * 达成条件值
     */
    private Integer condition_value;

    /**
     * 获得此成就奖励的积分
     */
    private Integer points;

    /**
     * 成就等级，数字越大越高级
     */
    private Integer level;

    /**
     * 是否隐藏成就：0-显示，1-隐藏直到达成
     */
    private Integer is_hidden;
}