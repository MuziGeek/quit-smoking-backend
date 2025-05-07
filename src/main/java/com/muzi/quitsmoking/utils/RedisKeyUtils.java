package com.muzi.quitsmoking.utils;

/**
 * Redis键工具类
 * 用于生成规范化的Redis键
 */
public class RedisKeyUtils {

    /**
     * 项目名称前缀
     */
    private static final String PROJECT_PREFIX = "quit_smoking:";
    
    /**
     * 用户相关前缀
     */
    private static final String USER_PREFIX = PROJECT_PREFIX + "user:";
    
    /**
     * 验证码前缀
     */
    private static final String VERIFY_CODE_PREFIX = USER_PREFIX + "verify_code:";
    
    /**
     * 用户Token前缀
     */
    private static final String USER_TOKEN_PREFIX = USER_PREFIX + "token:";
    
    /**
     * 用户权限前缀
     */
    private static final String USER_PERMISSION_PREFIX = USER_PREFIX + "permission:";
    
    /**
     * 用户角色前缀
     */
    private static final String USER_ROLE_PREFIX = USER_PREFIX + "role:";
    
    /**
     * 社区相关前缀
     */
    private static final String COMMUNITY_PREFIX = PROJECT_PREFIX + "community:";
    
    /**
     * 文章相关前缀
     */
    private static final String ARTICLE_PREFIX = PROJECT_PREFIX + "article:";
    
    /**
     * 系统配置前缀
     */
    private static final String SYSTEM_CONFIG_PREFIX = PROJECT_PREFIX + "config:";
    
    /**
     * 获取验证码的键
     * @param phone 手机号
     * @return Redis键
     */
    public static String getVerifyCodeKey(String phone) {
        return VERIFY_CODE_PREFIX + phone;
    }
    
    /**
     * 获取用户Token的键
     * @param userId 用户ID
     * @return Redis键
     */
    public static String getUserTokenKey(Long userId) {
        return USER_TOKEN_PREFIX + userId;
    }
    
    /**
     * 获取Token黑名单的键
     * @param token Token
     * @return Redis键
     */
    public static String getTokenBlacklistKey(String token) {
        return USER_TOKEN_PREFIX + "blacklist:" + token;
    }
    
    /**
     * 获取用户权限的键
     * @param userId 用户ID
     * @return Redis键
     */
    public static String getUserPermissionKey(Long userId) {
        return USER_PERMISSION_PREFIX + userId;
    }
    
    /**
     * 获取用户角色的键
     * @param userId 用户ID
     * @return Redis键
     */
    public static String getUserRoleKey(Long userId) {
        return USER_ROLE_PREFIX + userId;
    }
    
    /**
     * 获取文章缓存的键
     * @param articleId 文章ID
     * @return Redis键
     */
    public static String getArticleKey(Long articleId) {
        return ARTICLE_PREFIX + articleId;
    }
    
    /**
     * 获取文章阅读量的键
     * @param articleId 文章ID
     * @return Redis键
     */
    public static String getArticleViewKey(Long articleId) {
        return ARTICLE_PREFIX + "view:" + articleId;
    }
    
    /**
     * 获取系统配置的键
     * @param configKey 配置键
     * @return Redis键
     */
    public static String getSystemConfigKey(String configKey) {
        return SYSTEM_CONFIG_PREFIX + configKey;
    }
    
    /**
     * 私有构造方法，防止实例化
     */
    private RedisKeyUtils() {
        throw new IllegalStateException("Utility class");
    }
} 