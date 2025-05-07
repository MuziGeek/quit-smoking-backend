package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 帖子与标签的关联表
 * @TableName post_tag_relation
 */
@TableName(value ="post_tag_relation")
@Data
public class PostTagRelation {
    /**
     * 关联ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer relation_id;

    /**
     * 帖子ID
     */
    private Integer post_id;

    /**
     * 标签ID
     */
    private Integer tag_id;

    /**
     * 创建时间
     */
    private Date created_at;
}