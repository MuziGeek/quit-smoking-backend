package com.muzi.quitsmoking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.ArticleMapper;
import com.muzi.quitsmoking.model.entity.Article;
import com.muzi.quitsmoking.service.ArticleService;
import org.springframework.stereotype.Service;

/**
* @author ysh
* @description 针对表【article(戒烟资讯文章表)】的数据库操作Service实现
* @createDate 2025-04-25 15:36:36
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

}




