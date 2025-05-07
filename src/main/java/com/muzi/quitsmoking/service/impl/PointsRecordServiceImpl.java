package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.PointsRecordMapper;
import com.muzi.quitsmoking.mapper.UserMapper;
import com.muzi.quitsmoking.model.entity.PointsRecord;
import com.muzi.quitsmoking.model.entity.User;
import com.muzi.quitsmoking.service.PointsRecordService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 积分记录服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements PointsRecordService {
    @Resource
    private final UserMapper userMapper;

    /**
     * 创建积分记录
     *
     * @param userId 用户ID
     * @param points 积分变动值（正数增加，负数减少）
     * @param reason 变动原因
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPointsRecord(Long userId, Integer points, String reason) {
        // 获取用户当前积分
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("创建积分记录失败：用户不存在, userId={}", userId);
            return false;
        }

        Integer currentPoints = user.getPoints() == null ? 0 : user.getPoints();
        Integer updatedPoints = currentPoints + points;
        if (updatedPoints < 0) {
            updatedPoints = 0; // 确保积分不为负数
        }

        // 创建积分记录
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setUser_id(Math.toIntExact(userId));
        pointsRecord.setPoints(points);
        pointsRecord.setReason(reason);
        pointsRecord.setCurrent_points(updatedPoints);

        // 保存记录
        boolean success = save(pointsRecord);
        if (success) {
            log.info("积分记录创建成功：userId={}, points={}, reason={}, currentPoints={}", userId, points, reason, updatedPoints);
        } else {
            log.error("积分记录创建失败：userId={}, points={}, reason={}", userId, points, reason);
        }

        return success;
    }

    /**
     * 分页查询用户积分记录
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @Override
    public Page<PointsRecord> getUserPointsRecordByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRecord::getUser_id, userId)
                .orderByDesc(PointsRecord::getCreated_at);

        // 执行分页查询
        Page<PointsRecord> page = new Page<>(pageNum, pageSize);
        page = page(page, queryWrapper);

        // 转换为分页VO
        Page<PointsRecord> pageVO = new Page<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());

        return pageVO;
    }
} 