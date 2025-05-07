/**
 * 用户相关API接口
 */
import request from './request';

/**
 * 用户登录
 * @param {Object} data 登录参数
 * @returns {Promise} 登录结果
 */
export const login = (data) => {
  return request.post('/user/login', data);
};

/**
 * 获取验证码
 * @param {Object} data 手机号码
 * @returns {Promise} 验证码发送结果
 */
export const getVerifyCode = (data) => {
  return request.post('/user/verify-code', data);
};

/**
 * 退出登录
 * @returns {Promise} 退出结果
 */
export const logout = () => {
  return request.post('/user/logout');
};

/**
 * 获取用户信息
 * @returns {Promise} 用户信息
 */
export const getUserInfo = () => {
  return request.get('/user/info');
};

/**
 * 更新用户信息
 * @param {Object} data 用户信息
 * @returns {Promise} 更新结果
 */
export const updateUserInfo = (data) => {
  return request.put('/user/info', data);
};

/**
 * 更新用户头像
 * @param {string} filePath 头像图片路径
 * @returns {Promise} 更新结果
 */
export const updateAvatar = (filePath) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: request.BASE_URL + '/user/avatar',
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