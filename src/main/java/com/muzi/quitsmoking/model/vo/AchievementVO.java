package com.muzi.quitsmoking.model.vo;

import lombok.Data;
import java.util.Date;

/**
 * 成就视图对象
 * 用于向前端展示成就相关信息
 * @author muzi
 */
@Data
public class AchievementVO {
    /**
     * 成就ID
     */
    private Integer achievementId;
    
    /**
     * 成就标题
     */
    private String title;
    
    /**
     * 成就描述
     */
    private String description;
    
    /**
     * 成就图标URL
     */
    private String icon;
    
    /**
     * 成就类型：time-时间相关，health-健康相关，money-金钱相关，social-社交相关
     */
    private String type;
    
    /**
     * 条件类型：days-戒烟天数，saves-节省金额，posts-发帖数，etc
     */
    private String conditionType;
    
    /**
     * 达成条件值
     */
    private Integer conditionValue;
    
    /**
     * 获得此成就奖励的积分
     */
    private Integer points;
    
    /**
     * 成就等级，数字越大越高级
     */
    private Integer level;
    
    /**
     * 用户是否已获得此成就
     */
    private Boolean achieved;
    
    /**
     * 达成时间（如果已获得）
     */
    private Date achievedAt;
    
    /**
     * 是否已读（如果已获得）
     */
    private Boolean isRead;
    
    /**
     * 当前进度百分比（0-100）
     */
    private Integer progress;
    
    /**
     * 当前进度值
     */
    private Integer currentValue;
} 