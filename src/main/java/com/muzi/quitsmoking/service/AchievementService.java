package com.muzi.quitsmoking.service;

import com.muzi.quitsmoking.model.entity.Achievement;
import com.muzi.quitsmoking.model.entity.UserAchievement;
import com.muzi.quitsmoking.model.vo.AchievementVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 成就系统服务接口
 * @author muzi
 */
public interface AchievementService extends IService<Achievement> {

    /**
     * 获取所有成就列表
     * @return 成就视图对象列表
     */
    List<AchievementVO> getAllAchievements();

    /**
     * 获取用户已获得的成就
     * @param userId 用户ID
     * @return 用户成就视图对象列表
     */
    List<AchievementVO> getUserAchievements(Long userId);

    /**
     * 获取成就详情
     * @param achievementId 成就ID
     * @return 成就视图对象
     */
    AchievementVO getAchievementDetail(Long achievementId);

    /**
     * 标记成就为已读
     * @param userAchievementId 用户成就ID
     * @return 操作是否成功
     */
    boolean markAchievementAsRead(Long userAchievementId);

    /**
     * 检查并授予用户成就
     * @param userId 用户ID
     * @param actionType 行动类型（如：登录打卡、社区发帖等）
     * @param actionValue 行动相关值
     * @return 新获得的成就列表
     */
    List<AchievementVO> checkAndGrantAchievements(Long userId, String actionType, Object actionValue);

    /**
     * 获取用户未读的成就通知
     * @param userId 用户ID
     * @return 未读的成就通知列表
     */
    List<UserAchievement> getUnreadAchievementNotifications(Long userId);
    
    /**
     * 获取用户成就进度
     * @param userId 用户ID
     * @param achievementId 成就ID
     * @return 进度百分比，如果未开始则为0，如果已完成则为100
     */
    int getAchievementProgress(Long userId, Long achievementId);
    
    /**
     * 获取用户成就统计信息
     * @param userId 用户ID
     * @return 统计信息（包含总成就数、已获得数、未获得数等）
     */
    Map<String, Object> getUserAchievementStats(Long userId);
}
