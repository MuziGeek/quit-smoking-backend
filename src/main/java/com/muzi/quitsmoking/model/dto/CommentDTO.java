package com.muzi.quitsmoking.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 发表评论DTO
 */
@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 200, message = "评论内容不能超过200字")
    private String content;

    /**
     * 回复的评论ID（如果是回复评论）
     */
    private Long replyToCommentId;

    /**
     * 回复的用户ID（如果是回复用户）
     */
    private Long replyToUserId;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 是否匿名
     */
    private Boolean isAnonymous;
}