package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 社区小组VO
 */
@Data
public class GroupVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 小组ID
     */
    private Long id;

    /**
     * 小组名称
     */
    private String name;

    /**
     * 小组描述
     */
    private String description;

    /**
     * 小组头像
     */
    private String avatar;

    /**
     * 小组封面
     */
    private String coverImage;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者昵称
     */
    private String creatorName;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 帖子数量
     */
    private Integer postCount;

    /**
     * 是否已加入
     */
    private Boolean isJoined;
    /**
     * 小组logo
     */
    private String  logo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最新帖子
     */
    private List<PostVO> RecentPosts;
} 