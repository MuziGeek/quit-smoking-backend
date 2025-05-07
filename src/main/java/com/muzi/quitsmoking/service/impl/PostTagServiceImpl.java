package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.PostTagMapper;
import com.muzi.quitsmoking.model.entity.PostTag;
import com.muzi.quitsmoking.service.PostTagService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【post_tag(帖子标签表)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:18
*/
@Service
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag>
    implements PostTagService{

}




