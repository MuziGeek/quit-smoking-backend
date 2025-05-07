package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.exception.CustomException;
import com.muzi.quitsmoking.mapper.AchievementMapper;
import com.muzi.quitsmoking.mapper.UserAchievementMapper;
import com.muzi.quitsmoking.mapper.UserMapper;
import com.muzi.quitsmoking.model.entity.Achievement;
import com.muzi.quitsmoking.model.entity.User;
import com.muzi.quitsmoking.model.entity.UserAchievement;
import com.muzi.quitsmoking.model.vo.AchievementVO;
import com.muzi.quitsmoking.service.AchievementService;
import com.muzi.quitsmoking.service.UserService;
import com.muzi.quitsmoking.utils.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 成就系统服务实现类
 * @author muzi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementServiceImpl extends ServiceImpl<AchievementMapper, Achievement>
    implements AchievementService {
    @Resource
    private final AchievementMapper achievementMapper;
    @Resource
    private final UserAchievementMapper userAchievementMapper;
    @Resource
    private final UserMapper userMapper;
    private final UserService userService;
    
    @Override
    public List<AchievementVO> getAllAchievements() {
        // 获取当前用户ID
        Long userId = SecurityUtils.getCurrentUserId();

        // 获取所有成就
        List<Achievement> achievements = achievementMapper.selectList(null);
        
        // 获取用户已获得的成就
        List<UserAchievement> userAchievements = userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
        );
        
        // 转换为VO对象
        return convertToAchievementVOList(achievements, userAchievements);
    }

    @Override
    public List<AchievementVO> getUserAchievements(Long userId) {
        // 如果未传入用户ID，则获取当前登录用户的ID
        if (userId == null) {
            userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                throw new CustomException("用户未登录");
            }
        }
        
        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new CustomException("用户不存在");
        }
        
        // 获取用户已获得的成就
        List<UserAchievement> userAchievements = userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
        );
        
        if (userAchievements.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取成就详情
        List<Integer> achievementIds = userAchievements.stream()
                .map(UserAchievement::getAchievement_id)
                .collect(Collectors.toList());
        
        List<Achievement> achievements = achievementMapper.selectList(
                new LambdaQueryWrapper<Achievement>()
                        .in(Achievement::getAchievement_id, achievementIds)
        );
        
        // 转换为VO对象
        return convertToAchievementVOList(achievements, userAchievements);
    }

    @Override
    public AchievementVO getAchievementDetail(Long achievementId) {
        // 参数校验
        if (achievementId == null) {
            throw new CustomException("成就ID不能为空");
        }
        
        // 获取当前用户ID
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new CustomException("用户未登录");
        }
        
        // 获取成就详情
        Achievement achievement = achievementMapper.selectById(achievementId);
        if (achievement == null) {
            throw new CustomException("成就不存在");
        }
        
        // 查询用户是否已获得该成就
        UserAchievement userAchievement = userAchievementMapper.selectOne(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
                        .eq(UserAchievement::getAchievement_id, achievementId)
        );
        
        // 获取当前进度
        int progress = getAchievementProgress(userId, achievementId);
        Integer currentValue = calculateCurrentValue(userId, achievement);
        
        // 转换为VO
        AchievementVO vo = new AchievementVO();
        vo.setAchievementId(achievement.getAchievement_id());
        vo.setTitle(achievement.getTitle());
        vo.setDescription(achievement.getDescription());
        vo.setIcon(achievement.getIcon());
        vo.setType(achievement.getType());
        vo.setConditionType(achievement.getCondition_type());
        vo.setConditionValue(achievement.getCondition_value());
        vo.setPoints(achievement.getPoints());
        vo.setLevel(achievement.getLevel());
        vo.setAchieved(userAchievement != null);
        vo.setProgress(progress);
        vo.setCurrentValue(currentValue);
        
        if (userAchievement != null) {
            vo.setAchievedAt(userAchievement.getAchieved_time());
            vo.setIsRead(userAchievement.getIs_read() == 1);
        }
        
        return vo;
    }

    @Override
    @Transactional
    public boolean markAchievementAsRead(Long userAchievementId) {
        // 参数校验
        if (userAchievementId == null) {
            throw new CustomException("用户成就ID不能为空");
        }
        
        // 获取当前用户ID
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new CustomException("用户未登录");
        }
        
        // 查询用户成就记录
        UserAchievement userAchievement = userAchievementMapper.selectById(userAchievementId);
        if (userAchievement == null) {
            throw new CustomException("用户成就记录不存在");
        }
        
        // 验证是否为当前用户的成就
        if (!userAchievement.getUser_id().equals(userId.intValue())) {
            throw new CustomException("无权操作");
        }
        
        // 标记为已读
        LambdaUpdateWrapper<UserAchievement> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAchievement::getAchievement_id, userAchievementId)
                .set(UserAchievement::getIs_read, 1);
        
        return userAchievementMapper.update(null, updateWrapper) > 0;
    }

    @Override
    @Transactional
    public List<AchievementVO> checkAndGrantAchievements(Long userId, String actionType, Object actionValue) {
        // 参数校验
        if (userId == null || actionType == null || actionValue == null) {
            log.error("成就检查参数不完整: userId={}, actionType={}, actionValue={}", userId, actionType, actionValue);
            return Collections.emptyList();
        }
        
        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("成就检查失败: 用户不存在, userId={}", userId);
            return Collections.emptyList();
        }
        
        // 查询与该动作类型相关的所有成就
        List<Achievement> achievements = achievementMapper.selectList(
                new LambdaQueryWrapper<Achievement>()
                        .eq(Achievement::getCondition_type, actionType)
        );
        
        if (achievements.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 查询用户已获得的成就
        List<UserAchievement> userAchievements = userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
        );
        
        Set<Integer> achievedIds = userAchievements.stream()
                .map(UserAchievement::getAchievement_id)
                .collect(Collectors.toSet());
        
        // 检查每个成就条件是否满足
        Integer currentValue = parseActionValue(actionValue);
        if (currentValue == null) {
            log.error("无法解析的动作值: {}", actionValue);
            return Collections.emptyList();
        }
        
        List<Achievement> newlyAchieved = new ArrayList<>();
        Date now = new Date();
        
        for (Achievement achievement : achievements) {
            // 跳过已获得的成就
            if (achievedIds.contains(achievement.getAchievement_id())) {
                continue;
            }
            
            // 检查是否满足条件
            if (currentValue >= achievement.getCondition_value()) {
                // 授予成就
                UserAchievement newAchievement = new UserAchievement();
                newAchievement.setUser_id(userId.intValue());
                newAchievement.setAchievement_id(achievement.getAchievement_id());
                newAchievement.setAchieved_time(now);
                newAchievement.setIs_read(0); // 未读
                
                userAchievementMapper.insert(newAchievement);
                
                // 更新用户积分
                userService.updateUserPoints(userId, achievement.getPoints(), "获得成就: " + achievement.getTitle());
                
                newlyAchieved.add(achievement);
                log.info("用户获得新成就: userId={}, achievementId={}, title={}", userId, achievement.getAchievement_id(), achievement.getTitle());
            }
        }
        
        // 返回新获得的成就列表
        if (newlyAchieved.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 获取新创建的用户成就记录
        List<UserAchievement> newUserAchievements = userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
                        .in(UserAchievement::getAchievement_id, newlyAchieved.stream().map(Achievement::getAchievement_id).collect(Collectors.toList()))
        );
        
        return convertToAchievementVOList(newlyAchieved, newUserAchievements);
    }

    @Override
    public List<UserAchievement> getUnreadAchievementNotifications(Long userId) {
        // 如果未传入用户ID，则获取当前登录用户的ID
        if (userId == null) {
            userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                throw new CustomException("用户未登录");
            }
        }
        
        // 查询未读成就通知
        return userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
                        .eq(UserAchievement::getIs_read, 0)
        );
    }

    @Override
    public int getAchievementProgress(Long userId, Long achievementId) {
        // 参数校验
        if (userId == null || achievementId == null) {
            return 0;
        }
        
        // 获取成就信息
        Achievement achievement = achievementMapper.selectById(achievementId);
        if (achievement == null) {
            return 0;
        }
        
        // 检查用户是否已获得该成就
        UserAchievement userAchievement = userAchievementMapper.selectOne(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
                        .eq(UserAchievement::getAchievement_id, achievementId)
        );
        
        // 如果已获得该成就，直接返回100%
        if (userAchievement != null) {
            return 100;
        }
        
        // 计算当前值
        Integer currentValue = calculateCurrentValue(userId, achievement);
        if (currentValue == null) {
            return 0;
        }
        
        // 计算进度百分比
        int conditionValue = achievement.getCondition_value();
        if (conditionValue <= 0) {
            return currentValue > 0 ? 100 : 0;
        }
        
        int progress = (int) ((double) currentValue / conditionValue * 100);
        return Math.min(100, progress); // 确保不超过100%
    }

    @Override
    public Map<String, Object> getUserAchievementStats(Long userId) {
        // 如果未传入用户ID，则获取当前登录用户的ID
        if (userId == null) {
            userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                throw new CustomException("用户未登录");
            }
        }
        
        // 查询用户已获得的成就
        List<UserAchievement> userAchievements = userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>()
                        .eq(UserAchievement::getUser_id, userId)
        );
        
        // 获取所有成就总数
        Long totalAchievements = achievementMapper.selectCount(null);
        
        // 计算统计数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAchieved", userAchievements.size());
        stats.put("totalAchievements", totalAchievements);
        stats.put("remainingAchievements", totalAchievements - userAchievements.size());
        
        // 计算完成率
        double completionRate = totalAchievements > 0 
                ? (double) userAchievements.size() / totalAchievements * 100 
                : 0;
        stats.put("completionRate", Math.round(completionRate));
        
        // 类型统计
        if (!userAchievements.isEmpty()) {
            List<Integer> achievementIds = userAchievements.stream()
                    .map(UserAchievement::getAchievement_id)
                    .collect(Collectors.toList());
            
            List<Achievement> achievements = achievementMapper.selectList(
                    new LambdaQueryWrapper<Achievement>()
                            .in(Achievement::getAchievement_id, achievementIds)
            );
            
            // 按类型分组
            Map<String, Long> typeCount = achievements.stream()
                    .collect(Collectors.groupingBy(Achievement::getType, Collectors.counting()));
            
            stats.put("typeDistribution", typeCount);
        }
        
        return stats;
    }

    /**
     * 将成就实体和用户成就关联信息转换为视图对象列表
     * @param achievements 成就列表
     * @param userAchievements 用户成就列表
     * @return 成就视图对象列表
     */
    private List<AchievementVO> convertToAchievementVOList(List<Achievement> achievements, List<UserAchievement> userAchievements) {
        if (achievements == null || achievements.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 创建用户成就ID映射，方便快速查找
        Map<Integer, UserAchievement> userAchievementMap = userAchievements.stream()
                .collect(Collectors.toMap(
                        UserAchievement::getAchievement_id,
                        ua -> ua,
                        (existing, replacement) -> existing // 如果有重复，保留第一个
                ));
        
        // 获取当前用户ID用于计算进度
        Long userId = SecurityUtils.getCurrentUserId();
        
        // 转换为VO对象
        return achievements.stream().map(achievement -> {
            AchievementVO vo = new AchievementVO();
            vo.setAchievementId(achievement.getAchievement_id());
            vo.setTitle(achievement.getTitle());
            vo.setDescription(achievement.getDescription());
            vo.setIcon(achievement.getIcon());
            vo.setType(achievement.getType());
            vo.setConditionType(achievement.getCondition_type());
            vo.setConditionValue(achievement.getCondition_value());
            vo.setPoints(achievement.getPoints());
            vo.setLevel(achievement.getLevel());
            
            // 设置用户相关信息
            UserAchievement userAchievement = userAchievementMap.get(achievement.getAchievement_id());
            vo.setAchieved(userAchievement != null);
            
            if (userAchievement != null) {
                vo.setAchievedAt(userAchievement.getAchieved_time());
                vo.setIsRead(userAchievement.getIs_read() == 1);
                vo.setProgress(100); // 已获得的成就进度为100%
                vo.setCurrentValue(achievement.getCondition_value());
            } else if (userId != null) {
                // 计算进度
                vo.setProgress(getAchievementProgress(userId, Long.valueOf(achievement.getAchievement_id())));
                vo.setCurrentValue(calculateCurrentValue(userId, achievement));
            } else {
                vo.setProgress(0);
                vo.setCurrentValue(0);
            }
            
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 计算用户当前成就条件的值
     * @param userId 用户ID
     * @param achievement 成就
     * @return 当前值
     */
    private Integer calculateCurrentValue(Long userId, Achievement achievement) {
        if (userId == null || achievement == null) {
            return 0;
        }
        
        String conditionType = achievement.getCondition_type();
        
        // 根据不同的条件类型获取当前值
        switch (conditionType) {
            case "days": // 戒烟天数
                User user = userMapper.selectById(userId);
                if (user == null ) {//
                    return 0;
                }
                // 计算戒烟天数
//                long quitDays = (new Date().getTime() - user.getQuit_date().getTime()) / (1000 * 60 * 60 * 24);
                return (int) Math.max(0, 1);
                
            case "saves": // 节省金额
                // 从用户服务获取节省金额
                return userService.getUserSavedMoney(userId).intValue();
                
            case "posts": // 发帖数
                // TODO: 从社区服务获取用户发帖数
                return 0;
                
            case "comments": // 评论数
                // TODO: 从社区服务获取用户评论数
                return 0;
                
            case "checkins": // 打卡次数
                // TODO: 从打卡服务获取用户打卡次数
                return 0;
                
            // 其他条件类型
            default:
                log.warn("未知的成就条件类型: {}", conditionType);
                return 0;
        }
    }
    
    /**
     * 解析动作值为整数
     * @param actionValue 动作值
     * @return 整数值
     */
    private Integer parseActionValue(Object actionValue) {
        if (actionValue == null) {
            return null;
        }
        
        if (actionValue instanceof Integer) {
            return (Integer) actionValue;
        } else if (actionValue instanceof Long) {
            return ((Long) actionValue).intValue();
        } else if (actionValue instanceof Double) {
            return ((Double) actionValue).intValue();
        } else if (actionValue instanceof String) {
            try {
                return Integer.parseInt((String) actionValue);
            } catch (NumberFormatException e) {
                log.error("无法将字符串转换为数字: {}", actionValue);
                return null;
            }
        }
        
        log.error("不支持的动作值类型: {}", actionValue.getClass().getName());
        return null;
    }
}




