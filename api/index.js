/**
 * API统一导出文件
 */
import * as userApi from './user';
import * as smokingApi from './smoking';
import * as communityApi from './community';
import * as toolsApi from './tools';

export default {
  user: userApi,
  smoking: smokingApi,
  community: communityApi,
  tools: toolsApi
}; 