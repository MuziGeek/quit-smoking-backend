package com.muzi.quitsmoking.utils;

import com.muzi.quitsmoking.exception.BusinessException;
import com.muzi.quitsmoking.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 安全工具类
 * 主要用于处理用户认证和授权相关功能
 */
@Slf4j
public class SecurityUtils {

    /**
     * 用户ID存储在请求属性中的KEY
     */
    private static final String USER_ID_KEY = "currentUserId";

    /**
     * 用户Token存储在请求头中的KEY
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
    public static Long getCurrentUserId() {
        Long userId = (Long) getRequestAttributes().getAttribute(USER_ID_KEY, RequestAttributes.SCOPE_REQUEST);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        return userId;
    }

    /**
     * 获取当前用户ID（不抛异常，仅返回null）
     * @return 当前用户ID，未登录时返回null
     */
    public static Long getCurrentUserIdOrNull() {
        try {
            return (Long) getRequestAttributes().getAttribute(USER_ID_KEY, RequestAttributes.SCOPE_REQUEST);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置当前用户ID
     * @param userId 用户ID
     */
    public static void setCurrentUserId(Long userId) {
        getRequestAttributes().setAttribute(USER_ID_KEY, userId, RequestAttributes.SCOPE_REQUEST);
        log.debug("设置当前用户ID: {}", userId);
    }

    /**
     * 清除当前用户ID
     */
    public static void clearCurrentUserId() {
        getRequestAttributes().removeAttribute(USER_ID_KEY, RequestAttributes.SCOPE_REQUEST);
        log.debug("清除当前用户ID");
    }

    /**
     * 获取Token
     * @return 用户Token，不包含前缀
     */
    public static String getToken() {
        String token = getRequest().getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        }
        return token;
    }

    /**
     * 判断当前是否已登录
     * @return 是否已登录
     */
    public static boolean isAuthenticated() {
        return getCurrentUserIdOrNull() != null;
    }

    /**
     * 获取请求属性
     * @return 请求属性
     */
    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null || !(attributes instanceof ServletRequestAttributes)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取请求上下文失败");
        }
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取请求对象
     * @return 请求对象
     */
    private static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }
} 