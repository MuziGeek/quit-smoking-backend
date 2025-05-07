package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.GroupMemberMapper;
import com.muzi.quitsmoking.model.entity.GroupMember;
import com.muzi.quitsmoking.service.GroupMemberService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【group_member(小组成员表)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:07
*/
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService{

}




