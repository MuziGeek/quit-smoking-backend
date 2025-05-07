package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.HealthTimelineMapper;
import com.muzi.quitsmoking.model.entity.HealthTimeline;
import com.muzi.quitsmoking.service.HealthTimelineService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【health_timeline(戒烟健康时间线表，记录不同时间点的健康恢复情况)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:11
*/
@Service
public class HealthTimelineServiceImpl extends ServiceImpl<HealthTimelineMapper, HealthTimeline>
    implements HealthTimelineService{

}




