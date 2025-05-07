package com.muzi.quitsmoking.service;

/**
 * 权限服务接口
 * 提供用户权限相关的功能
 */
public interface PermissionService {

    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户ID
     * @param permission 权限标识
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permission);

    /**
     * 检查用户是否拥有所有指定权限
     * @param userId 用户ID
     * @param permissions 权限标识数组
     * @return 是否有所有权限
     */
    boolean hasAllPermissions(Long userId, String[] permissions);

    /**
     * 检查用户是否拥有任一指定权限
     * @param userId 用户ID
     * @param permissions 权限标识数组
     * @return 是否有任一权限
     */
    boolean hasAnyPermission(Long userId, String[] permissions);

    /**
     * 为用户授予权限
     * @param userId 用户ID
     * @param permission 权限标识
     */
    void grantPermission(Long userId, String permission);

    /**
     * 撤销用户权限
     * @param userId 用户ID
     * @param permission 权限标识
     */
    void revokePermission(Long userId, String permission);

    /**
     * 获取用户所有权限
     * @param userId 用户ID
     * @return 权限标识数组
     */
    String[] getUserPermissions(Long userId);
} 