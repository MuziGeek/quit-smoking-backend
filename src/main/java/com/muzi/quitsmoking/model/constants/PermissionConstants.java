package com.muzi.quitsmoking.model.constants;

/**
 * 权限常量类
 * 定义系统中使用的所有权限标识
 */
public class PermissionConstants {

    /**
     * 用户管理权限
     */
    public static final String USER_ADD = "user:add";
    public static final String USER_EDIT = "user:edit";
    public static final String USER_DELETE = "user:delete";
    public static final String USER_VIEW = "user:view";
    public static final String USER_EXPORT = "user:export";
    
    /**
     * 内容管理权限
     */
    public static final String CONTENT_ADD = "content:add";
    public static final String CONTENT_EDIT = "content:edit";
    public static final String CONTENT_DELETE = "content:delete";
    public static final String CONTENT_VIEW = "content:view";
    public static final String CONTENT_PUBLISH = "content:publish";
    
    /**
     * 社区管理权限
     */
    public static final String COMMUNITY_POST_ADD = "community:post:add";
    public static final String COMMUNITY_POST_EDIT = "community:post:edit";
    public static final String COMMUNITY_POST_DELETE = "community:post:delete";
    public static final String COMMUNITY_COMMENT_ADD = "community:comment:add";
    public static final String COMMUNITY_COMMENT_DELETE = "community:comment:delete";
    public static final String COMMUNITY_LIKE = "community:like";
    
    /**
     * 系统管理权限
     */
    public static final String SYSTEM_CONFIG = "system:config";
    public static final String SYSTEM_LOG = "system:log";
    public static final String SYSTEM_BACKUP = "system:backup";
    
    /**
     * 超级管理员权限
     */
    public static final String ADMIN = "admin";
    
    /**
     * 私有构造方法，防止实例化
     */
    private PermissionConstants() {
        throw new IllegalStateException("Utility class");
    }
} 