/**
 * 工具包相关API接口
 */
import request from './request';

/**
 * 获取深呼吸练习列表
 * @returns {Promise} 深呼吸练习列表
 */
export const getBreathingExercises = () => {
  return request.get('/tools/breathing');
};

/**
 * 获取戒烟建议
 * @returns {Promise} 戒烟建议
 */
export const getSmokingTips = () => {
  return request.get('/tools/tips');
};

/**
 * 获取戒烟资讯
 * @param {Object} params 查询参数
 * @returns {Promise} 戒烟资讯列表
 */
export const getArticles = (params) => {
  return request.get('/tools/articles', params);
};

/**
 * 获取文章详情
 * @param {string} id 文章ID
 * @returns {Promise} 文章详情
 */
export const getArticleDetail = (id) => {
  return request.get(`/tools/articles/${id}`);
};

/**
 * 获取紧急求助信息
 * @returns {Promise} 紧急求助信息
 */
export const getEmergencyHelp = () => {
  return request.get('/tools/emergency');
};

/**
 * 提交烟瘾紧急情况
 * @param {Object} data 紧急情况数据
 * @returns {Promise} 提交结果
 */
export const submitEmergency = (data) => {
  return request.post('/tools/emergency', data);
};

/**
 * 获取激励名言
 * @returns {Promise} 激励名言
 */
export const getMotivationalQuotes = () => {
  return request.get('/tools/quotes');
}; 