package com.muzi.quitsmoking.controller;

import com.muzi.quitsmoking.common.Result;
import com.muzi.quitsmoking.model.entity.UserAchievement;
import com.muzi.quitsmoking.model.vo.AchievementVO;
import com.muzi.quitsmoking.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 成就系统控制器
 */
@Slf4j
@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    /**
     * 获取所有成就列表
     * @return 成就列表
     */
    @GetMapping("/list")
    public Result<List<AchievementVO>> getAllAchievements() {
        List<AchievementVO> list = achievementService.getAllAchievements();
        return Result.success(list);
    }

    /**
     * 获取用户已获得的成就
     * @return 用户已获得的成就列表
     */
    @GetMapping("/user")
    public Result<List<AchievementVO>> getUserAchievements() {
        List<AchievementVO> list = achievementService.getUserAchievements(null);
        return Result.success(list);
    }

    /**
     * 获取成就详情
     * @param id 成就ID
     * @return 成就详情
     */
    @GetMapping("/{id}")
    public Result<AchievementVO> getAchievementDetail(@PathVariable("id") Long id) {
        AchievementVO achievement = achievementService.getAchievementDetail(id);
        return Result.success(achievement);
    }

    /**
     * 标记成就为已读
     * @param id 用户成就ID
     * @return 操作结果
     */
    @PostMapping("/read/{id}")
    public Result<Void> markAchievementAsRead(@PathVariable("id") Long id) {
        boolean success = achievementService.markAchievementAsRead(id);
        return success ? Result.success() : Result.error("标记成就已读失败");
    }
    
    /**
     * 获取用户未读的成就通知
     * @return 未读的成就通知列表
     */
    @GetMapping("/notification/unread")
    public Result<List<UserAchievement>> getUnreadAchievementNotifications() {
        List<UserAchievement> notifications = achievementService.getUnreadAchievementNotifications(null);
        return Result.success(notifications);
    }
    
    /**
     * 获取用户成就统计信息
     * @return 成就统计信息
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getUserAchievementStats() {
        Map<String, Object> stats = achievementService.getUserAchievementStats(null);
        return Result.success(stats);
    }
    
    /**
     * 获取特定成就的进度
     * @param id 成就ID
     * @return 成就进度（0-100）
     */
    @GetMapping("/progress/{id}")
    public Result<Integer> getAchievementProgress(@PathVariable("id") Long id) {
        int progress = achievementService.getAchievementProgress(null, id);
        return Result.success(progress);
    }
} 