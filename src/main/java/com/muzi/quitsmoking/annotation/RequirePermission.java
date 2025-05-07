package com.muzi.quitsmoking.annotation;

import java.lang.annotation.*;

/**
 * 权限要求注解
 * 用于标注需要特定权限才能访问的方法或类
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    
    /**
     * 需要的权限列表，多个权限之间是OR关系
     * @return 权限列表
     */
    String[] value();
    
    /**
     * 权限之间的关系
     * @return true-需要同时具备所有权限(AND关系)，false-只需要具备任意一个权限(OR关系)
     */
    boolean requireAll() default false;
    
    /**
     * 无权限时的错误消息
     * @return 错误消息
     */
    String message() default "没有操作权限";
} 