package com.muzi.quitsmoking.controller;

import com.muzi.quitsmoking.common.Result;
import com.muzi.quitsmoking.model.dto.EmergencyDTO;
import com.muzi.quitsmoking.model.vo.ArticleVO;
import com.muzi.quitsmoking.model.vo.BreathingExerciseVO;
import com.muzi.quitsmoking.model.vo.EmergencyHelpVO;
import com.muzi.quitsmoking.model.vo.MotivationalQuoteVO;
import com.muzi.quitsmoking.model.vo.SmokingTipVO;
import com.muzi.quitsmoking.service.ToolsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 工具控制器
 */
@Slf4j
@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
public class ToolsController {

    private final ToolsService toolsService;

    /**
     * 获取深呼吸练习列表
     * @return 深呼吸练习列表
     */
    @GetMapping("/breathing")
    public Result<List<BreathingExerciseVO>> getBreathingExercises() {
        List<BreathingExerciseVO> list = toolsService.getBreathingExercises();
        return Result.success(list);
    }

    /**
     * 获取戒烟建议
     * @return 戒烟建议
     */
    @GetMapping("/tips")
    public Result<List<SmokingTipVO>> getSmokingTips() {
        List<SmokingTipVO> list = toolsService.getSmokingTips();
        return Result.success(list);
    }

    /**
     * 获取戒烟资讯
     * @param params 查询参数
     * @return 戒烟资讯列表
     */
    @GetMapping("/articles")
    public Result<Map<String, Object>> getArticles(@RequestParam(required = false) Map<String, Object> params) {
        Map<String, Object> result = toolsService.getArticles(params);
        return Result.success(result);
    }

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/articles/{id}")
    public Result<ArticleVO> getArticleDetail(@PathVariable Long id) {
        ArticleVO article = toolsService.getArticleDetail(id);
        return Result.success(article);
    }

    /**
     * 获取紧急求助信息
     * @return 紧急求助信息
     */
    @GetMapping("/emergency")
    public Result<EmergencyHelpVO> getEmergencyHelp() {
        EmergencyHelpVO emergency = toolsService.getEmergencyHelp();
        return Result.success(emergency);
    }

    /**
     * 提交烟瘾紧急情况
     * @param emergencyDTO 紧急情况数据
     * @return 提交结果
     */
    @PostMapping("/emergency")
    public Result<Void> submitEmergency(@RequestBody @Valid EmergencyDTO emergencyDTO) {
        toolsService.submitEmergency(emergencyDTO);
        return Result.success();
    }

    /**
     * 获取激励名言
     * @return 激励名言
     */
    @GetMapping("/quotes")
    public Result<List<MotivationalQuoteVO>> getMotivationalQuotes() {
        List<MotivationalQuoteVO> list = toolsService.getMotivationalQuotes();
        return Result.success(list);
    }
}