package com.muzi.quitsmoking.aop;

import com.muzi.quitsmoking.annotation.RequireLogin;
import com.muzi.quitsmoking.annotation.RequirePermission;
import com.muzi.quitsmoking.exception.BusinessException;
import com.muzi.quitsmoking.exception.ErrorCode;
import com.muzi.quitsmoking.service.PermissionService;
import com.muzi.quitsmoking.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 安全切面
 * 用于处理权限和登录检查
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {

    private final PermissionService permissionService;

    /**
     * 定义切点：所有标注了RequireLogin注解的方法
     */
    @Pointcut("@annotation(com.muzi.quitsmoking.annotation.RequireLogin)")
    public void loginPointcut() {
    }

    /**
     * 定义切点：所有标注了RequirePermission注解的方法
     */
    @Pointcut("@annotation(com.muzi.quitsmoking.annotation.RequirePermission)")
    public void permissionPointcut() {
    }

    /**
     * 在方法执行前检查登录状态
     * @param joinPoint 连接点
     */
    @Before("loginPointcut()")
    public void checkLogin(JoinPoint joinPoint) {
        try {
            // 获取当前用户ID，如果未登录会抛出异常
            SecurityUtils.getCurrentUserId();
        } catch (BusinessException e) {
            // 获取注解中的错误消息
            String message = getLoginAnnotationMessage(joinPoint);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, message);
        }
    }

    /**
     * 在方法执行前检查权限
     * @param joinPoint 连接点
     */
    @Before("permissionPointcut()")
    public void checkPermission(JoinPoint joinPoint) {
        // 获取当前用户ID
        Long userId = SecurityUtils.getCurrentUserId();
        
        // 获取方法上的RequirePermission注解
        RequirePermission annotation = getPermissionAnnotation(joinPoint);
        if (annotation == null) {
            return;
        }
        
        // 获取所需权限
        String[] permissions = annotation.value();
        boolean requireAll = annotation.requireAll();
        
        // 检查权限
        boolean hasPermission = false;
        if (requireAll) {
            // 需要所有权限
            hasPermission = permissionService.hasAllPermissions(userId, permissions);
        } else {
            // 需要任一权限
            hasPermission = permissionService.hasAnyPermission(userId, permissions);
        }
        
        // 如果没有权限，抛出异常
        if (!hasPermission) {
            throw new BusinessException(ErrorCode.FORBIDDEN, annotation.message());
        }
    }

    /**
     * 获取方法上的RequireLogin注解
     * @param joinPoint 连接点
     * @return RequireLogin注解
     */
    private String getLoginAnnotationMessage(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequireLogin annotation = method.getAnnotation(RequireLogin.class);
        return annotation.message();
    }

    /**
     * 获取方法上的RequirePermission注解
     * @param joinPoint 连接点
     * @return RequirePermission注解
     */
    private RequirePermission getPermissionAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(RequirePermission.class);
    }
}