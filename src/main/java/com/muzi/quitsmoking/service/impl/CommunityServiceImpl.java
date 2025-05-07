package com.muzi.quitsmoking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.mapper.*;
import com.muzi.quitsmoking.model.dto.CommentDTO;
import com.muzi.quitsmoking.model.dto.GroupDTO;
import com.muzi.quitsmoking.model.dto.PostDTO;
import com.muzi.quitsmoking.model.entity.*;
import com.muzi.quitsmoking.model.vo.CommentVO;
import com.muzi.quitsmoking.model.vo.GroupVO;
import com.muzi.quitsmoking.model.vo.PostDetailVO;
import com.muzi.quitsmoking.model.vo.PostVO;
import com.muzi.quitsmoking.model.vo.TopicVO;
import com.muzi.quitsmoking.service.CommunityService;
import com.muzi.quitsmoking.service.UserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 社区服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl extends ServiceImpl<CommunityPostMapper, CommunityPost> implements CommunityService {
    @Resource
    private final UserMapper userMapper;
    @Resource
    private final CommentMapper commentMapper;
    @Resource
    private final LikeRecordMapper likeRecordMapper;
    @Resource
    private final PostImageMapper postImageMapper;
    @Resource
    private final PostTagMapper postTagMapper;
    @Resource
    private final PostTagRelationMapper postTagRelationMapper;
    @Resource
    private final CommunityGroupMapper communityGroupMapper;
    @Resource
    private final GroupMemberMapper groupMemberMapper;
    private final UserService userService;

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    private Integer getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            throw new RuntimeException("用户未登录");
        }
        
        token = token.replace("Bearer ", "");
        // 实际项目中应该从Redis或JWT中获取用户ID
        // 这里简化处理，直接假设1用户
        return 1;
    }

    /**
     * 获取社区帖子列表
     * @param params 查询参数
     * @return 帖子列表和分页信息
     */
    @Override
    public Map<String, Object> getPosts(Map<String, Object> params) {
        // 获取分页参数
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        
        // 构建查询条件
        LambdaQueryWrapper<CommunityPost> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据参数添加筛选条件
        if (params.containsKey("groupId") && params.get("groupId") != null) {
            queryWrapper.eq(CommunityPost::getGroup_id, params.get("groupId"));
        }
        
        if (params.containsKey("userId") && params.get("userId") != null) {
            queryWrapper.eq(CommunityPost::getUser_id, params.get("userId"));
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(CommunityPost::getCreated_at);
        
        // 执行分页查询
        Page<CommunityPost> postPage = page(new Page<>(page, size), queryWrapper);
        
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 获取用户点赞的帖子ID列表
        Set<Integer> likedPostIds = new HashSet<>();
        LambdaQueryWrapper<LikeRecord> likeQueryWrapper = new LambdaQueryWrapper<>();
        likeQueryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "post");
        List<LikeRecord> likeRecords = likeRecordMapper.selectList(likeQueryWrapper);
        for (LikeRecord likeRecord : likeRecords) {
            likedPostIds.add(likeRecord.getTarget_id());
        }
        
        // 转换为VO
        List<PostVO> postVOList = postPage.getRecords().stream()
                .map(post -> convertToPostVO(post, likedPostIds))
                .collect(Collectors.toList());
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("content", postVOList);
        result.put("totalElements", postPage.getTotal());
        result.put("totalPages", postPage.getPages());
        result.put("currentPage", page);
        result.put("size", size);
        
        return result;
    }

    /**
     * 将CommunityPost实体转换为PostVO
     * @param post 帖子实体
     * @param likedPostIds 已点赞的帖子ID集合
     * @return PostVO
     */
    private PostVO convertToPostVO(CommunityPost post, Set<Integer> likedPostIds) {
        PostVO postVO = new PostVO();
        postVO.setId(Long.valueOf(post.getPost_id()));
        postVO.setUserId(Long.valueOf(post.getUser_id()));
        postVO.setContent(post.getContent());
        postVO.setLikeCount(post.getLike_count());
        postVO.setCommentCount(post.getComment_count());
        postVO.setCreateTime(DateUtil.toLocalDateTime(post.getCreated_at()));
        postVO.setIsLiked(likedPostIds.contains(post.getPost_id()));
        
        // 获取用户信息
        User user = userMapper.selectById(post.getUser_id());
        if (user != null) {
            postVO.setNickname(user.getNickname());
            postVO.setAvatar(user.getAvatar());
        }
        
        // 获取帖子图片
        LambdaQueryWrapper<PostImage> imageQueryWrapper = new LambdaQueryWrapper<>();
        imageQueryWrapper.eq(PostImage::getPost_id, post.getPost_id());
        List<PostImage> postImages = postImageMapper.selectList(imageQueryWrapper);
        List<String> images = postImages.stream()
                .map(PostImage::getImage_url)
                .collect(Collectors.toList());
        postVO.setImages(images);
        
        // 获取帖子标签
        LambdaQueryWrapper<PostTagRelation> tagRelationQueryWrapper = new LambdaQueryWrapper<>();
        tagRelationQueryWrapper.eq(PostTagRelation::getPost_id, post.getPost_id());
        List<PostTagRelation> tagRelations = postTagRelationMapper.selectList(tagRelationQueryWrapper);
        
        if (!tagRelations.isEmpty()) {
            List<Integer> tagIds = tagRelations.stream()
                    .map(PostTagRelation::getTag_id)
                    .collect(Collectors.toList());
            
            LambdaQueryWrapper<PostTag> tagQueryWrapper = new LambdaQueryWrapper<>();
            tagQueryWrapper.in(PostTag::getTag_id, tagIds);
            List<PostTag> tags = postTagMapper.selectList(tagQueryWrapper);
            
            List<String> tagNames = tags.stream()
                    .map(PostTag::getTag_name)
                    .collect(Collectors.toList());
            postVO.setTags(tagNames);
        } else {
            postVO.setTags(new ArrayList<>());
        }
        
        return postVO;
    }

    /**
     * 获取推荐的热门话题
     * @return 热门话题列表
     */
    @Override
    public List<TopicVO> getHotTopics() {
        // 查询所有话题标签
        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PostTag::getUse_count);
        queryWrapper.last("LIMIT 10"); // 最多返回10个热门话题
        List<PostTag> postTags = postTagMapper.selectList(queryWrapper);
        
        // 转换为VO
        return postTags.stream().map(tag -> {
            TopicVO topicVO = new TopicVO();
            topicVO.setId(Long.valueOf(tag.getTag_id()));
            topicVO.setName(tag.getTag_name());
            topicVO.setDescription(tag.getDescription());
            topicVO.setIcon(tag.getIcon());
            topicVO.setUseCount(tag.getUse_count());
            topicVO.setIsHot(tag.getIs_hot() == 1);
            return topicVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取帖子详情
     * @param id 帖子ID
     * @return 帖子详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO getPostDetail(Long id) {
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 增加浏览次数
        post.setView_count(post.getView_count() + 1);
        updateById(post);
        
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 判断是否已点赞
        LambdaQueryWrapper<LikeRecord> likeQueryWrapper = new LambdaQueryWrapper<>();
        likeQueryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "post")
                .eq(LikeRecord::getTarget_id, id);
        long likeCount = likeRecordMapper.selectCount(likeQueryWrapper);
        Set<Integer> likedPostIds = new HashSet<>();
        if (likeCount > 0) {
            likedPostIds.add(post.getPost_id());
        }
        
        // 转换为PostVO
        PostDetailVO postDetailVO = new PostDetailVO();
        BeanUtil.copyProperties(convertToPostVO(post, likedPostIds), postDetailVO);
        
        // 获取热门评论
        LambdaQueryWrapper<Comment> commentQueryWrapper = new LambdaQueryWrapper<>();
        commentQueryWrapper.eq(Comment::getPost_id, id)
                .eq(Comment::getStatus, 1) // 只查询正常状态的评论
                .eq(Comment::getParent_id, 0) // 只查询一级评论
                .orderByDesc(Comment::getLike_count)
                .last("LIMIT 3"); // 最多返回3条热门评论
        List<Comment> hotComments = commentMapper.selectList(commentQueryWrapper);
        
        // 获取用户点赞的评论ID列表
        Set<Integer> likedCommentIds = new HashSet<>();
        LambdaQueryWrapper<LikeRecord> commentLikeQueryWrapper = new LambdaQueryWrapper<>();
        commentLikeQueryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "comment");
        List<LikeRecord> commentLikeRecords = likeRecordMapper.selectList(commentLikeQueryWrapper);
        for (LikeRecord likeRecord : commentLikeRecords) {
            likedCommentIds.add(likeRecord.getTarget_id());
        }
        
        // 转换为CommentVO
        List<CommentVO> hotCommentVOs = hotComments.stream().map(comment -> {
            return convertToCommentVO(comment, likedCommentIds);
        }).collect(Collectors.toList());
        
        postDetailVO.setHotComments(hotCommentVOs);
        
        return postDetailVO;
    }

    /**
     * 将Comment实体转换为CommentVO
     * @param comment 评论实体
     * @param likedCommentIds 已点赞的评论ID集合
     * @return CommentVO
     */
    private CommentVO convertToCommentVO(Comment comment, Set<Integer> likedCommentIds) {
        CommentVO commentVO = new CommentVO();
        commentVO.setId(Long.valueOf(comment.getComment_id()));
        commentVO.setUserId(Long.valueOf(comment.getUser_id()));
        commentVO.setContent(comment.getContent());
        commentVO.setLikeCount(comment.getLike_count());
        commentVO.setCreateTime(DateUtil.toLocalDateTime(comment.getCreated_at()));
        commentVO.setIsLiked(likedCommentIds.contains(comment.getComment_id()));
        
        // 获取用户信息
        User user = userMapper.selectById(comment.getUser_id());
        if (user != null) {
            commentVO.setNickname(user.getNickname());
            commentVO.setAvatar(user.getAvatar());
        }
        
        // 获取回复列表
        LambdaQueryWrapper<Comment> replyQueryWrapper = new LambdaQueryWrapper<>();
        replyQueryWrapper.eq(Comment::getParent_id, comment.getComment_id())
                .eq(Comment::getStatus, 1)
                .orderByAsc(Comment::getCreated_at);
        List<Comment> replies = commentMapper.selectList(replyQueryWrapper);
        
        // 转换为ReplyVO
        List<CommentVO.ReplyVO> replyVOs = replies.stream().map(reply -> {
            CommentVO.ReplyVO replyVO = new CommentVO.ReplyVO();
            replyVO.setId(Long.valueOf(reply.getComment_id()));
            replyVO.setUserId(Long.valueOf(reply.getUser_id()));
            replyVO.setContent(reply.getContent());
            replyVO.setCreateTime(DateUtil.toLocalDateTime(reply.getCreated_at()));
            
            // 获取回复用户信息
            User replyUser = userMapper.selectById(reply.getUser_id());
            if (replyUser != null) {
                replyVO.setNickname(replyUser.getNickname());
                replyVO.setAvatar(replyUser.getAvatar());
            }
            
            return replyVO;
        }).collect(Collectors.toList());
        
        commentVO.setReplies(replyVOs);
        commentVO.setReplyCount(replyVOs.size());
        
        return commentVO;
    }

    /**
     * 发布帖子
     * @param postDTO 帖子数据
     * @return 发布结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostVO createPost(PostDTO postDTO) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 如果指定了小组，验证小组是否存在
        if (postDTO.getGroupId() != null) {
            CommunityGroup group = communityGroupMapper.selectById(postDTO.getGroupId().intValue());
            if (group == null) {
                throw new RuntimeException("小组不存在");
            }
            
            // 验证用户是否是小组成员
            LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GroupMember::getGroup_id, postDTO.getGroupId())
                      .eq(GroupMember::getUser_id, currentUserId);
            GroupMember member = groupMemberMapper.selectOne(queryWrapper);
            if (member == null) {
                throw new RuntimeException("您不是该小组成员，无法发帖");
            }
        }
        
        // 创建帖子
        CommunityPost post = new CommunityPost();
        post.setUser_id(currentUserId);
        post.setContent(postDTO.getContent());
        post.setGroup_id(postDTO.getGroupId() != null ? postDTO.getGroupId().intValue() : null);
        post.setPost_type("normal"); // 默认为普通帖子
        post.setStatus(1); // 正常状态
        post.setView_count(0);
        post.setLike_count(0);
        post.setComment_count(0);
        post.setIs_anonymous(0); // 默认不匿名
        post.setCreated_at(new Date());
        post.setUpdated_at(new Date());
        save(post);
        
        // 保存帖子图片
        if (postDTO.getImages() != null && !postDTO.getImages().isEmpty()) {
            for (String imageUrl : postDTO.getImages()) {
                PostImage postImage = new PostImage();
                postImage.setPost_id(post.getPost_id());
                postImage.setImage_url(imageUrl);
                postImage.setCreated_at(new Date());
                postImageMapper.insert(postImage);
            }
        }
        
        // 保存帖子标签
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            for (String tagName : postDTO.getTags()) {
                // 查询标签是否存在
                LambdaQueryWrapper<PostTag> tagQueryWrapper = new LambdaQueryWrapper<>();
                tagQueryWrapper.eq(PostTag::getTag_name, tagName);
                PostTag tag = postTagMapper.selectOne(tagQueryWrapper);
                
                // 如果标签不存在，则创建新标签
                if (tag == null) {
                    tag = new PostTag();
                    tag.setTag_name(tagName);
                    tag.setDescription(tagName);
                    tag.setUse_count(1);
                    tag.setIs_hot(0); // 默认不是热门标签
                    tag.setCreated_at(new Date());
                    postTagMapper.insert(tag);
                } else {
                    // 更新标签使用次数
                    tag.setUse_count(tag.getUse_count() + 1);
                    postTagMapper.updateById(tag);
                }
                
                // 创建标签与帖子的关联
                PostTagRelation tagRelation = new PostTagRelation();
                tagRelation.setPost_id(post.getPost_id());
                tagRelation.setTag_id(tag.getTag_id());
                tagRelation.setCreated_at(new Date());
                postTagRelationMapper.insert(tagRelation);
            }
        }
        
        // 如果帖子属于某个小组，更新小组帖子数
        if (postDTO.getGroupId() != null) {
            CommunityGroup group = communityGroupMapper.selectById(postDTO.getGroupId().intValue());
            if (group != null) {
                group.setPost_count(group.getPost_count() + 1);
                communityGroupMapper.updateById(group);
            }
        }
        
        // 获取用户点赞的帖子ID列表
        Set<Integer> likedPostIds = new HashSet<>();
        
        // 转换为VO并返回
        return convertToPostVO(post, likedPostIds);
    }

    /**
     * 上传帖子图片
     * @param file 图片文件
     * @return 上传结果，返回图片URL
     */
    @Override
    public String uploadPostImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        
        // TODO: 上传图片到OSS或其他文件存储，获取URL
        String imageUrl = "https://example.com/images/" + UUID.randomUUID().toString() + ".jpg";
        
        return imageUrl;
    }

    /**
     * 更新帖子
     * @param id 帖子ID
     * @param postDTO 帖子数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePost(Long id, PostDTO postDTO) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 验证权限
        if (!post.getUser_id().equals(currentUserId)) {
            throw new RuntimeException("无权修改此帖子");
        }
        
        // 更新帖子内容
        post.setContent(postDTO.getContent());
        post.setUpdated_at(new Date());
        updateById(post);
        
        // 更新帖子图片
        // 先删除原有图片
        LambdaQueryWrapper<PostImage> imageQueryWrapper = new LambdaQueryWrapper<>();
        imageQueryWrapper.eq(PostImage::getPost_id, id);
        postImageMapper.delete(imageQueryWrapper);
        
        // 保存新图片
        if (postDTO.getImages() != null && !postDTO.getImages().isEmpty()) {
            for (String imageUrl : postDTO.getImages()) {
                PostImage postImage = new PostImage();
                postImage.setPost_id(id.intValue());
                postImage.setImage_url(imageUrl);
                postImage.setCreated_at(new Date());
                postImageMapper.insert(postImage);
            }
        }
        
        // 更新帖子标签
        // 先删除原有标签关联
        LambdaQueryWrapper<PostTagRelation> tagRelationQueryWrapper = new LambdaQueryWrapper<>();
        tagRelationQueryWrapper.eq(PostTagRelation::getPost_id, id);
        List<PostTagRelation> oldTagRelations = postTagRelationMapper.selectList(tagRelationQueryWrapper);
        
        // 收集原有标签ID
        List<Integer> oldTagIds = oldTagRelations.stream()
                .map(PostTagRelation::getTag_id)
                .collect(Collectors.toList());
        
        // 删除原有标签关联
        postTagRelationMapper.delete(tagRelationQueryWrapper);
        
        // 更新原有标签使用次数
        for (Integer tagId : oldTagIds) {
            PostTag tag = postTagMapper.selectById(tagId);
            if (tag != null && tag.getUse_count() > 0) {
                tag.setUse_count(tag.getUse_count() - 1);
                postTagMapper.updateById(tag);
            }
        }
        
        // 保存新标签
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            for (String tagName : postDTO.getTags()) {
                // 查询标签是否存在
                LambdaQueryWrapper<PostTag> tagQueryWrapper = new LambdaQueryWrapper<>();
                tagQueryWrapper.eq(PostTag::getTag_name, tagName);
                PostTag tag = postTagMapper.selectOne(tagQueryWrapper);
                
                // 如果标签不存在，则创建新标签
                if (tag == null) {
                    tag = new PostTag();
                    tag.setTag_name(tagName);
                    tag.setDescription(tagName);
                    tag.setUse_count(1);
                    tag.setIs_hot(0); // 默认不是热门标签
                    tag.setCreated_at(new Date());
                    postTagMapper.insert(tag);
                } else {
                    // 更新标签使用次数
                    tag.setUse_count(tag.getUse_count() + 1);
                    postTagMapper.updateById(tag);
                }
                
                // 创建标签与帖子的关联
                PostTagRelation tagRelation = new PostTagRelation();
                tagRelation.setPost_id(id.intValue());
                tagRelation.setTag_id(tag.getTag_id());
                tagRelation.setCreated_at(new Date());
                postTagRelationMapper.insert(tagRelation);
            }
        }
    }

    /**
     * 删除帖子
     * @param id 帖子ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 验证权限
        if (!post.getUser_id().equals(currentUserId)) {
            throw new RuntimeException("无权删除此帖子");
        }
        
        // 更新帖子状态为已删除
        post.setStatus(0); // 0表示删除
        updateById(post);
        
        // 如果帖子属于某个小组，更新小组帖子数
        if (post.getGroup_id() != null) {
            CommunityGroup group = communityGroupMapper.selectById(post.getGroup_id());
            if (group != null && group.getPost_count() > 0) {
                group.setPost_count(group.getPost_count() - 1);
                communityGroupMapper.updateById(group);
            }
        }
        
        // 收集帖子的标签ID
        LambdaQueryWrapper<PostTagRelation> tagRelationQueryWrapper = new LambdaQueryWrapper<>();
        tagRelationQueryWrapper.eq(PostTagRelation::getPost_id, id);
        List<PostTagRelation> tagRelations = postTagRelationMapper.selectList(tagRelationQueryWrapper);
        
        // 更新标签使用次数
        for (PostTagRelation tagRelation : tagRelations) {
            PostTag tag = postTagMapper.selectById(tagRelation.getTag_id());
            if (tag != null && tag.getUse_count() > 0) {
                tag.setUse_count(tag.getUse_count() - 1);
                postTagMapper.updateById(tag);
            }
        }
    }

    /**
     * 点赞帖子
     * @param id 帖子ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likePost(Long id) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 检查是否已点赞
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "post")
                .eq(LikeRecord::getTarget_id, id);
        long count = likeRecordMapper.selectCount(queryWrapper);
        
        if (count > 0) {
            throw new RuntimeException("已点赞过该帖子");
        }
        
        // 创建点赞记录
        LikeRecord likeRecord = new LikeRecord();
        likeRecord.setUser_id(currentUserId);
        likeRecord.setType("post");
        likeRecord.setTarget_id(id.intValue());
        likeRecord.setCreated_at(new Date());
        likeRecordMapper.insert(likeRecord);
        
        // 更新帖子点赞数
        post.setLike_count(post.getLike_count() + 1);
        updateById(post);
    }

    /**
     * 取消点赞帖子
     * @param id 帖子ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlikePost(Long id) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 删除点赞记录
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "post")
                .eq(LikeRecord::getTarget_id, id);
        long count = likeRecordMapper.delete(queryWrapper);
        
        if (count == 0) {
            throw new RuntimeException("未点赞过该帖子");
        }
        
        // 更新帖子点赞数
        post.setLike_count(post.getLike_count() - 1);
        updateById(post);
    }

    /**
     * 获取帖子评论列表
     * @param id 帖子ID
     * @param params 查询参数
     * @return 评论列表和分页信息
     */
    @Override
    public Map<String, Object> getComments(Long id, Map<String, Object> params) {
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getPost_id, id)
                .eq(Comment::getStatus, 1) // 只查询正常状态的评论
                .eq(Comment::getParent_id, 0) // 只查询一级评论
                .orderByDesc(Comment::getCreated_at);
        
        // 设置分页参数
        int current = 1;
        int size = 10;
        if (params != null && params.containsKey("page")) {
            current = Integer.parseInt(params.get("page").toString());
        }
        if (params != null && params.containsKey("size")) {
            size = Integer.parseInt(params.get("size").toString());
        }
        
        // 查询评论列表
        Page<Comment> page = new Page<>(current, size);
        Page<Comment> commentPage = commentMapper.selectPage(page, queryWrapper);
        
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 获取用户点赞的评论ID列表
        Set<Integer> likedCommentIds = new HashSet<>();
        LambdaQueryWrapper<LikeRecord> likeQueryWrapper = new LambdaQueryWrapper<>();
        likeQueryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "comment");
        List<LikeRecord> likeRecords = likeRecordMapper.selectList(likeQueryWrapper);
        for (LikeRecord likeRecord : likeRecords) {
            likedCommentIds.add(likeRecord.getTarget_id());
        }
        
        // 转换为VO
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> {
            return convertToCommentVO(comment, likedCommentIds);
        }).collect(Collectors.toList());
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", commentVOList);
        result.put("total", commentPage.getTotal());
        result.put("pages", commentPage.getPages());
        result.put("current", commentPage.getCurrent());
        result.put("size", commentPage.getSize());
        
        return result;
    }

    /**
     * 发表评论
     * @param id 帖子ID
     * @param commentDTO 评论数据
     * @return 评论VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO createComment(Long id, CommentDTO commentDTO) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询帖子
        CommunityPost post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 创建评论
        Comment comment = new Comment();
        comment.setPost_id(id.intValue());
        comment.setUser_id(currentUserId);
        comment.setContent(commentDTO.getContent());
        
        // 处理回复评论
        if (commentDTO.getParentId() != null) {
            comment.setParent_id(commentDTO.getParentId().intValue());
            
            // 检查父评论是否存在
            Comment parentComment = commentMapper.selectById(commentDTO.getParentId());
            if (parentComment == null) {
                throw new RuntimeException("回复的评论不存在");
            }
            
            // 确保父评论属于这个帖子
            if (!parentComment.getPost_id().equals(id.intValue())) {
                throw new RuntimeException("回复的评论不属于该帖子");
            }
        } else {
            comment.setParent_id(0); // 一级评论
        }
        
        comment.setLike_count(0);
        comment.setIs_anonymous(commentDTO.getIsAnonymous() ? 1 : 0);
        comment.setStatus(1); // 正常状态
        comment.setCreated_at(new Date());
        commentMapper.insert(comment);
        
        // 更新帖子评论数
        post.setComment_count(post.getComment_count() + 1);
        updateById(post);
        
        // 获取用户点赞的评论ID列表
        Set<Integer> likedCommentIds = new HashSet<>();
        
        // 转换为VO并返回
        return convertToCommentVO(comment, likedCommentIds);
    }

    /**
     * 删除评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long postId, Long commentId) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询评论
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        // 验证评论是否属于该帖子
        if (!comment.getPost_id().equals(postId.intValue())) {
            throw new RuntimeException("评论不属于该帖子");
        }
        
        // 验证权限
        if (!comment.getUser_id().equals(currentUserId)) {
            throw new RuntimeException("无权删除此评论");
        }
        
        // 更新评论状态为已删除
        comment.setStatus(0); // 0表示删除
        commentMapper.updateById(comment);
        
        // 更新帖子评论数
        CommunityPost post = getById(postId);
        if (post != null && post.getComment_count() > 0) {
            post.setComment_count(post.getComment_count() - 1);
            updateById(post);
        }
    }

    /**
     * 点赞评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(Long postId, Long commentId) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询评论
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        // 验证评论是否属于该帖子
        if (!comment.getPost_id().equals(postId.intValue())) {
            throw new RuntimeException("评论不属于该帖子");
        }
        
        // 检查是否已点赞
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "comment")
                .eq(LikeRecord::getTarget_id, commentId);
        long count = likeRecordMapper.selectCount(queryWrapper);
        
        if (count > 0) {
            throw new RuntimeException("已点赞过该评论");
        }
        
        // 创建点赞记录
        LikeRecord likeRecord = new LikeRecord();
        likeRecord.setUser_id(currentUserId);
        likeRecord.setType("comment");
        likeRecord.setTarget_id(commentId.intValue());
        likeRecord.setCreated_at(new Date());
        likeRecordMapper.insert(likeRecord);
        
        // 更新评论点赞数
        comment.setLike_count(comment.getLike_count() + 1);
        commentMapper.updateById(comment);
    }

    /**
     * 取消点赞评论
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlikeComment(Long postId, Long commentId) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询评论
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        // 验证评论是否属于该帖子
        if (!comment.getPost_id().equals(postId.intValue())) {
            throw new RuntimeException("评论不属于该帖子");
        }
        
        // 删除点赞记录
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "comment")
                .eq(LikeRecord::getTarget_id, commentId);
        long count = likeRecordMapper.delete(queryWrapper);
        
        if (count == 0) {
            throw new RuntimeException("未点赞过该评论");
        }
        
        // 更新评论点赞数
        comment.setLike_count(comment.getLike_count() - 1);
        commentMapper.updateById(comment);
    }

    /**
     * 获取社区小组列表
     * @param params 查询参数
     * @return 小组列表和分页信息
     */
    @Override
    public Map<String, Object> getGroups(Map<String, Object> params) {
        // 构建查询条件
        LambdaQueryWrapper<CommunityGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommunityGroup::getStatus, 1); // 只查询正常状态的小组
        
        // 处理查询参数
        if (params != null) {
            // 按关键词搜索
            if (params.containsKey("keyword")) {
                String keyword = params.get("keyword").toString();
                queryWrapper.like(CommunityGroup::getName, keyword)
                        .or()
                        .like(CommunityGroup::getDescription, keyword);
            }
            
            // 按分类筛选
            if (params.containsKey("categoryId")) {
                Integer categoryId = Integer.parseInt(params.get("categoryId").toString());
                queryWrapper.eq(CommunityGroup::getCategory_id, categoryId);
            }
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
        
        // 按成员数量降序排序
        queryWrapper.orderByDesc(CommunityGroup::getMember_count);
        
        // 查询小组列表
        Page<CommunityGroup> page = new Page<>(current, size);
        Page<CommunityGroup> groupPage = communityGroupMapper.selectPage(page, queryWrapper);
        
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 获取用户加入的小组ID列表
        Set<Integer> joinedGroupIds = new HashSet<>();
        LambdaQueryWrapper<GroupMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.eq(GroupMember::getUser_id, currentUserId);
        List<GroupMember> groupMembers = groupMemberMapper.selectList(memberQueryWrapper);
        for (GroupMember groupMember : groupMembers) {
            joinedGroupIds.add(groupMember.getGroup_id());
        }
        
        // 转换为VO
        List<GroupVO> groupVOList = groupPage.getRecords().stream().map(group -> {
            return convertToGroupVO(group, joinedGroupIds);
        }).collect(Collectors.toList());
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", groupVOList);
        result.put("total", groupPage.getTotal());
        result.put("pages", groupPage.getPages());
        result.put("current", groupPage.getCurrent());
        result.put("size", groupPage.getSize());
        
        return result;
    }

    /**
     * 将CommunityGroup实体转换为GroupVO
     * @param group 小组实体
     * @param joinedGroupIds 已加入的小组ID集合
     * @return GroupVO
     */
    private GroupVO convertToGroupVO(CommunityGroup group, Set<Integer> joinedGroupIds) {
        GroupVO groupVO = new GroupVO();
        groupVO.setId(Long.valueOf(group.getGroup_id()));
        groupVO.setName(group.getName());
        groupVO.setDescription(group.getDescription());
        groupVO.setLogo(group.getLogo());
        groupVO.setCoverImage(group.getCover_image());
        groupVO.setMemberCount(group.getMember_count());
        groupVO.setPostCount(group.getPost_count());
        groupVO.setIsJoined(joinedGroupIds.contains(group.getGroup_id()));
        
        return groupVO;
    }

    /**
     * 获取小组详情
     * @param id 小组ID
     * @return 小组详情
     */
    @Override
    public GroupVO getGroupDetail(Long id) {
        // 查询小组
        CommunityGroup group = communityGroupMapper.selectById(id);
        if (group == null) {
            throw new RuntimeException("小组不存在");
        }
        
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 判断是否已加入
        LambdaQueryWrapper<GroupMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.eq(GroupMember::getUser_id, currentUserId)
                .eq(GroupMember::getGroup_id, id);
        long count = groupMemberMapper.selectCount(memberQueryWrapper);
        Set<Integer> joinedGroupIds = new HashSet<>();
        if (count > 0) {
            joinedGroupIds.add(id.intValue());
        }
        
        // 转换为VO
        GroupVO groupVO = convertToGroupVO(group, joinedGroupIds);
        
        // 获取前3个帖子
        LambdaQueryWrapper<CommunityPost> postQueryWrapper = new LambdaQueryWrapper<>();
        postQueryWrapper.eq(CommunityPost::getGroup_id, id)
                .eq(CommunityPost::getStatus, 1)
                .orderByDesc(CommunityPost::getCreated_at)
                .last("LIMIT 3");
        List<CommunityPost> recentPosts = list(postQueryWrapper);
        
        // 获取用户点赞的帖子ID列表
        Set<Integer> likedPostIds = new HashSet<>();
        LambdaQueryWrapper<LikeRecord> likeQueryWrapper = new LambdaQueryWrapper<>();
        likeQueryWrapper.eq(LikeRecord::getUser_id, currentUserId)
                .eq(LikeRecord::getType, "post");
        List<LikeRecord> likeRecords = likeRecordMapper.selectList(likeQueryWrapper);
        for (LikeRecord likeRecord : likeRecords) {
            likedPostIds.add(likeRecord.getTarget_id());
        }
        
        // 转换为PostVO
        List<PostVO> recentPostVOs = recentPosts.stream().map(post -> {
            return convertToPostVO(post, likedPostIds);
        }).collect(Collectors.toList());
        
        groupVO.setRecentPosts(recentPostVOs);
        
        return groupVO;
    }

    /**
     * 创建小组
     * @param groupDTO 小组数据
     * @return 小组VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupVO createGroup(GroupDTO groupDTO) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 检查小组名称是否已存在
        LambdaQueryWrapper<CommunityGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommunityGroup::getName, groupDTO.getName());
        long count = communityGroupMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("小组名称已存在");
        }
        
        // 创建小组
        CommunityGroup group = new CommunityGroup();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        group.setLogo(groupDTO.getLogo());
        group.setCover_image(groupDTO.getCoverImage());
        group.setCategory_id(groupDTO.getCategoryId() != null ? groupDTO.getCategoryId() : null);
        group.setCreator_id(currentUserId);
        group.setMember_count(1); // 创建者自动成为成员
        group.setPost_count(0);
        group.setStatus(1); // 正常状态
        group.setCreated_at(new Date());
        group.setUpdated_at(new Date());
        communityGroupMapper.insert(group);
        
        // 创建者自动加入小组
        GroupMember member = new GroupMember();
        member.setGroup_id(group.getGroup_id());
        member.setUser_id(currentUserId);
        member.setRole("admin"); // 创建者为管理员
        member.setJoined_at(new Date());
        groupMemberMapper.insert(member);
        
        // 获取用户加入的小组ID列表
        Set<Integer> joinedGroupIds = new HashSet<>();
        joinedGroupIds.add(group.getGroup_id());
        
        // 转换为VO并返回
        return convertToGroupVO(group, joinedGroupIds);
    }

    /**
     * 加入小组
     * @param id 小组ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinGroup(Long id) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询小组
        CommunityGroup group = communityGroupMapper.selectById(id);
        if (group == null) {
            throw new RuntimeException("小组不存在");
        }
        
        // 检查是否已加入
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getUser_id, currentUserId)
                .eq(GroupMember::getGroup_id, id);
        long count = groupMemberMapper.selectCount(queryWrapper);
        
        if (count > 0) {
            throw new RuntimeException("已加入该小组");
        }
        
        // 加入小组
        GroupMember member = new GroupMember();
        member.setGroup_id(id.intValue());
        member.setUser_id(currentUserId);
        member.setRole("member"); // 普通成员
        member.setJoined_at(new Date());
        groupMemberMapper.insert(member);
        
        // 更新小组成员数
        group.setMember_count(group.getMember_count() + 1);
        communityGroupMapper.updateById(group);
    }

    /**
     * 退出小组
     * @param id 小组ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveGroup(Long id) {
        // 获取当前登录用户ID
        Integer currentUserId = getCurrentUserId();
        
        // 查询小组
        CommunityGroup group = communityGroupMapper.selectById(id);
        if (group == null) {
            throw new RuntimeException("小组不存在");
        }
        
        // 检查是否为小组创建者
        if (group.getCreator_id().equals(currentUserId)) {
            throw new RuntimeException("小组创建者不能退出小组");
        }
        
        // 删除成员记录
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getUser_id, currentUserId)
                .eq(GroupMember::getGroup_id, id);
        long count = groupMemberMapper.delete(queryWrapper);
        
        if (count == 0) {
            throw new RuntimeException("未加入该小组");
        }
        
        // 更新小组成员数
        group.setMember_count(group.getMember_count() - 1);
        communityGroupMapper.updateById(group);
    }
} 