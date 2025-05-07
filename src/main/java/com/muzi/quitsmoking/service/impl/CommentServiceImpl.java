package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.CommentMapper;
import com.muzi.quitsmoking.model.entity.Comment;
import com.muzi.quitsmoking.service.CommentService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2025-04-25 15:37:53
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




