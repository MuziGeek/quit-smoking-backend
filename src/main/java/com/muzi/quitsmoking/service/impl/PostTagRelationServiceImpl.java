package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.PostTagRelationMapper;
import com.muzi.quitsmoking.model.entity.PostTagRelation;
import com.muzi.quitsmoking.service.PostTagRelationService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【post_tag_relation(帖子与标签的关联表)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:26
*/
@Service
public class PostTagRelationServiceImpl extends ServiceImpl<PostTagRelationMapper, PostTagRelation>
    implements PostTagRelationService{

}




