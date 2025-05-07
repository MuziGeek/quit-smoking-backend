package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户积分记录表
 * @TableName points_record
 */
@TableName(value ="points_record")
@Data
public class PointsRecord {
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
     * 积分变化值，正数表示增加，负数表示减少
     */
    private Integer points;

    /**
     * 积分变化原因
     */
    private String reason;

    /**
     * 变化后的总积分
     */
    private Integer current_points;

    /**
     * 创建时间
     */
    private Date created_at;
}