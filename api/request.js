/**
 * 统一请求模块
 * 基于uni.request封装，特别优化微信小程序环境
 */

// 直接导入模拟服务
import mockService from '../utils/mock-service';

// 基本配置
const CONFIG = {
  // 服务器地址，改为本地开发环境或模拟数据
  BASE_URL: 'http://localhost:3000/api',
  // 请求超时时间
  TIMEOUT: 10000,
  // 是否启用模拟数据服务
  USE_MOCK: true,
  // 是否在微信小程序环境
  IS_MP_WEIXIN: false
};

// 判断当前环境
// #ifdef MP-WEIXIN
CONFIG.IS_MP_WEIXIN = true;
// #endif

// 导出BASE_URL以便其他模块使用
export const API_BASE_URL = CONFIG.BASE_URL;

// 初始化模拟服务
if (CONFIG.USE_MOCK) {
  mockService.setupMockService();
}

// 存储最近错误时间，用于防抖处理
let lastErrorTime = 0;

/**
 * 安全显示提示
 * @param {Object} options Toast配置
 */
const safeShowToast = (options) => {
  try {
    // 微信小程序环境需要延迟执行
    if (CONFIG.IS_MP_WEIXIN) {
      setTimeout(() => {
        try {
          uni.showToast(options);
        } catch (e) {
          console.warn('显示提示失败(延迟):', e);
        }
      }, 100);
    } else {
      uni.showToast(options);
    }
  } catch (e) {
    console.warn('显示提示失败:', e);
  }
};

/**
 * 安全显示错误提示
 * @param {string} message 错误信息
 */
const safeShowError = (message) => {
  // 防抖处理，避免短时间内多次显示
  const now = Date.now();
  if (now - lastErrorTime < 2000) {
    return;
  }
  
  lastErrorTime = now;
  
  safeShowToast({
    title: message,
    icon: 'none',
    duration: 2000
  });
};

/**
 * 请求拦截器
 * @param {Object} config 请求配置
 * @returns {Object} 处理后的请求配置
 */
const requestInterceptor = (config) => {
  // 获取token
  const token = uni.getStorageSync('token');
  
  // 添加token到header
  if (token) {
    config.header = {
      ...config.header,
      'Authorization': `Bearer ${token}`
    };
  }
  
  // 添加公共请求头
  config.header = {
    ...config.header,
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  };
  
  return config;
};

/**
 * 响应拦截器
 * @param {Object} response 响应数据
 * @returns {Object} 处理后的响应数据
 */
const responseInterceptor = (response) => {
  // 状态码200表示成功
  if (response.statusCode === 200) {
    // 业务状态码判断
    if (response.data.code === 0) {
      return response.data.data;
    } else {
      // 业务错误提示
      safeShowToast({
        title: response.data.message || '请求失败',
        icon: 'none'
      });
      return Promise.reject(response.data);
    }
  } else if (response.statusCode === 401) {
    // 未授权，跳转到登录页
    safeShowToast({
      title: '登录已过期，请重新登录',
      icon: 'none'
    });
    
    // 清除token
    uni.removeStorageSync('token');
    
    // 跳转到登录页
    setTimeout(() => {
      uni.reLaunch({
        url: '/pages/login/login'
      });
    }, 1500);
    
    return Promise.reject(response.data);
  } else {
    // 其他错误
    safeShowToast({
      title: '网络错误，请稍后再试',
      icon: 'none'
    });
    return Promise.reject(response.data);
  }
};

/**
 * 错误处理器
 * @param {Object} error 错误信息
 * @returns {Promise} 错误信息
 */
const errorHandler = (error) => {
  // 检查是否是网络连接错误
  if (error.errMsg && (error.errMsg.includes('fail') || error.errMsg.includes('Failed to fetch'))) {
    // 提供更具体的错误信息
    const errorMsg = error.errMsg.includes('timeout') 
      ? '请求超时，请检查网络连接'
      : '网络连接异常，请检查网络设置';
      
    // 记录错误时间
    uni.setStorageSync('lastNetworkErrorTime', Date.now());
    
    // 显示错误信息，使用防抖
    safeShowError(errorMsg);
    
    console.error('网络请求错误:', error);
  } else {
    // 其他错误提示
    safeShowError('网络异常，请检查网络连接');
  }
  return Promise.reject(error);
};

