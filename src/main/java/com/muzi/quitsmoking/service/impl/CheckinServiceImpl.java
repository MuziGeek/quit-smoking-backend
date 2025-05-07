package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.CheckinMapper;
import com.muzi.quitsmoking.model.entity.Checkin;
import com.muzi.quitsmoking.service.CheckinService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【checkin(用户每日打卡记录表)】的数据库操作Service实现
* @createDate 2025-04-25 15:37:50
*/
@Service
public class CheckinServiceImpl extends ServiceImpl<CheckinMapper, Checkin>
    implements CheckinService{

}




