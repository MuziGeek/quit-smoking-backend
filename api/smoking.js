/**
 * 戒烟相关API接口
 */
import request from './request';

/**
 * 获取戒烟统计数据
 * @returns {Promise} 戒烟统计数据
 */
export const getSmokingStats = () => {
  return request.get('/smoking/stats');
};

/**
 * 获取戒烟打卡记录
 * @param {Object} params 查询参数
 * @returns {Promise} 打卡记录列表
 */
export const getCheckinRecords = (params) => {
  return request.get('/smoking/checkin', params);
};

/**
 * 提交每日打卡
 * @param {Object} data 打卡数据
 * @returns {Promise} 打卡结果
 */
export const submitCheckin = (data) => {
  return request.post('/smoking/checkin', data);
};

/**
 * 获取成就列表
 * @returns {Promise} 成就列表
 */
export const getAchievements = () => {
  return request.get('/smoking/achievements');
};

/**
 * 获取戒烟时间线
 * @returns {Promise} 戒烟时间线
 */
export const getTimeline = () => {
  return request.get('/smoking/timeline');
};

/**
 * 获取戒烟健康状况
 * @returns {Promise} 健康状况
 */
export const getHealthStatus = () => {
  return request.get('/smoking/health');
};

/**
 * 设置戒烟目标
 * @param {Object} data 目标数据
 * @returns {Promise} 设置结果
 */
export const setSmokingGoal = (data) => {
  return request.post('/smoking/goal', data);
};

/**
 * 获取戒烟目标
 * @returns {Promise} 戒烟目标
 */
export const getSmokingGoal = () => {
  return request.get('/smoking/goal');
}; 