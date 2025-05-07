package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户每日打卡记录表
 * @TableName checkin
 */
@TableName(value ="checkin")
@Data
public class Checkin {
    /**
     * 打卡ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer checkin_id;

    /**
     * 用户ID，关联user表
     */
    private Integer user_id;

    /**
     * 打卡日期
     */
    private Date checkin_date;

    /**
     * 打卡状态：success-成功，tempted-有烟瘾但忍住了，failed-没忍住吸烟了
     */
    private String status;

    /**
     * 烟瘾强度：1-5级
     */
    private Integer craving_level;

    /**
     * 吸烟数量，仅status为failed时有效
     */
    private Integer smoked_count;

    /**
     * 心情：happy-开心，normal-平静，sad-难过，anxious-焦虑，angry-愤怒
     */
    private String mood;

    /**
     * 打卡笔记，记录当天的感受
     */
    private String note;

    /**
     * 打卡创建时间
     */
    private Date created_at;
}