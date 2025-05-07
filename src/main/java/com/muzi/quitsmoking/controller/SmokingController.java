package com.muzi.quitsmoking.controller;

import com.muzi.quitsmoking.common.Result;
import com.muzi.quitsmoking.model.dto.CheckinDTO;
import com.muzi.quitsmoking.model.vo.AchievementVO;
import com.muzi.quitsmoking.model.vo.CheckinVO;
import com.muzi.quitsmoking.model.vo.HealthStatusVO;
import com.muzi.quitsmoking.model.vo.SmokingStatsVO;
import com.muzi.quitsmoking.model.vo.TimelineVO;
import com.muzi.quitsmoking.service.SmokingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 戒烟控制器
 */
@Slf4j
@RestController
@RequestMapping("/smoking")
@RequiredArgsConstructor
public class SmokingController {

    private final SmokingService smokingService;

    /**
     * 获取戒烟统计数据
     * @return 戒烟统计数据
     */
    @GetMapping("/stats")
    public Result<SmokingStatsVO> getSmokingStats() {
        SmokingStatsVO stats = smokingService.getSmokingStats();
        return Result.success(stats);
    }

    /**
     * 获取戒烟打卡记录
     * @param params 查询参数
     * @return 打卡记录列表
     */
    @GetMapping("/checkin")
    public Result<List<CheckinVO>> getCheckinRecords(@RequestParam(required = false) Map<String, Object> params) {
        List<CheckinVO> list = smokingService.getCheckinRecords(params);
        return Result.success(list);
    }

    /**
     * 提交每日打卡
     * @param checkinDTO 打卡数据
     * @return 打卡结果
     */
    @PostMapping("/checkin")
    public Result<CheckinVO> submitCheckin(@RequestBody @Valid CheckinDTO checkinDTO) {
        CheckinVO checkinVO = smokingService.submitCheckin(checkinDTO);
        return Result.success(checkinVO);
    }

    /**
     * 获取成就列表
     * @return 成就列表
     */
    @GetMapping("/achievements")
    public Result<List<AchievementVO>> getAchievements() {
        List<AchievementVO> list = smokingService.getAchievements();
        return Result.success(list);
    }

    /**
     * 获取戒烟时间线
     * @return 戒烟时间线
     */
    @GetMapping("/timeline")
    public Result<TimelineVO> getTimeline() {
        TimelineVO timeline = smokingService.getTimeline();
        return Result.success(timeline);
    }

    /**
     * 获取戒烟健康状况
     * @return 健康状况
     */
    @GetMapping("/health")
    public Result<HealthStatusVO> getHealthStatus() {
        HealthStatusVO healthStatus = smokingService.getHealthStatus();
        return Result.success(healthStatus);
    }

    /**
     * 设置戒烟目标
     * @param data 目标数据
     * @return 设置结果
     */
    @PostMapping("/goal")
    public Result<Void> setSmokingGoal(@RequestBody Map<String, Object> data) {
        smokingService.setSmokingGoal(data);
        return Result.success();
    }

    /**
     * 获取戒烟目标
     * @return 戒烟目标
     */
    @GetMapping("/goal")
    public Result<Map<String, Object>> getSmokingGoal() {
        Map<String, Object> goal = smokingService.getSmokingGoal();
        return Result.success(goal);
    }
} 