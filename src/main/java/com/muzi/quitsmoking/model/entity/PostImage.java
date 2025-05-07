package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 帖子图片表
 * @TableName post_image
 */
@TableName(value ="post_image")
@Data
public class PostImage {
    /**
     * 图片ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer image_id;

    /**
     * 关联的帖子ID
     */
    private Integer post_id;

    /**
     * 图片URL
     */
    private String image_url;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 图片顺序
     */
    private Integer order_num;

    /**
     * 上传时间
     */
    private Date created_at;
}