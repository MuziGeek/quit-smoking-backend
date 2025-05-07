package com.muzi.quitsmoking.service;

import com.muzi.quitsmoking.model.dto.EmergencyDTO;
import com.muzi.quitsmoking.model.vo.ArticleVO;
import com.muzi.quitsmoking.model.vo.BreathingExerciseVO;
import com.muzi.quitsmoking.model.vo.EmergencyHelpVO;
import com.muzi.quitsmoking.model.vo.MotivationalQuoteVO;
import com.muzi.quitsmoking.model.vo.SmokingTipVO;

import java.util.List;
import java.util.Map;

/**
 * 工具服务接口
 */
public interface ToolsService {

    /**
     * 获取深呼吸练习列表
     * @return 深呼吸练习列表
     */
    List<BreathingExerciseVO> getBreathingExercises();

    /**
     * 获取戒烟建议
     * @return 戒烟建议
     */
    List<SmokingTipVO> getSmokingTips();

    /**
     * 获取戒烟资讯
     * @param params 查询参数
     * @return 戒烟资讯列表和分页信息
     */
    Map<String, Object> getArticles(Map<String, Object> params);

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    ArticleVO getArticleDetail(Long id);

    /**
     * 获取紧急求助信息
     * @return 紧急求助信息
     */
    EmergencyHelpVO getEmergencyHelp();

    /**
     * 提交烟瘾紧急情况
     * @param emergencyDTO 紧急情况数据
     */
    void submitEmergency(EmergencyDTO emergencyDTO);

    /**
     * 获取激励名言
     * @return 激励名言
     */
    List<MotivationalQuoteVO> getMotivationalQuotes();
} 