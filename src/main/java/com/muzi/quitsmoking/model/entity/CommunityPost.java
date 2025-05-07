package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 社区帖子表
 * @TableName community_post
 */
@TableName(value ="community_post")
@Data
public class CommunityPost {
    /**
     * 帖子ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer post_id;

    /**
     * 发帖用户ID
     */
    private Integer user_id;

    /**
     * 所属小组ID，NULL表示不属于任何小组
     */
    private Integer group_id;

    /**
     * 帖子标题，可为空
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子类型：normal-普通，question-问题，share-分享
     */
    private String post_type;

    /**
     * 状态：0-删除，1-正常，2-置顶
     */
    private Integer status;

    /**
     * 浏览次数
     */
    private Integer view_count;

    /**
     * 点赞数
     */
    private Integer like_count;

    /**
     * 评论数
     */
    private Integer comment_count;

    /**
     * 是否匿名：0-否，1-是
     */
    private Integer is_anonymous;

    /**
     * 发布时间
     */
    private Date created_at;

    /**
     * 更新时间
     */
    private Date updated_at;
}