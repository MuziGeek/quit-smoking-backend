package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户基本信息表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 用户ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer user_id;

    /**
     * 用户名，唯一标识符
     */
    private String username;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 昵称，显示用
     */
    private String nickname;

    /**
     * 头像URL地址
     */
    private String avatar;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birth_date;

    /**
     * 注册时间
     */
    private Date register_time;

    /**
     * 最后登录时间
     */
    private Date last_login_time;

    /**
     * 账号状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 积分，可用于兑换虚拟物品或特权
     */
    private Integer points;
}