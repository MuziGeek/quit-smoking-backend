package com.muzi.quitsmoking.model.constants;

/**
 * 角色常量类
 * 定义系统中使用的所有角色标识
 */
public class RoleConstants {

    /**
     * 超级管理员
     */
    public static final String SUPER_ADMIN = "super_admin";
    
    /**
     * 管理员
     */
    public static final String ADMIN = "admin";
    
    /**
     * 运营人员
     */
    public static final String OPERATOR = "operator";
    
    /**
     * 内容编辑
     */
    public static final String EDITOR = "editor";
    
    /**
     * 普通用户
     */
    public static final String USER = "user";
    
    /**
     * VIP用户
     */
    public static final String VIP = "vip";
    
    /**
     * 游客
     */
    public static final String GUEST = "guest";
    
    /**
     * 私有构造方法，防止实例化
     */
    private RoleConstants() {
        throw new IllegalStateException("Utility class");
    }
} 