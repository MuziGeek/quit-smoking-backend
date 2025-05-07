package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.ToolMapper;
import com.muzi.quitsmoking.model.entity.Tool;
import com.muzi.quitsmoking.service.ToolService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【tool(戒烟工具表，包含各种辅助戒烟的工具)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:26
*/
@Service
public class ToolServiceImpl extends ServiceImpl<ToolMapper, Tool>
    implements ToolService{

}




