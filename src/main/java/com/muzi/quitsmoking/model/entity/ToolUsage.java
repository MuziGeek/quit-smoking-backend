package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户工具使用记录表
 * @TableName tool_usage
 */
@TableName(value ="tool_usage")
@Data
public class ToolUsage {
    /**
     * 使用记录ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer usage_id;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 工具ID
     */
    private Integer tool_id;

    /**
     * 使用时间
     */
    private Date usage_time;

    /**
     * 使用时长（秒）
     */
    private Integer duration;

    /**
     * 使用反馈：1-有用，0-无用
     */
    private Integer feedback;
}