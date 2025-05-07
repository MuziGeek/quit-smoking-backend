package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 社区帖子VO
 */
@Data
public class PostVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 作者ID
     */
    private Long userId;

    /**
     * 作者昵称
     */
    private String nickname;

    /**
     * 作者头像
     */
    private String avatar;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 图片列表
     */
    private List<String> images;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 是否点赞
     */
    private Boolean isLiked;

    /**
     * 话题标签
     */
    private List<String> tags;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;
} 