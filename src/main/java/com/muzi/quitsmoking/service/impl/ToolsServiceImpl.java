package com.muzi.quitsmoking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.ArticleMapper;
import com.muzi.quitsmoking.mapper.EmergencyHelpMapper;
import com.muzi.quitsmoking.model.dto.EmergencyDTO;
import com.muzi.quitsmoking.model.entity.Article;
import com.muzi.quitsmoking.model.entity.EmergencyHelp;
import com.muzi.quitsmoking.model.vo.ArticleVO;
import com.muzi.quitsmoking.model.vo.BreathingExerciseVO;
import com.muzi.quitsmoking.model.vo.EmergencyHelpVO;
import com.muzi.quitsmoking.model.vo.MotivationalQuoteVO;
import com.muzi.quitsmoking.model.vo.SmokingTipVO;
import com.muzi.quitsmoking.service.ToolsService;
import com.muzi.quitsmoking.service.UserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 工具服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ToolsServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ToolsService {
    @Resource
    private final EmergencyHelpMapper emergencyHelpMapper;

    private final UserService userService;

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    private Integer getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        // 实际项目中应该从Redis或JWT中获取用户ID
        // 这里简化处理，直接假设1用户
        return 1;
    }

    /**
     * 获取深呼吸练习列表
     * @return 深呼吸练习列表
     */
    @Override
    public List<BreathingExerciseVO> getBreathingExercises() {
        // 实际项目中应该从数据库中查询
        // 这里提供示例数据
        List<BreathingExerciseVO> list = new ArrayList<>();
        
        BreathingExerciseVO exercise1 = new BreathingExerciseVO();
        exercise1.setId(1L);
        exercise1.setName("4-7-8呼吸法");
        exercise1.setDescription("这是一种放松身心的呼吸方式，可以帮助缓解烟瘾和压力。");
        exercise1.setSteps("1. 舒适坐姿，放松身体\n2. 呼气，并数4秒\n3. 屏住呼吸，并数7秒\n4. 呼气，并数8秒\n5. 重复4次");
        exercise1.setDuration(5);
        exercise1.setDifficulty(1);
        exercise1.setImage("https://example.com/breathing/4-7-8.jpg");
        list.add(exercise1);
        
        BreathingExerciseVO exercise2 = new BreathingExerciseVO();
        exercise2.setId(2L);
        exercise2.setName("腹式呼吸");
        exercise2.setDescription("腹式呼吸可以增加肺活量，改善血液循环，有效缓解烟瘾。");
        exercise2.setSteps("1. 平躺或坐直，一手放胸部一手放腹部\n2. 缓慢吸气，让腹部隆起\n3. 缓慢呼气，收缩腹部\n4. 循环10次");
        exercise2.setDuration(10);
        exercise2.setDifficulty(2);
        exercise2.setImage("https://example.com/breathing/abdominal.jpg");
        list.add(exercise2);
        
        BreathingExerciseVO exercise3 = new BreathingExerciseVO();
        exercise3.setId(3L);
        exercise3.setName("交替鼻孔呼吸");
        exercise3.setDescription("这是一种瑜伽呼吸法，可以平衡左右脑，缓解焦虑和烟瘾。");
        exercise3.setSteps("1. 坐直，右手拇指盖住右鼻孔\n2. 从左鼻孔吸气\n3. 用右手无名指盖住左鼻孔，同时松开右鼻孔\n4. 从右鼻孔呼气\n5. 从右鼻孔吸气\n6. 盖住右鼻孔，从左鼻孔呼气\n7. 重复5-10次");
        exercise3.setDuration(15);
        exercise3.setDifficulty(3);
        exercise3.setImage("https://example.com/breathing/alternate-nostril.jpg");
        list.add(exercise3);
        
        return list;
    }

    /**
     * 获取戒烟建议
     * @return 戒烟建议
     */
    @Override
    public List<SmokingTipVO> getSmokingTips() {
        // 实际项目中应该从数据库中查询
        // 这里提供示例数据
        List<SmokingTipVO> list = new ArrayList<>();
        
        SmokingTipVO tip1 = new SmokingTipVO();
        tip1.setId(1L);
        tip1.setTitle("找到替代品");
        tip1.setContent("当烟瘾来临时，尝试咀嚼无糖口香糖、吃胡萝卜或芹菜等低热量零食，或使用戒烟糖果等替代品。");
        tip1.setCategory("行为策略");
        tip1.setStage("early");
        list.add(tip1);
        
        SmokingTipVO tip2 = new SmokingTipVO();
        tip2.setId(2L);
        tip2.setTitle("延迟吸烟");
        tip2.setContent("当想吸烟时，告诉自己再等10分钟。在这段时间内做些别的事情，烟瘾可能会过去。");
        tip2.setCategory("心理技巧");
        tip2.setStage("middle");
        list.add(tip2);
        
        SmokingTipVO tip3 = new SmokingTipVO();
        tip3.setId(3L);
        tip3.setTitle("避开触发因素");
        tip3.setContent("识别并避开能引起烟瘾的情况，如饮酒、咖啡、特定朋友或压力环境。");
        tip3.setCategory("环境管理");
        tip3.setStage("early");
        list.add(tip3);
        
        SmokingTipVO tip4 = new SmokingTipVO();
        tip4.setId(4L);
        tip4.setTitle("多喝水");
        tip4.setContent("多喝水可以帮助清除体内的尼古丁，减轻戒断症状，并且喝水这个动作可以代替吸烟的动作。");
        tip4.setCategory("健康习惯");
        tip4.setStage("early");
        list.add(tip4);
        
        SmokingTipVO tip5 = new SmokingTipVO();
        tip5.setId(5L);
        tip5.setTitle("寻求支持");
        tip5.setContent("告诉朋友和家人你正在戒烟，寻求他们的支持和鼓励。加入戒烟群组或寻求专业帮助也是很好的选择。");
        tip5.setCategory("社交支持");
        tip5.setStage("middle");
        list.add(tip5);
        
        return list;
    }

    /**
     * 获取戒烟资讯
     * @param params 查询参数
     * @return 戒烟资讯列表和分页信息
     */
    @Override
    public Map<String, Object> getArticles(Map<String, Object> params) {
        // 构建查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        
        // 处理查询参数
        if (params != null && params.containsKey("category")) {
            queryWrapper.eq(Article::getCategory, params.get("category"));
        }
        if (params != null && params.containsKey("keyword")) {
            String keyword = params.get("keyword").toString();
            queryWrapper.like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getSummary, keyword);
        }
        
        // 设置分页参数
        int current = 1;
        int size = 10;
        if (params != null && params.containsKey("page")) {
            current = Integer.parseInt(params.get("page").toString());
        }
        if (params != null && params.containsKey("size")) {
            size = Integer.parseInt(params.get("size").toString());
        }
        
        // 按发布时间降序排序
        queryWrapper.orderByDesc(Article::getCreated_at);

        // 查询文章列表
        Page<Article> page = new Page<>(current, size);
        page = page(page, queryWrapper);
        
        // 转换为VO
        List<ArticleVO> articleVOList = page.getRecords().stream().map(article -> {
            ArticleVO articleVO = new ArticleVO();
            articleVO.setId(Long.valueOf(article.getArticle_id()));
            articleVO.setTitle(article.getTitle());
            articleVO.setSummary(article.getSummary());
            // 不返回内容，减少数据量
            articleVO.setAuthor(article.getAuthor());
            articleVO.setCategory(article.getCategory());
            articleVO.setCoverImage(article.getCover());
            articleVO.setPublishTime(DateUtil.toLocalDateTime(article.getCreated_at()));
            articleVO.setReadCount(article.getView_count());
            articleVO.setLikeCount(article.getLike_count());
            return articleVO;
        }).collect(Collectors.toList());
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", articleVOList);
        result.put("total", page.getTotal());
        result.put("pages", page.getPages());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        
        return result;
    }

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleVO getArticleDetail(Long id) {
        // 查询文章
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        
        // 增加阅读量
        article.setView_count(article.getView_count() + 1);
        updateById(article);
        
        // 转换为VO
        ArticleVO articleVO = new ArticleVO();
        BeanUtil.copyProperties(article, articleVO, "id", "publishTime");
        articleVO.setId(Long.valueOf(article.getArticle_id()));
        articleVO.setPublishTime(DateUtil.toLocalDateTime(article.getCreated_at()));
        
        // 记录用户阅读记录
        // 实际项目中应该保存到数据库中
        
        return articleVO;
    }

    /**
     * 获取紧急求助信息
     * @return 紧急求助信息
     */
    @Override
    public EmergencyHelpVO getEmergencyHelp() {
        // 实际项目中应该从数据库中查询
        // 这里提供示例数据
        EmergencyHelpVO emergencyHelpVO = new EmergencyHelpVO();
        emergencyHelpVO.setTitle("强烈烟瘾应对指南");
        emergencyHelpVO.setDescription("当你感到强烈烟瘾难以抵抗时，请尝试以下方法或联系专业帮助。");
        
        // 设置求助方法
        List<EmergencyHelpVO.EmergencyMethod> methods = new ArrayList<>();
        
        EmergencyHelpVO.EmergencyMethod method1 = new EmergencyHelpVO.EmergencyMethod();
        method1.setId(1L);
        method1.setName("深呼吸法");
        method1.setDescription("深呼吸可以帮助你缓解焦虑和烟瘾。");
        method1.setSteps("1. 找一个安静的地方坐下\n2. 缓慢深吸气，数到4\n3. 屏住呼吸，数到4\n4. 缓慢呼气，数到6\n5. 重复5-10次");
        method1.setIcon("breathing");
        methods.add(method1);
        
        EmergencyHelpVO.EmergencyMethod method2 = new EmergencyHelpVO.EmergencyMethod();
        method2.setId(2L);
        method2.setName("分散注意力");
        method2.setDescription("转移注意力可以帮助你度过烟瘾高峰。");
        method2.setSteps("1. 立即开始做一些需要动手的活动\n2. 例如：洗碗、整理房间、园艺活动\n3. 或者去散步、运动");
        method2.setIcon("distract");
        methods.add(method2);
        
        EmergencyHelpVO.EmergencyMethod method3 = new EmergencyHelpVO.EmergencyMethod();
        method3.setId(3L);
        method3.setName("喝水");
        method3.setDescription("喝水可以冲淡烟瘾，提供替代性的口腔满足感。");
        method3.setSteps("1. 慢慢喝一大杯水\n2. 感受水在口中和喉咙的感觉\n3. 想象水在清洗你的身体");
        method3.setIcon("water");
        methods.add(method3);
        
        emergencyHelpVO.setMethods(methods);
        
        // 设置紧急联系人
        List<EmergencyHelpVO.Contact> contacts = new ArrayList<>();
        
        EmergencyHelpVO.Contact contact1 = new EmergencyHelpVO.Contact();
        contact1.setId(1L);
        contact1.setName("全国戒烟热线");
        contact1.setPhone("400-888-5531");
        contact1.setDescription("24小时戒烟咨询服务");
        contacts.add(contact1);
        
        EmergencyHelpVO.Contact contact2 = new EmergencyHelpVO.Contact();
        contact2.setId(2L);
        contact2.setName("心理健康热线");
        contact2.setPhone("400-161-9995");
        contact2.setDescription("提供心理支持和咨询");
        contacts.add(contact2);
        
        emergencyHelpVO.setContacts(contacts);
        
        return emergencyHelpVO;
    }

    /**
     * 提交烟瘾紧急情况
     * @param emergencyDTO 紧急情况数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitEmergency(EmergencyDTO emergencyDTO) {
        // 获取当前用户ID
        Integer userId = getCurrentUserId();
        
        // 创建紧急求助记录
        EmergencyHelp emergencyHelp = new EmergencyHelp();
        emergencyHelp.setUser_id(userId);
        emergencyHelp.setDescription(emergencyDTO.getDescription());
        emergencyHelp.setSeverity(emergencyDTO.getSeverity());
        emergencyHelp.setHandle_type(emergencyDTO.getHandleType());
        emergencyHelp.setStatus(1); // 新建状态
        emergencyHelp.setCreated_at(new Date());
        
        // 保存记录
        emergencyHelpMapper.insert(emergencyHelp);
        
        // 根据处理方式执行不同操作
        if (emergencyDTO.getHandleType() == 2) {
            // 联系亲友，实际项目中可以发送短信通知
            log.info("向用户{}的亲友发送通知", userId);
        } else if (emergencyDTO.getHandleType() == 3) {
            // 专业帮助，实际项目中可以联系戒烟顾问
            log.info("为用户{}联系专业戒烟顾问", userId);
        }
    }

    /**
     * 获取激励名言
     * @return 激励名言
     */
    @Override
    public List<MotivationalQuoteVO> getMotivationalQuotes() {
        // 实际项目中应该从数据库中查询
        // 这里提供示例数据
        List<MotivationalQuoteVO> list = new ArrayList<>();
        
        MotivationalQuoteVO quote1 = new MotivationalQuoteVO();
        quote1.setId(1L);
        quote1.setContent("每一次拒绝吸烟，你都离健康更近一步。");
        quote1.setAuthor("戒烟专家");
        quote1.setBackgroundImage("https://example.com/quotes/bg1.jpg");
        list.add(quote1);
        
        MotivationalQuoteVO quote2 = new MotivationalQuoteVO();
        quote2.setId(2L);
        quote2.setContent("戒烟的道路上你并不孤单，每天都有成千上万的人在同你一起奋斗。");
        quote2.setAuthor("健康协会");
        quote2.setBackgroundImage("https://example.com/quotes/bg2.jpg");
        list.add(quote2);
        
        MotivationalQuoteVO quote3 = new MotivationalQuoteVO();
        quote3.setId(3L);
        quote3.setContent("你必须先相信自己能成功，然后才会真正成功。");
        quote3.setAuthor("拿破仑·希尔");
        quote3.setBackgroundImage("https://example.com/quotes/bg3.jpg");
        list.add(quote3);
        
        MotivationalQuoteVO quote4 = new MotivationalQuoteVO();
        quote4.setId(4L);
        quote4.setContent("每天的进步一点点，终将汇聚成巨大的改变。");
        quote4.setAuthor("罗宾·夏尔马");
        quote4.setBackgroundImage("https://example.com/quotes/bg4.jpg");
        list.add(quote4);
        
        MotivationalQuoteVO quote5 = new MotivationalQuoteVO();
        quote5.setId(5L);
        quote5.setContent("戒掉香烟，点燃健康生活。");
        quote5.setAuthor("世界卫生组织");
        quote5.setBackgroundImage("https://example.com/quotes/bg5.jpg");
        list.add(quote5);
        
        // 随机返回一条名言
        Collections.shuffle(list);
        return list.subList(0, Math.min(3, list.size()));
    }
} 