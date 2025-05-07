package com.muzi.quitsmoking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.AchievementMapper;
import com.muzi.quitsmoking.mapper.CheckinMapper;
import com.muzi.quitsmoking.mapper.HealthTimelineMapper;
import com.muzi.quitsmoking.mapper.UserMapper;
import com.muzi.quitsmoking.model.dto.CheckinDTO;
import com.muzi.quitsmoking.model.entity.Achievement;
import com.muzi.quitsmoking.model.entity.Checkin;
import com.muzi.quitsmoking.model.entity.HealthTimeline;
import com.muzi.quitsmoking.model.entity.User;
import com.muzi.quitsmoking.model.vo.AchievementVO;
import com.muzi.quitsmoking.model.vo.CheckinVO;
import com.muzi.quitsmoking.model.vo.HealthStatusVO;
import com.muzi.quitsmoking.model.vo.SmokingStatsVO;
import com.muzi.quitsmoking.model.vo.TimelineVO;
import com.muzi.quitsmoking.service.SmokingService;
import com.muzi.quitsmoking.service.UserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 戒烟服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmokingServiceImpl extends ServiceImpl<CheckinMapper, Checkin> implements SmokingService {
    @Resource
    private final UserMapper userMapper;
    @Resource
    private final AchievementMapper achievementMapper;
    @Resource
    private final HealthTimelineMapper healthTimelineMapper;
    private final UserService userService;

    // 模拟用户目标数据存储
    private static final Map<Integer, Map<String, Object>> USER_GOALS = new HashMap<>();

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    private Integer getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            throw new RuntimeException("用户未登录");
        }
        
        token = token.replace("Bearer ", "");
        // 实际项目中应该从Redis或JWT中获取用户ID
        // 这里简化处理，直接假设1用户
        return 1;
    }

    /**
     * 获取戒烟统计数据
     * @return 戒烟统计数据
     */
    @Override
    public SmokingStatsVO getSmokingStats() {
        Integer userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        SmokingStatsVO statsVO = new SmokingStatsVO();
        
        // 假设用户有一个戒烟日期字段，实际项目中需要从数据库中获取
        LocalDate quitDate = LocalDate.now().minusDays(30); // 示例：假设用户30天前开始戒烟
        statsVO.setQuitDate(quitDate);
        
        // 计算戒烟天数
        long quitDays = ChronoUnit.DAYS.between(quitDate, LocalDate.now());
        statsVO.setQuitDays((int) quitDays);
        
        // 计算节省金额（假设每包烟20元，每天一包）
        double pricePerPack = 20.0; // 示例：假设每包烟20元
        int cigarettesPerDay = 20; // 示例：假设每天吸20支烟
        double savedMoney = quitDays * pricePerPack;
        statsVO.setSavedMoney(savedMoney);
        
        // 计算未吸香烟数量
        int nonSmokingCount = (int) (quitDays * cigarettesPerDay);
        statsVO.setNonSmokingCount(nonSmokingCount);
        
        // 计算增加寿命（假设每支烟减少11分钟寿命）
        int increasedLifeMinutes = nonSmokingCount * 11;
        statsVO.setIncreasedLifeMinutes(increasedLifeMinutes);
        
        // 计算连续打卡天数和今日是否已打卡
        // 实际项目中应该查询打卡记录表
        statsVO.setConsecutiveCheckinDays(15); // 示例数据
        statsVO.setIsTodayCheckin(false); // 示例数据
        
        return statsVO;
    }

    /**
     * 获取戒烟打卡记录
     * @param params 查询参数
     * @return 打卡记录列表
     */
    @Override
    public List<CheckinVO> getCheckinRecords(Map<String, Object> params) {
        Integer userId = getCurrentUserId();
        
        // 构建查询条件
        LambdaQueryWrapper<Checkin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Checkin::getUser_id, userId);
        
        // 处理查询参数，如日期范围等
        if (params != null && params.containsKey("startDate")) {
            Date startDate = DateUtil.parseDate(params.get("startDate").toString());
            queryWrapper.ge(Checkin::getCheckin_date, startDate);
        }
        if (params != null && params.containsKey("endDate")) {
            Date endDate = DateUtil.parseDate(params.get("endDate").toString());
            queryWrapper.le(Checkin::getCheckin_date, endDate);
        }
        
        // 按打卡日期降序排序
        queryWrapper.orderByDesc(Checkin::getCheckin_date);
        
        // 查询打卡记录
        List<Checkin> checkinList = list(queryWrapper);
        
        // 转换为VO
        List<CheckinVO> checkinVOList = checkinList.stream().map(checkin -> {
            CheckinVO checkinVO = new CheckinVO();
            checkinVO.setId(Long.valueOf(checkin.getCheckin_id()));
            checkinVO.setFeeling(checkin.getNote());
            
            // 将mood字段值映射为整数
            Map<String, Integer> moodMap = new HashMap<>();
            moodMap.put("happy", 1);
            moodMap.put("normal", 2);
            moodMap.put("sad", 4);
            moodMap.put("anxious", 3);
            moodMap.put("angry", 5);
            checkinVO.setMood(moodMap.getOrDefault(checkin.getMood(), 3));
            
            checkinVO.setCravingLevel(checkin.getCraving_level());
            checkinVO.setCheckinTime(DateUtil.toLocalDateTime(checkin.getCheckin_date()));
            return checkinVO;
        }).collect(Collectors.toList());
        
        return checkinVOList;
    }

    /**
     * 提交每日打卡
     * @param checkinDTO 打卡数据
     * @return 打卡结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckinVO submitCheckin(CheckinDTO checkinDTO) {
        Integer userId = getCurrentUserId();
        
        // 检查今日是否已打卡
        Date today = DateUtil.beginOfDay(new Date());
        LambdaQueryWrapper<Checkin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Checkin::getUser_id, userId)
                .ge(Checkin::getCheckin_date, today);
        long count = count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("今日已打卡");
        }
        
        // 创建打卡记录
        Checkin checkin = new Checkin();
        checkin.setUser_id(userId);
        checkin.setCheckin_date(new Date());
        
        // 设置状态，根据烟瘾程度判断
        if (checkinDTO.getCravingLevel() <= 2) {
            checkin.setStatus("success");
            checkin.setSmoked_count(0);
        } else if (checkinDTO.getCravingLevel() <= 4) {
            checkin.setStatus("tempted");
            checkin.setSmoked_count(0);
        } else {
            checkin.setStatus("failed");
            checkin.setSmoked_count(1); // 假设失败时吸了1支烟
        }
        
        // 设置烟瘾程度
        checkin.setCraving_level(checkinDTO.getCravingLevel());
        
        // 设置心情，将整数映射为字符串
        Map<Integer, String> moodMap = new HashMap<>();
        moodMap.put(1, "happy");
        moodMap.put(2, "normal");
        moodMap.put(3, "anxious");
        moodMap.put(4, "sad");
        moodMap.put(5, "angry");
        checkin.setMood(moodMap.getOrDefault(checkinDTO.getMood(), "normal"));
        
        // 设置笔记
        checkin.setNote(checkinDTO.getFeeling());
        
        // 设置创建时间
        checkin.setCreated_at(new Date());
        
        // 保存打卡记录
        save(checkin);
        
        // 转换为VO
        CheckinVO checkinVO = new CheckinVO();
        checkinVO.setId(Long.valueOf(checkin.getCheckin_id()));
        checkinVO.setFeeling(checkin.getNote());
        checkinVO.setMood(checkinDTO.getMood());
        checkinVO.setCravingLevel(checkin.getCraving_level());
        checkinVO.setCheckinTime(DateUtil.toLocalDateTime(checkin.getCheckin_date()));
        
        return checkinVO;
    }

    /**
     * 获取成就列表
     * @return 成就列表
     */
    @Override
    public List<AchievementVO> getAchievements() {
        // 获取所有成就
        List<Achievement> achievementList = achievementMapper.selectList(null);
        
        // 获取用户已获得的成就
        Integer userId = getCurrentUserId();
        // 实际项目中应该查询用户成就关联表
        Set<Integer> userAchievementIds = new HashSet<>(); // 示例：假设用户已获得的成就ID
        
        // 转换为VO
        return achievementList.stream().map(achievement -> {
            AchievementVO achievementVO = new AchievementVO();
            BeanUtil.copyProperties(achievement, achievementVO);
            
            // 设置是否已获得
            boolean achieved = userAchievementIds.contains(achievement.getAchievement_id());
            achievementVO.setAchieved(achieved);
            
            // 设置获得时间，实际项目中应该从用户成就关联表中获取
            if (achieved) {
                LocalDateTime randomPastDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
                // Convert LocalDateTime to Date
                Date date = Date.from(randomPastDateTime.atZone(ZoneId.systemDefault()).toInstant());
                achievementVO.setAchievedAt(date); // 示例数据
            }
            
            return achievementVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取戒烟时间线
     * @return 戒烟时间线
     */
    @Override
    public TimelineVO getTimeline() {
        Integer userId = getCurrentUserId();
        
        // 获取用户戒烟日期
        LocalDate quitDate = LocalDate.now().minusDays(30); // 示例：假设用户30天前开始戒烟
        
        // 构建时间线
        TimelineVO timelineVO = new TimelineVO();
        List<TimelineVO.TimelineItem> items = new ArrayList<>();
        
        // 添加戒烟开始事件
        TimelineVO.TimelineItem startItem = new TimelineVO.TimelineItem();
        startItem.setDate(quitDate);
        startItem.setTitle("开始戒烟");
        startItem.setDescription("您开始了戒烟之旅");
        startItem.setType(1);
        startItem.setIcon("smoke-free");
        items.add(startItem);
        
        // 添加健康里程碑
        List<HealthTimeline> healthTimelineList = healthTimelineMapper.selectList(null);
        for (HealthTimeline healthTimeline : healthTimelineList) {
            int days = healthTimeline.getDays_required();
            LocalDate milestoneDate = quitDate.plusDays(days);
            
            // 只添加已经达到的里程碑
            if (!milestoneDate.isAfter(LocalDate.now())) {
                TimelineVO.TimelineItem item = new TimelineVO.TimelineItem();
                item.setDate(milestoneDate);
                item.setTitle(healthTimeline.getTitle());
                item.setDescription(healthTimeline.getDescription());
                item.setType(1);
                item.setIcon("health");
                items.add(item);
            }
        }
        
        // 添加成就获得事件
        // 实际项目中应该查询用户成就关联表
        
        // 添加打卡记录
        LambdaQueryWrapper<Checkin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Checkin::getUser_id, userId)
                .orderByDesc(Checkin::getCheckin_date);
        List<Checkin> checkinList = list(queryWrapper);
        
        for (Checkin checkin : checkinList) {
            TimelineVO.TimelineItem item = new TimelineVO.TimelineItem();
            item.setDate(LocalDate.from(DateUtil.toLocalDateTime(checkin.getCheckin_date())));
            
            String status = checkin.getStatus();
            if ("success".equals(status)) {
                item.setTitle("成功打卡");
                item.setDescription("您今天成功抵抗了烟瘾");
                item.setIcon("success");
            } else if ("tempted".equals(status)) {
                item.setTitle("有烟瘾但忍住了");
                item.setDescription("您今天经历了烟瘾但成功坚持住了");
                item.setIcon("tempted");
            } else {
                item.setTitle("没忍住吸烟了");
                item.setDescription("您今天吸了" + checkin.getSmoked_count() + "支烟，继续加油！");
                item.setIcon("failed");
            }
            
            item.setType(3);
            items.add(item);
        }
        
        // 按日期降序排序
        items.sort(Comparator.comparing(TimelineVO.TimelineItem::getDate).reversed());
        
        timelineVO.setItems(items);
        return timelineVO;
    }

    /**
     * 获取戒烟健康状况
     * @return 健康状况
     */
    @Override
    public HealthStatusVO getHealthStatus() {
        // 获取用户戒烟日期
        LocalDate quitDate = LocalDate.now().minusDays(30); // 示例：假设用户30天前开始戒烟
        long quitDays = ChronoUnit.DAYS.between(quitDate, LocalDate.now());
        
        // 获取所有健康里程碑
        List<HealthTimeline> healthTimelineList = healthTimelineMapper.selectList(null);
        healthTimelineList.sort(Comparator.comparing(HealthTimeline::getDays_required));
        
        HealthStatusVO healthStatusVO = new HealthStatusVO();
        List<HealthStatusVO.HealthMilestoneVO> achievedMilestones = new ArrayList<>();
        HealthStatusVO.HealthMilestoneVO nextMilestone = null;
        
        // 计算当前进度和下一个里程碑
        for (HealthTimeline healthTimeline : healthTimelineList) {
            int requiredDays = healthTimeline.getDays_required();
            boolean achieved = quitDays >= requiredDays;
            
            HealthStatusVO.HealthMilestoneVO milestoneVO = new HealthStatusVO.HealthMilestoneVO();
            milestoneVO.setId(Long.valueOf(healthTimeline.getTimeline_id()));
            milestoneVO.setTitle(healthTimeline.getTitle());
            milestoneVO.setDescription(healthTimeline.getDescription());
            milestoneVO.setRequiredDays(requiredDays);
            milestoneVO.setAchieved(achieved);
            
            if (achieved) {
                milestoneVO.setAchievedPercent(100);
                achievedMilestones.add(milestoneVO);
            } else if (nextMilestone == null) {
                // 找到下一个未达成的里程碑
                int achievedPercent = (int) (quitDays * 100 / requiredDays);
                milestoneVO.setAchievedPercent(achievedPercent);
                nextMilestone = milestoneVO;
                
                // 设置当前总体进度百分比
                healthStatusVO.setCurrentProgressPercent(achievedPercent);
            }
        }
        
        healthStatusVO.setAchievedMilestones(achievedMilestones);
        healthStatusVO.setNextMilestone(nextMilestone);
        
        return healthStatusVO;
    }

    /**
     * 设置戒烟目标
     * @param data 目标数据
     */
    @Override
    public void setSmokingGoal(Map<String, Object> data) {
        Integer userId = getCurrentUserId();
        USER_GOALS.put(userId, data);
    }

    /**
     * 获取戒烟目标
     * @return 戒烟目标
     */
    @Override
    public Map<String, Object> getSmokingGoal() {
        Integer userId = getCurrentUserId();
        return USER_GOALS.getOrDefault(userId, new HashMap<>());
    }
} 