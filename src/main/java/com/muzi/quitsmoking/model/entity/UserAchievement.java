package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户获得的成就记录表
 * @TableName user_achievement
 */
@TableName(value ="user_achievement")
@Data
public class UserAchievement {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 成就ID
     */
    private Integer achievement_id;

    /**
     * 获得成就的时间
     */
    private Date achieved_time;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Integer is_read;
}