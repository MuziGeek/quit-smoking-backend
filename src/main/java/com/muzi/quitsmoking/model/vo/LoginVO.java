package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应VO
 */
@Data
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户token
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 是否新用户
     */
    private Boolean isNewUser;
} 