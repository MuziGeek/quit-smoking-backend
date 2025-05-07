package com.muzi.quitsmoking.service;

/**
 * Token服务接口
 * 提供Token的管理功能，包括生成、验证、解析等
 */
public interface TokenService {

    /**
     * 生成Token
     * @param userId 用户ID
     * @return Token
     */
    String generateToken(Long userId);

    /**
     * 从Token中获取用户ID
     * @param token Token
     * @return 用户ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 验证Token是否有效
     * @param token Token
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 刷新Token
     * @param token 旧Token
     * @return 新Token
     */
    String refreshToken(String token);

    /**
     * 使Token失效
     * @param token Token
     */
    void invalidateToken(String token);

    /**
     * 检查Token是否已过期
     * @param token Token
     * @return 是否已过期
     */
    boolean isTokenExpired(String token);
} 