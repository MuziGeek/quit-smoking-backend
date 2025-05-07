package com.muzi.quitsmoking.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 验证码请求DTO
 */
@Data
public class VerifyCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *, 手机号码
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
} 