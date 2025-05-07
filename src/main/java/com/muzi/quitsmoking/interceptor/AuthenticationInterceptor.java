package com.muzi.quitsmoking.interceptor;

import com.muzi.quitsmoking.exception.BusinessException;
import com.muzi.quitsmoking.exception.ErrorCode;
import com.muzi.quitsmoking.service.TokenService;
import com.muzi.quitsmoking.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * 认证拦截器
 * 用于拦截请求并验证用户身份
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取token
        String token = request.getHeader(SecurityUtils.TOKEN_HEADER);
        
        // 如果请求头中没有token，则尝试从请求参数中获取
        if (token == null || token.isEmpty()) {
            token = request.getParameter("token");
        }
        
        // 如果token为空，则抛出未授权异常
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未提供有效的身份凭证");
        }
        
        // 如果token以指定前缀开头，则去除前缀
        if (token.startsWith(SecurityUtils.TOKEN_PREFIX)) {
            token = token.substring(SecurityUtils.TOKEN_PREFIX.length());
        }
        
        // 从token中获取用户ID
        Long userId = tokenService.getUserIdFromToken(token);
        if (Objects.isNull(userId)) {
            throw new BusinessException(ErrorCode.USER_TOKEN_INVALID, "无效的身份凭证");
        }
        
        // 验证token是否已过期
        if (tokenService.isTokenExpired(token)) {
            throw new BusinessException(ErrorCode.USER_TOKEN_EXPIRED, "身份凭证已过期，请重新登录");
        }
        
        // 将用户ID存入请求属性
        SecurityUtils.setCurrentUserId(userId);
        
        // 放行请求
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除存储的用户ID
        SecurityUtils.clearCurrentUserId();
    }
} 