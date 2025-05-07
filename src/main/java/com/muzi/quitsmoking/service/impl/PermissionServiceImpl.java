package com.muzi.quitsmoking.service.impl;

import com.muzi.quitsmoking.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限服务实现类
 * 简单实现，实际项目中应该从数据库读取权限
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    /**
     * 模拟权限存储，用于演示
     * 实际项目中应该从数据库读取
     * Key: 用户ID, Value: 权限集合
     */
    private final Map<Long, Set<String>> permissionCache = new ConcurrentHashMap<>();

    @Override
    public boolean hasPermission(Long userId, String permission) {
        if (userId == null || permission == null) {
            return false;
        }
        
        // 获取用户权限集合
        Set<String> permissions = permissionCache.get(userId);
        if (permissions == null) {
            return false;
        }
        
        // 检查是否包含权限
        return permissions.contains(permission);
    }

    @Override
    public boolean hasAllPermissions(Long userId, String[] permissions) {
        if (userId == null || permissions == null || permissions.length == 0) {
            return false;
        }
        
        // 获取用户权限集合
        Set<String> userPermissions = permissionCache.get(userId);
        if (userPermissions == null) {
            return false;
        }
        
        // 检查是否包含所有权限
        for (String permission : permissions) {
            if (!userPermissions.contains(permission)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean hasAnyPermission(Long userId, String[] permissions) {
        if (userId == null || permissions == null || permissions.length == 0) {
            return false;
        }
        
        // 获取用户权限集合
        Set<String> userPermissions = permissionCache.get(userId);
        if (userPermissions == null) {
            return false;
        }
        
        // 检查是否包含任一权限
        for (String permission : permissions) {
            if (userPermissions.contains(permission)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void grantPermission(Long userId, String permission) {
        if (userId == null || permission == null) {
            return;
        }
        
        // 获取用户权限集合，如果不存在则创建
        Set<String> permissions = permissionCache.computeIfAbsent(userId, k -> new HashSet<>());
        
        // 添加权限
        permissions.add(permission);
        
        log.info("用户 {} 授予权限: {}", userId, permission);
    }

    @Override
    public void revokePermission(Long userId, String permission) {
        if (userId == null || permission == null) {
            return;
        }
        
        // 获取用户权限集合
        Set<String> permissions = permissionCache.get(userId);
        if (permissions == null) {
            return;
        }
        
        // 移除权限
        permissions.remove(permission);
        
        log.info("用户 {} 撤销权限: {}", userId, permission);
    }

    @Override
    public String[] getUserPermissions(Long userId) {
        if (userId == null) {
            return new String[0];
        }
        
        // 获取用户权限集合
        Set<String> permissions = permissionCache.get(userId);
        if (permissions == null) {
            return new String[0];
        }
        
        // 转换为数组
        return permissions.toArray(new String[0]);
    }
} 