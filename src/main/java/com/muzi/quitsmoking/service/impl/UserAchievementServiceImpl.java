package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.UserAchievementMapper;
import com.muzi.quitsmoking.model.entity.UserAchievement;
import com.muzi.quitsmoking.service.UserAchievementService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【user_achievement(用户已获得的成就记录表)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:26
*/
@Service
public class UserAchievementServiceImpl extends ServiceImpl<UserAchievementMapper, UserAchievement>
    implements UserAchievementService{

}




