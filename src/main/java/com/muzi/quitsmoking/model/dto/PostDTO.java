package com.muzi.quitsmoking.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

/**
 * 发布帖子DTO
 */
@Data
public class PostDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    @Size(max = 500, message = "帖子内容不能超过500字")
    private String content;

    /**
     * 图片列表
     */
    private List<String> images;

    /**
     * 话题标签
     */
    private List<String> tags;

    /**
     * 所属小组ID
     */
    private Long groupId;
} 