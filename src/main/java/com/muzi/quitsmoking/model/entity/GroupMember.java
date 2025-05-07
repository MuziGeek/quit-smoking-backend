package com.muzi.quitsmoking.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 小组成员表
 * @TableName group_member
 */
@TableName(value ="group_member")
@Data
public class GroupMember {
    /**
     * 成员ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer member_id;

    /**
     * 小组ID
     */
    private Integer group_id;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 角色：member-普通成员，admin-管理员，creator-创建者
     */
    private String role;

    /**
     * 加入时间
     */
    private Date joined_at;

    /**
     * 状态：0-审核中，1-已加入，2-已退出
     */
    private Integer status;
}