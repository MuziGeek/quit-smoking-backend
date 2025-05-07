package com.muzi.quitsmoking.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.muzi.quitsmoking.model.entity.PointsRecord;

/**
 * 积分记录服务接口
 */
public interface PointsRecordService extends IService<PointsRecord> {
    
    /**
     * 创建积分记录
     *
     * @param userId 用户ID
     * @param points 积分变动值（正数增加，负数减少）
     * @param reason 变动原因
     * @return 是否成功
     */
    boolean createPointsRecord(Long userId, Integer points, String reason);
    
    /**
     * 分页查询用户积分记录
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<PointsRecord> getUserPointsRecordByPage(Long userId, Integer pageNum, Integer pageSize);
} 