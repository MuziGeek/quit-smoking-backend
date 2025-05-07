package com.muzi.quitsmoking.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 紧急求助请求DTO
 */
@Data
public class EmergencyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 紧急情况描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 紧急程度：1-轻微，2-中等，3-严重
     */
    private Integer severity;

    /**
     * 处理方式：1-自行处理，2-联系亲友，3-专业帮助
     */
    private Integer handleType;
} 