package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 紧急求助记录表，记录用户烟瘾发作的求助情况
 * @TableName emergency_help
 */
@TableName(value ="emergency_help")
@Data
public class EmergencyHelp {
    /**
     * 求助ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer help_id;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 求助时间
     */
    private Date help_time;

    /**
     * 烟瘾级别：1-5
     */
    private Integer craving_level;

    /**
     * 情绪状态
     */
    private String emotion;

    /**
     * 诱发因素
     */
    private String trigger;

    /**
     * 采用的解决方法
     */
    private String solution_used;

    /**
     * 结果：0-失败，1-成功
     */
    private Integer result;

    /**
     * 描述
     */
    private String description;

    /**
     * 紧急程度：1-轻微，2-中等，3-严重
     */
    private Integer severity;

    /**
     * 处理方式：1-等待，2-求助，3-其他
     */
    private Integer handle_type;

    /**
     * 状态：0-删除，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date created_at;
}