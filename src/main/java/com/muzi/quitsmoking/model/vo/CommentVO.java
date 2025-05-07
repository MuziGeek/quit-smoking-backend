package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论VO
 */
@Data
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论用户昵称
     */
    private String nickname;

    /**
     * 评论用户头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否点赞
     */
    private Boolean isLiked;

    /**
     * 回复列表
     */
    private List<ReplyVO> replies;

    /**
     * 回复数量
     */
    private Integer replyCount;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 回复VO
     */
    @Data
    public static class ReplyVO implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 回复ID
         */
        private Long id;

        /**
         * 回复用户ID
         */
        private Long userId;

        /**
         * 回复用户昵称
         */
        private String nickname;

        /**
         * 回复用户头像
         */
        private String avatar;

        /**
         * 回复内容
         */
        private String content;

        /**
         * 被回复用户ID
         */
        private Long replyToUserId;

        /**
         * 被回复用户昵称
         */
        private String replyToNickname;

        /**
         * 回复时间
         */
        private LocalDateTime createTime;
    }
} 