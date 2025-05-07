package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.HealthBenefitMapper;
import com.muzi.quitsmoking.model.entity.HealthBenefit;
import com.muzi.quitsmoking.service.HealthBenefitService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【health_benefit(健康时间线收益表，记录每个时间点的具体健康收益)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:09
*/
@Service
public class HealthBenefitServiceImpl extends ServiceImpl<HealthBenefitMapper, HealthBenefit>
    implements HealthBenefitService{

}