/**
 * 模拟请求处理函数
 * @param {Object} options 请求配置
 * @returns {Promise<Object>} 请求结果
 */
const handleMockRequest = async (options) => {
  try {
    // 调用模拟服务获取响应
    const mockResponse = await mockService.mockRequest(options);
    
    // 检查响应状态
    if (mockResponse.code === 0) {
      return mockResponse.data;
    } else {
      // 业务错误，显示提示
      const errorMessage = mockResponse.message || '请求失败';
      safeShowError(errorMessage);
      
      return Promise.reject(mockResponse);
    }
  } catch (error) {
    console.error('模拟请求处理出错:', error);
    safeShowError('网络请求失败');
    return Promise.reject(error);
  }
};

/**
 * 发送真实请求
 * @param {Object} options 请求配置
 * @returns {Promise<Object>} 请求结果
 */
const sendRealRequest = (options) => {
  return new Promise((resolve, reject) => {
    // uni.request会返回一个requestTask对象
    uni.request({
      ...options,
      success: (res) => {
        try {
          // 响应拦截
          const data = responseInterceptor(res);
          resolve(data);
        } catch (error) {
          reject(error);
        }
      },
      fail: (error) => {
        // 错误处理
        errorHandler(error);
        reject(error);
      }
    });
  });
};

/**
 * 处理请求
 * @param {Object} options 请求配置
 * @returns {Promise<Object>} 请求结果
 */
const processRequest = async (options) => {
  // 请求拦截
  const interceptedConfig = requestInterceptor(options);
  
  // 如果启用了模拟服务
  if (CONFIG.USE_MOCK) {
    return handleMockRequest(interceptedConfig);
  }
  
  // 发送真实请求
  return sendRealRequest(interceptedConfig);
};

/**
 * 发送请求
 * @param {Object} options 请求配置
 * @returns {Promise} 响应结果
 */
const request = (options) => {
  // 合并请求配置
  const config = {
    url: `${CONFIG.BASE_URL}${options.url}`,
    method: options.method || 'GET',
    data: options.data || {},
    header: options.header || {},
    timeout: CONFIG.TIMEOUT
  };
  
  // 处理请求
  return processRequest(config);
};

/**
 * GET请求
 * @param {string} url 请求路径
 * @param {Object} data 请求参数
 * @param {Object} header 请求头
 * @returns {Promise} 响应结果
 */
const get = (url, data = {}, header = {}) => {
  return request({
    url,
    method: 'GET',
    data,
    header
  });
};

/**
 * POST请求
 * @param {string} url 请求路径
 * @param {Object} data 请求参数
 * @param {Object} header 请求头
 * @returns {Promise} 响应结果
 */
const post = (url, data = {}, header = {}) => {
  return request({
    url,
    method: 'POST',
    data,
    header
  });
};

/**
 * PUT请求
 * @param {string} url 请求路径
 * @param {Object} data 请求参数
 * @param {Object} header 请求头
 * @returns {Promise} 响应结果
 */
const put = (url, data = {}, header = {}) => {
  return request({
    url,
    method: 'PUT',
    data,
    header
  });
};

/**
 * DELETE请求
 * @param {string} url 请求路径
 * @param {Object} data 请求参数
 * @param {Object} header 请求头
 * @returns {Promise} 响应结果
 */
const del = (url, data = {}, header = {}) => {
  return request({
    url,
    method: 'DELETE',
    data,
    header
  });
};

// 导出请求方法和配置信息
export default {
  request,
  get,
  post,
  put,
  delete: del,
  BASE_URL: CONFIG.BASE_URL,
  USE_MOCK: CONFIG.USE_MOCK,
  IS_MP_WEIXIN: CONFIG.IS_MP_WEIXIN
}; 