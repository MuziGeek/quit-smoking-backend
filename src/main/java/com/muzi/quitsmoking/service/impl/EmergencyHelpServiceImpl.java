package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.EmergencyHelpMapper;
import com.muzi.quitsmoking.model.entity.EmergencyHelp;
import com.muzi.quitsmoking.service.EmergencyHelpService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【emergency_help(紧急求助记录表，记录用户烟瘾发作的求助情况)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:04
*/
@Service
public class EmergencyHelpServiceImpl extends ServiceImpl<EmergencyHelpMapper, EmergencyHelp>
    implements EmergencyHelpService{

}




