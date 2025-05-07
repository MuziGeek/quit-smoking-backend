package com.muzi.quitsmoking.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求DTO
 */
@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
} 