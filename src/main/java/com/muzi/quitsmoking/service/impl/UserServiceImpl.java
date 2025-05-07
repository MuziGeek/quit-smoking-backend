package com.muzi.quitsmoking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzi.quitsmoking.exception.BusinessException;
import com.muzi.quitsmoking.exception.ErrorCode;
import com.muzi.quitsmoking.mapper.SmokingRecordMapper;
import com.muzi.quitsmoking.mapper.UserMapper;
import com.muzi.quitsmoking.model.dto.LoginDTO;
import com.muzi.quitsmoking.model.dto.UserInfoUpdateDTO;
import com.muzi.quitsmoking.model.entity.SmokingRecord;
import com.muzi.quitsmoking.model.entity.User;
import com.muzi.quitsmoking.model.vo.LoginVO;
import com.muzi.quitsmoking.model.vo.UserInfoVO;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private SmokingRecordMapper smokingRecordMapper;

    // 模拟验证码存储，实际项目应使用Redis
    private static final Map<String, String> CODE_MAP = new HashMap<>();
    // 模拟token存储，实际项目应使用Redis或JWT
    private static final Map<String, Integer> TOKEN_MAP = new HashMap<>();

    /**
     * 发送验证码
     * @param phone 手机号码
     */
    @Override
    public void sendVerifyCode(String phone) {
        // 生成6位随机验证码
        String code = RandomUtil.randomNumbers(6);
        // 存储验证码，实际项目中应该发送短信并存储到Redis
        CODE_MAP.put(phone, code);
        log.info("手机号{}的验证码为：{}", phone, code);
    }

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO loginDTO) {
        String phone = loginDTO.getPhone();
        String code = loginDTO.getCode();
        
        // 验证验证码
        String storedCode = CODE_MAP.get(phone);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new BusinessException(ErrorCode.USER_VERIFY_CODE_ERROR, "验证码错误");
        }
        
        // 验证通过后，清除验证码
        CODE_MAP.remove(phone);
        
        // 查询用户是否存在
        User user = lambdaQuery().eq(User::getPhone, phone).one();
        boolean isNewUser = false;
        
        // 不存在则创建新用户
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setNickname("用户" + RandomUtil.randomString(6));
            user.setGender(0);
            user.setRegister_time(new Date());
            user.setLast_login_time(new Date());
            user.setStatus(1);
            user.setLevel(1);
            user.setPoints(0);
            save(user);
            isNewUser = true;
        } else {
            // 更新最后登录时间
            user.setLast_login_time(new Date());
            updateById(user);
        }
        
        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_MAP.put(token, user.getUser_id());
        
        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(Long.valueOf(user.getUser_id()));
        loginVO.setNickname(user.getNickname());
        loginVO.setAvatar(user.getAvatar());
        loginVO.setIsNewUser(isNewUser);
        
        return loginVO;
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(token)) {
            token = token.replace("Bearer ", "");
            TOKEN_MAP.remove(token);
        }
    }

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    private Integer getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        
        token = token.replace("Bearer ", "");
        Integer userId = TOKEN_MAP.get(token);
        if (userId == null) {
            throw new BusinessException(ErrorCode.USER_TOKEN_EXPIRED, "用户未登录或登录已过期");
        }
        
        return userId;
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @Override
    public UserInfoVO getUserInfo() {
        Integer userId = getCurrentUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO, "id", "phone", "quitDate", "createTime");
        userInfoVO.setId(Long.valueOf(user.getUser_id()));
        userInfoVO.setPhone(user.getPhone());
        userInfoVO.setCreateTime(DateUtil.toLocalDateTime(user.getRegister_time()));
        
        // TODO: 从其他表获取戒烟相关数据
        
        return userInfoVO;
    }

    /**
     * 更新用户信息
     * @param userInfoUpdateDTO 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO) {
        Integer userId = getCurrentUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        
        if (StrUtil.isNotBlank(userInfoUpdateDTO.getNickname())) {
            user.setNickname(userInfoUpdateDTO.getNickname());
        }
        
        if (userInfoUpdateDTO.getGender() != null) {
            user.setGender(userInfoUpdateDTO.getGender());
        }
        
        // TODO: 更新其他表中的戒烟相关数据
        
        updateById(user);
    }

    /**
     * 更新用户头像
     * @param file 头像图片
     * @return 头像URL
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAvatar(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "上传文件不能为空");
        }
        
        // TODO: 上传头像到OSS或其他文件存储，获取URL
        String avatarUrl = "https://example.com/avatar/" + UUID.randomUUID().toString() + ".jpg";
        
        // 更新用户头像
        Integer userId = getCurrentUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        
        user.setAvatar(avatarUrl);
        updateById(user);
        
        return avatarUrl;
    }

    /**
     * 更新用户积分
     * @param userId 用户ID
     * @param points 积分变化值，正数为增加，负数为减少
     * @param reason 积分变化原因
     * @return 更新后的积分
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserPoints(Long userId, Integer points, String reason) {
        // 检查用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 计算更新后的积分
        Integer currentPoints = user.getPoints() == null ? 0 : user.getPoints();
        Integer updatedPoints = currentPoints + points;
        if (updatedPoints < 0) {
            updatedPoints = 0; // 确保积分不为负数
        }
        
        // 更新用户积分
        user.setPoints(updatedPoints);
        boolean success = this.updateById(user);
        if (!success) {
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "积分更新失败");
        }
        
        // 记录积分变动日志
        log.info("用户积分变动：userId={}, points={}, reason={}, currentPoints={}", userId, points, reason, updatedPoints);
        
        // 检查是否需要更新用户等级
        checkAndUpdateUserLevel(user, updatedPoints);
        
        return updatedPoints;
    }

    /**
     * 根据积分检查并更新用户等级
     * @param user 用户对象
     * @param currentPoints 当前积分
     */
    private void checkAndUpdateUserLevel(User user, Integer currentPoints) {
        // 根据积分规则确定用户等级
        Integer newLevel = 1; // 默认等级
        
        // 积分阶梯，可以根据实际业务需求调整
        if (currentPoints >= 1000) {
            newLevel = 5;
        } else if (currentPoints >= 500) {
            newLevel = 4;
        } else if (currentPoints >= 200) {
            newLevel = 3;
        } else if (currentPoints >= 50) {
            newLevel = 2;
        }
        
        // 只有等级发生变化时才更新
        if (!newLevel.equals(user.getLevel())) {
            user.setLevel(newLevel);
            this.updateById(user);
            
            // 记录用户等级变化日志
            log.info("用户等级变化：userId={}, oldLevel={}, newLevel={}", user.getUser_id(), user.getLevel(), newLevel);
        }
    }

    /**
     * 获取用户戒烟节省的金额
     *
     * @param userId 用户ID
     * @return 节省金额
     */
    @Override
    public Double getUserSavedMoney(Long userId) {
        // 检查用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 获取用户的戒烟记录
        SmokingRecord smokingRecord = smokingRecordMapper.selectOne(
            new LambdaQueryWrapper<SmokingRecord>()
                .eq(SmokingRecord::getUser_id, userId)
                .orderByDesc(SmokingRecord::getCreated_at)
                .last("LIMIT 1")
        );
        
        if (smokingRecord == null) {
            return 0.0; // 没有戒烟记录，返回0
        }
        
        // 计算节省金额
        // 每包香烟的价格
        Double pricePerPack = smokingRecord.getPrice_per_pack().doubleValue();
        if (pricePerPack == null || pricePerPack <= 0) {
            pricePerPack = 20.0; // 默认每包20元
        }
        
        // 每天吸烟数量
        Integer cigarettePerDay = smokingRecord.getCigarette_per_day();
        if (cigarettePerDay == null || cigarettePerDay <= 0) {
            cigarettePerDay = 10; // 默认每天10支
        }
        
        // 计算每天花费
        Double costPerDay = (pricePerPack / 20.0) * cigarettePerDay;
        
        // 计算已戒烟天数
        Date startDate = smokingRecord.getStart_date();
        if (startDate == null) {
            return 0.0; // 没有开始日期，返回0
        }
        
        long daysBetween = ChronoUnit.DAYS.between(
            LocalDate.of(startDate.getYear() + 1900, startDate.getMonth() + 1, startDate.getDate()), 
            LocalDate.now()
        );
        if (daysBetween < 0) {
            daysBetween = 0; // 确保天数不为负
        }
        
        // 计算总节省金额
        Double totalSavedMoney = costPerDay * daysBetween;
        
        // 如果数据库中已有保存的节省金额，返回该值
        if (smokingRecord.getTotal_money_saved() != null && smokingRecord.getTotal_money_saved().doubleValue() > 0) {
            return smokingRecord.getTotal_money_saved().doubleValue();
        }
        
        return totalSavedMoney;
    }
}




