package com.muzi.quitsmoking.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 帖子详情VO，扩展自PostVO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostDetailVO extends PostVO {
    private static final long serialVersionUID = 1L;

    /**
     * 热门评论列表
     */
    private List<CommentVO> hotComments;
} 