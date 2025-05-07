package com.muzi.quitsmoking.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 创建小组DTO
 */
@Data
public class GroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 小组名称
     */
    @NotBlank(message = "小组名称不能为空")
    @Size(max = 20, message = "小组名称不能超过20字")
    private String name;

    /**
     * 小组描述
     */
    @NotBlank(message = "小组描述不能为空")
    @Size(max = 200, message = "小组描述不能超过200字")
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
     * 小组logo
     */
    private String logo;
    /**
     * 小组分类id
     */
    private Integer categoryId;
} 