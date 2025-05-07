package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.PostImageMapper;
import com.muzi.quitsmoking.model.entity.PostImage;
import com.muzi.quitsmoking.service.PostImageService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【post_image(帖子图片表)】的数据库操作Service实现
* @createDate 2025-04-25 15:38:16
*/
@Service
public class PostImageServiceImpl extends ServiceImpl<PostImageMapper, PostImage>
    implements PostImageService{

}




