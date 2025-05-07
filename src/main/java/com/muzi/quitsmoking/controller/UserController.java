package com.muzi.quitsmoking.controller;

import com.muzi.quitsmoking.common.Result;
import com.muzi.quitsmoking.model.dto.LoginDTO;
import com.muzi.quitsmoking.model.dto.UserInfoUpdateDTO;
import com.muzi.quitsmoking.model.dto.VerifyCodeDTO;
import com.muzi.quitsmoking.model.vo.LoginVO;
import com.muzi.quitsmoking.model.vo.UserInfoVO;
import com.muzi.quitsmoking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 获取验证码
     * @param verifyCodeDTO 手机号码
     * @return 验证码发送结果
     */
    @PostMapping("/verify-code")
    public Result<Void> getVerifyCode(@RequestBody @Valid VerifyCodeDTO verifyCodeDTO) {
        userService.sendVerifyCode(verifyCodeDTO.getPhone());
        return Result.success();
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        UserInfoVO userInfoVO = userService.getUserInfo();
        return Result.success(userInfoVO);
    }

    /**
     * 更新用户信息
     * @param userInfoUpdateDTO 用户信息
     * @return 更新结果
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody @Valid UserInfoUpdateDTO userInfoUpdateDTO) {
        userService.updateUserInfo(userInfoUpdateDTO);
        return Result.success();
    }

    /**
     * 更新用户头像
     * @param file 头像图片
     * @return 更新结果
     */
    @PostMapping("/avatar")
    public Result<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.updateAvatar(file);
        return Result.success(avatarUrl);
    }
} 