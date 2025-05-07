package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统配置表
 * @TableName system_config
 */
@TableName(value ="system_config")
@Data
public class SystemConfig {
    /**
     * 配置ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer config_id;

    /**
     * 配置键
     */
    private String config_key;

    /**
     * 配置值
     */
    private String config_value;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date created_at;

    /**
     * 更新时间
     */
    private Date updated_at;
}