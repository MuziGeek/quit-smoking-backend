package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 戒烟工具表，包含各种辅助戒烟的工具
 * @TableName tool
 */
@TableName(value ="tool")
@Data
public class Tool {
    /**
     * 工具ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer tool_id;

    /**
     * 工具名称
     */
    private String name;

    /**
     * 工具描述
     */
    private String description;

    /**
     * 工具图标
     */
    private String icon;

    /**
     * 工具类型：breathing-深呼吸练习，tips-戒烟建议，emergency-紧急求助，etc
     */
    private String type;

    /**
     * 工具内容，JSON格式存储
     */
    private String content;

    /**
     * 排序号
     */
    private Integer order_num;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}