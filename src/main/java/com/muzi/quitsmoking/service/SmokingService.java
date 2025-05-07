package com.muzi.quitsmoking.service;

import com.muzi.quitsmoking.model.dto.CheckinDTO;
import com.muzi.quitsmoking.model.vo.AchievementVO;
import com.muzi.quitsmoking.model.vo.CheckinVO;
import com.muzi.quitsmoking.model.vo.HealthStatusVO;
import com.muzi.quitsmoking.model.vo.SmokingStatsVO;
import com.muzi.quitsmoking.model.vo.TimelineVO;

import java.util.List;
import java.util.Map;

/**
 * 戒烟服务接口
 */
public interface SmokingService {

    /**
     * 获取戒烟统计数据
     * @return 戒烟统计数据
     */
    SmokingStatsVO getSmokingStats();

    /**
     * 获取戒烟打卡记录
     * @param params 查询参数
     * @return 打卡记录列表
     */
    List<CheckinVO> getCheckinRecords(Map<String, Object> params);

    /**
     * 提交每日打卡
     * @param checkinDTO 打卡数据
     * @return 打卡结果
     */
    CheckinVO submitCheckin(CheckinDTO checkinDTO);

    /**
     * 获取成就列表
     * @return 成就列表
     */
    List<AchievementVO> getAchievements();

    /**
     * 获取戒烟时间线
     * @return 戒烟时间线
     */
    TimelineVO getTimeline();

    /**
     * 获取戒烟健康状况
     * @return 健康状况
     */
    HealthStatusVO getHealthStatus();

    /**
     * 设置戒烟目标
     * @param data 目标数据
     */
    void setSmokingGoal(Map<String, Object> data);

    /**
     * 获取戒烟目标
     * @return 戒烟目标
     */
    Map<String, Object> getSmokingGoal();
} 