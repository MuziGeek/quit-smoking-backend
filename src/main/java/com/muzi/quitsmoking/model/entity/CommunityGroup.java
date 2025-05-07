package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 社区小组表
 * @TableName community_group
 */
@TableName(value ="community_group")
@Data
public class CommunityGroup {
    /**
     * 小组ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer group_id;

    /**
     * 小组名称
     */
    private String name;

    /**
     * 小组描述
     */
    private String description;

    /**
     * 小组图标URL
     */
    private String logo;

    /**
     * 小组封面图URL
     */
    private String cover_image;

    /**
     * 小组分类ID
     */
    private Integer category_id;

    /**
     * 创建者用户ID
     */
    private Integer creator_id;

    /**
     * 成员数量
     */
    private Integer member_count;

    /**
     * 帖子数量
     */
    private Integer post_count;

    /**
     * 状态：0-封禁，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date created_at;

    /**
     * 更新时间
     */
    private Date updated_at;
}