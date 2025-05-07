package com.muzi.quitsmoking.annotation;

import java.lang.annotation.*;

/**
 * 登录要求注解
 * 用于标注需要登录才能访问的方法或类
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
    
    /**
     * 是否强制要求登录
     * @return true-要求登录，false-不要求登录
     */
    boolean value() default true;
    
    /**
     * 未登录时的错误消息
     * @return 错误消息
     */
    String message() default "请先登录";
}