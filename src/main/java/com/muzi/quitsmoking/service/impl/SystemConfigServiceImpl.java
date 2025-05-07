package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.SystemConfigMapper;
import com.muzi.quitsmoking.model.entity.SystemConfig;
import com.muzi.quitsmoking.service.SystemConfigService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【system_config(系统配置表)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:26
*/
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService{

}




