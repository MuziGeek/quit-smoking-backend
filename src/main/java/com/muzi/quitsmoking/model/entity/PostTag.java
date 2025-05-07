package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 帖子标签表
 * @TableName post_tag
 */
@TableName(value ="post_tag")
@Data
public class PostTag {
    /**
     * 标签ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer tag_id;

    /**
     * 标签名称
     */
    private String tag_name;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 标签图标
     */
    private String icon;

    /**
     * 使用该标签的次数
     */
    private Integer use_count;

    /**
     * 是否热门：0-否，1-是
     */
    private Integer is_hot;

    /**
     * 创建时间
     */
    private Date created_at;
}