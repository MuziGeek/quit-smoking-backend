package com.muzi.quitsmoking.service;

import com.muzi.quitsmoking.model.dto.CommentDTO;
import com.muzi.quitsmoking.model.dto.GroupDTO;
import com.muzi.quitsmoking.model.dto.PostDTO;
import com.muzi.quitsmoking.model.vo.CommentVO;
import com.muzi.quitsmoking.model.vo.GroupVO;
import com.muzi.quitsmoking.model.vo.PostDetailVO;
import com.muzi.quitsmoking.model.vo.PostVO;
import com.muzi.quitsmoking.model.vo.TopicVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 社区服务接口
 */
public interface CommunityService {

    /**
     * 获取社区帖子列表
     * @param params 查询参数
     * @return 帖子列表和分页信息
     */
    Map<String, Object> getPosts(Map<String, Object> params);

    /**
     * 获取推荐的热门话题
     * @return 热门话题列表
     */
    List<TopicVO> getHotTopics();

    /**
     * 获取帖子详情
     * @param id 帖子ID
     * @return 帖子详情
     */
    PostDetailVO getPostDetail(Long id);

    /**
     * 发布帖子
     * @param postDTO 帖子数据
     * @return 发布结果
     */
    PostVO createPost(PostDTO postDTO);

    /**
     * 上传帖子图片
     * @param file 图片文件
     * @return 上传结果，返回图片URL
     */
    String uploadPostImage(MultipartFile file);

    /**
     * 更新帖子
     * @param id 帖子ID
     * @param postDTO 帖子数据
     */
    void updatePost(Long id, PostDTO postDTO);

    /**
     * 删除帖子
     * @param id 帖子ID
     */
    void deletePost(Long id);

    /**
     * 点赞帖子
     * @param id 帖子ID
     */
    void likePost(Long id);

    /**
     * 取消点赞帖子
     * @param id 帖子ID
     */
    void unlikePost(Long id);

    /**
     * 获取帖子评论列表
     * @param id 帖子ID
     * @param params 查询参数
     * @return 评论列表和分页信息
     */
    Map<String, Object> getComments(Long id, Map<String, Object> params);

    /**
     * 发布评论
     * @param id 帖子ID
     * @param commentDTO 评论数据
     * @return 发布结果
     */
    CommentVO createComment(Long id, CommentDTO commentDTO);

    /**
     * 删除评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    void deleteComment(Long postId, Long commentId);

    /**
     * 点赞评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    void likeComment(Long postId, Long commentId);

    /**
     * 取消点赞评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    void unlikeComment(Long postId, Long commentId);

    /**
     * 获取推荐的小组列表
     * @param params 查询参数
     * @return 小组列表和分页信息
     */
    Map<String, Object> getGroups(Map<String, Object> params);

    /**
     * 获取小组详情
     * @param id 小组ID
     * @return 小组详情
     */
    GroupVO getGroupDetail(Long id);

    /**
     * 创建小组
     * @param groupDTO 小组数据
     * @return 创建结果
     */
    GroupVO createGroup(GroupDTO groupDTO);

    /**
     * 加入小组
     * @param id 小组ID
     */
    void joinGroup(Long id);

    /**
     * 退出小组
     * @param id 小组ID
     */
    void leaveGroup(Long id);
} 