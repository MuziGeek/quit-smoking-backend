package com.muzi.quitsmoking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzi.quitsmoking.model.dto.LoginDTO;
import com.muzi.quitsmoking.model.dto.UserInfoUpdateDTO;
import com.muzi.quitsmoking.model.entity.User;
import com.muzi.quitsmoking.model.vo.LoginVO;
import com.muzi.quitsmoking.model.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author ysh
* @description 针对表【user(用户基本信息表)】的数据库操作Service
* @createDate 2025-04-25 15:38:26
*/
public interface UserService extends IService<User> {

    /**
     * 发送验证码
     * @param phone 手机号码
     */
    void sendVerifyCode(String phone);

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取用户信息
     * @return 用户信息
     */
    UserInfoVO getUserInfo();

    /**
     * 更新用户信息
     * @param userInfoUpdateDTO 用户信息
     */
    void updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO);

    /**
     * 更新用户头像
     * @param file 头像图片
     * @return 头像URL
     */
    String updateAvatar(MultipartFile file);
    
    /**
     * 更新用户积分
     * @param userId 用户ID
     * @param points 积分变化值，正数为增加，负数为减少
     * @param reason 积分变化原因
     * @return 更新后的积分
     */
    Integer updateUserPoints(Long userId, Integer points, String reason);
    



    /**
     * 获取用户节省金额
     *
     * @param userId 用户ID
     * @return 节省金额
     */
    Double getUserSavedMoney(Long userId);
}
