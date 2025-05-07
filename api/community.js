/**
 * 社区相关API接口
 */
import request from './request';

/**
 * 获取社区帖子列表
 * @param {Object} params 查询参数
 * @returns {Promise} 帖子列表
 */
export const getPosts = (params) => {
  return request.get('/community/posts', params);
};

/**
 * 获取推荐的热门话题
 * @returns {Promise} 热门话题列表
 */
export const getHotTopics = () => {
  return request.get('/community/topics/hot');
};

/**
 * 获取帖子详情
 * @param {string} id 帖子ID
 * @returns {Promise} 帖子详情
 */
export const getPostDetail = (id) => {
  return request.get(`/community/posts/${id}`);
};

/**
 * 发布帖子
 * @param {Object} data 帖子数据
 * @returns {Promise} 发布结果
 */
export const createPost = (data) => {
  return request.post('/community/posts', data);
};

/**
 * 上传帖子图片
 * @param {string} filePath 图片路径
 * @returns {Promise} 上传结果，返回图片URL
 */
export const uploadPostImage = (filePath) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: request.BASE_URL + '/community/upload/image',
      filePath,
      name: 'file',
      header: {
        'Authorization': `Bearer ${uni.getStorageSync('token')}`
      },
      success: (res) => {
        try {
          const data = JSON.parse(res.data);
          if (data.code === 0) {
            resolve(data.data);
          } else {
            uni.showToast({
              title: data.message || '上传失败',
              icon: 'none'
            });
            reject(data);
          }
        } catch (error) {
          reject(error);
        }
      },
      fail: (error) => {
        uni.showToast({
          title: '上传失败，请稍后再试',
          icon: 'none'
        });
        reject(error);
      }
    });
  });
};

/**
 * 更新帖子
 * @param {string} id 帖子ID
 * @param {Object} data 帖子数据
 * @returns {Promise} 更新结果
 */
export const updatePost = (id, data) => {
  return request.put(`/community/posts/${id}`, data);
};

/**
 * 删除帖子
 * @param {string} id 帖子ID
 * @returns {Promise} 删除结果
 */
export const deletePost = (id) => {
  return request.delete(`/community/posts/${id}`);
};

/**
 * 点赞帖子
 * @param {string} id 帖子ID
 * @returns {Promise} 点赞结果
 */
export const likePost = (id) => {
  return request.post(`/community/posts/${id}/like`);
};

/**
 * 取消点赞帖子
 * @param {string} id 帖子ID
 * @returns {Promise} 取消点赞结果
 */
export const unlikePost = (id) => {
  return request.delete(`/community/posts/${id}/like`);
};

/**
 * 获取帖子评论列表
 * @param {string} id 帖子ID
 * @param {Object} params 查询参数
 * @returns {Promise} 评论列表
 */
export const getComments = (id, params) => {
  return request.get(`/community/posts/${id}/comments`, params);
};

/**
 * 发布评论
 * @param {string} id 帖子ID
 * @param {Object} data 评论数据
 * @returns {Promise} 发布结果
 */
export const createComment = (id, data) => {
  return request.post(`/community/posts/${id}/comments`, data);
};

/**
 * 删除评论
 * @param {string} postId 帖子ID
 * @param {string} commentId 评论ID
 * @returns {Promise} 删除结果
 */
export const deleteComment = (postId, commentId) => {
  return request.delete(`/community/posts/${postId}/comments/${commentId}`);
};

/**
 * 点赞评论
 * @param {string} postId 帖子ID
 * @param {string} commentId 评论ID
 * @returns {Promise} 点赞结果
 */
export const likeComment = (postId, commentId) => {
  return request.post(`/community/posts/${postId}/comments/${commentId}/like`);
};

/**
 * 取消点赞评论
 * @param {string} postId 帖子ID
 * @param {string} commentId 评论ID
 * @returns {Promise} 取消点赞结果
 */
export const unlikeComment = (postId, commentId) => {
  return request.delete(`/community/posts/${postId}/comments/${commentId}/like`);
};

/**
 * 获取推荐的小组列表
 * @param {Object} params 查询参数
 * @returns {Promise} 小组列表
 */
export const getGroups = (params) => {
  return request.get('/community/groups', params);
};

/**
 * 获取小组详情
 * @param {string} id 小组ID
 * @returns {Promise} 小组详情
 */
export const getGroupDetail = (id) => {
  return request.get(`/community/groups/${id}`);
};

/**
 * 创建小组
 * @param {Object} data 小组数据
 * @returns {Promise} 创建结果
 */
export const createGroup = (data) => {
  return request.post('/community/groups', data);
};

/**
 * 加入小组
 * @param {string} id 小组ID
 * @returns {Promise} 加入结果
 */
export const joinGroup = (id) => {
  return request.post(`/community/groups/${id}/join`);
};

/**
 * 退出小组
 * @param {string} id 小组ID
 * @returns {Promise} 退出结果
 */
export const leaveGroup = (id) => {
  return request.delete(`/community/groups/${id}/join`);
}; 