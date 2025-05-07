package com.muzi.quitsmoking.controller;

import com.muzi.quitsmoking.common.Result;
import com.muzi.quitsmoking.model.dto.CommentDTO;
import com.muzi.quitsmoking.model.dto.GroupDTO;
import com.muzi.quitsmoking.model.dto.PostDTO;
import com.muzi.quitsmoking.model.vo.CommentVO;
import com.muzi.quitsmoking.model.vo.GroupVO;
import com.muzi.quitsmoking.model.vo.PostDetailVO;
import com.muzi.quitsmoking.model.vo.PostVO;
import com.muzi.quitsmoking.model.vo.TopicVO;
import com.muzi.quitsmoking.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 社区控制器
 */
@Slf4j
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * 获取社区帖子列表
     * @param params 查询参数
     * @return 帖子列表
     */
    @GetMapping("/posts")
    public Result<Map<String, Object>> getPosts(@RequestParam(required = false) Map<String, Object> params) {
        Map<String, Object> result = communityService.getPosts(params);
        return Result.success(result);
    }

    /**
     * 获取推荐的热门话题
     * @return 热门话题列表
     */
    @GetMapping("/topics/hot")
    public Result<List<TopicVO>> getHotTopics() {
        List<TopicVO> list = communityService.getHotTopics();
        return Result.success(list);
    }

    /**
     * 获取帖子详情
     * @param id 帖子ID
     * @return 帖子详情
     */
    @GetMapping("/posts/{id}")
    public Result<PostDetailVO> getPostDetail(@PathVariable Long id) {
        PostDetailVO postDetail = communityService.getPostDetail(id);
        return Result.success(postDetail);
    }

    /**
     * 发布帖子
     * @param postDTO 帖子数据
     * @return 发布结果
     */
    @PostMapping("/posts")
    public Result<PostVO> createPost(@RequestBody @Valid PostDTO postDTO) {
        PostVO post = communityService.createPost(postDTO);
        return Result.success(post);
    }

    /**
     * 上传帖子图片
     * @param file 图片文件
     * @return 上传结果，返回图片URL
     */
    @PostMapping("/upload/image")
    public Result<String> uploadPostImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = communityService.uploadPostImage(file);
        return Result.success(imageUrl);
    }

    /**
     * 更新帖子
     * @param id 帖子ID
     * @param postDTO 帖子数据
     * @return 更新结果
     */
    @PutMapping("/posts/{id}")
    public Result<Void> updatePost(@PathVariable Long id, @RequestBody @Valid PostDTO postDTO) {
        communityService.updatePost(id, postDTO);
        return Result.success();
    }

    /**
     * 删除帖子
     * @param id 帖子ID
     * @return 删除结果
     */
    @DeleteMapping("/posts/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        communityService.deletePost(id);
        return Result.success();
    }

    /**
     * 点赞帖子
     * @param id 帖子ID
     * @return 点赞结果
     */
    @PostMapping("/posts/{id}/like")
    public Result<Void> likePost(@PathVariable Long id) {
        communityService.likePost(id);
        return Result.success();
    }

    /**
     * 取消点赞帖子
     * @param id 帖子ID
     * @return 取消点赞结果
     */
    @DeleteMapping("/posts/{id}/like")
    public Result<Void> unlikePost(@PathVariable Long id) {
        communityService.unlikePost(id);
        return Result.success();
    }

    /**
     * 获取帖子评论列表
     * @param id 帖子ID
     * @param params 查询参数
     * @return 评论列表
     */
    @GetMapping("/posts/{id}/comments")
    public Result<Map<String, Object>> getComments(@PathVariable Long id, @RequestParam(required = false) Map<String, Object> params) {
        Map<String, Object> result = communityService.getComments(id, params);
        return Result.success(result);
    }

    /**
     * 发布评论
     * @param id 帖子ID
     * @param commentDTO 评论数据
     * @return 发布结果
     */
    @PostMapping("/posts/{id}/comments")
    public Result<CommentVO> createComment(@PathVariable Long id, @RequestBody @Valid CommentDTO commentDTO) {
        CommentVO comment = communityService.createComment(id, commentDTO);
        return Result.success(comment);
    }

    /**
     * 删除评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     * @return 删除结果
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public Result<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        communityService.deleteComment(postId, commentId);
        return Result.success();
    }

    /**
     * 点赞评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     * @return 点赞结果
     */
    @PostMapping("/posts/{postId}/comments/{commentId}/like")
    public Result<Void> likeComment(@PathVariable Long postId, @PathVariable Long commentId) {
        communityService.likeComment(postId, commentId);
        return Result.success();
    }

    /**
     * 取消点赞评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     * @return 取消点赞结果
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}/like")
    public Result<Void> unlikeComment(@PathVariable Long postId, @PathVariable Long commentId) {
        communityService.unlikeComment(postId, commentId);
        return Result.success();
    }

    /**
     * 获取推荐的小组列表
     * @param params 查询参数
     * @return 小组列表
     */
    @GetMapping("/groups")
    public Result<Map<String, Object>> getGroups(@RequestParam(required = false) Map<String, Object> params) {
        Map<String, Object> result = communityService.getGroups(params);
        return Result.success(result);
    }

    /**
     * 获取小组详情
     * @param id 小组ID
     * @return 小组详情
     */
    @GetMapping("/groups/{id}")
    public Result<GroupVO> getGroupDetail(@PathVariable Long id) {
        GroupVO group = communityService.getGroupDetail(id);
        return Result.success(group);
    }

    /**
     * 创建小组
     * @param groupDTO 小组数据
     * @return 创建结果
     */
    @PostMapping("/groups")
    public Result<GroupVO> createGroup(@RequestBody @Valid GroupDTO groupDTO) {
        GroupVO group = communityService.createGroup(groupDTO);
        return Result.success(group);
    }

    /**
     * 加入小组
     * @param id 小组ID
     * @return 加入结果
     */
    @PostMapping("/groups/{id}/join")
    public Result<Void> joinGroup(@PathVariable Long id) {
        communityService.joinGroup(id);
        return Result.success();
    }

    /**
     * 退出小组
     * @param id 小组ID
     * @return 退出结果
     */
    @DeleteMapping("/groups/{id}/join")
    public Result<Void> leaveGroup(@PathVariable Long id) {
        communityService.leaveGroup(id);
        return Result.success();
    }
} 