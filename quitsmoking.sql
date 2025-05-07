-- QuitSmoking数据库表结构

-- 用户表
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增',
  `username` varchar(50) NOT NULL COMMENT '用户名，唯一标识符',
  `password` varchar(100) NOT NULL COMMENT '加密后的密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称，显示用',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `gender` tinyint DEFAULT '0' COMMENT '性别：0-未知，1-男，2-女',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '账号状态：0-禁用，1-正常',
  `level` int DEFAULT '1' COMMENT '用户等级',
  `points` int DEFAULT '0' COMMENT '积分，可用于兑换虚拟物品或特权',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基本信息表';

-- 积分记录表
CREATE TABLE `points_record` (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID，主键自增',
  `user_id` int NOT NULL COMMENT '用户ID，关联user表',
  `points` int NOT NULL COMMENT '积分变化值，正数表示增加，负数表示减少',
  `reason` varchar(255) NOT NULL COMMENT '积分变化原因',
  `current_points` int NOT NULL COMMENT '变化后的总积分',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分记录表';

-- 系统配置表
CREATE TABLE `system_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '配置ID，主键自增',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `description` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 用户戒烟记录表
CREATE TABLE `smoking_record` (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID，主键自增',
  `user_id` int NOT NULL COMMENT '用户ID，关联user表',
  `start_date` date NOT NULL COMMENT '戒烟开始日期',
  `cigarette_per_day` int NOT NULL DEFAULT '0' COMMENT '之前每天吸烟支数',
  `price_per_pack` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '每包香烟价格',
  `cigarettes_per_pack` int NOT NULL DEFAULT '20' COMMENT '每包香烟支数',
  `goal_days` int DEFAULT NULL COMMENT '戒烟目标天数',
  `current_goal` int DEFAULT NULL COMMENT '当前目标天数',
  `current_days` int NOT NULL DEFAULT '0' COMMENT '当前已戒烟天数',
  `longest_streak` int NOT NULL DEFAULT '0' COMMENT '最长连续戒烟天数',
  `total_money_saved` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计节省金额',
  `total_cigarettes_avoided` int NOT NULL DEFAULT '0' COMMENT '累计未吸香烟数量',
  `health_percent` int NOT NULL DEFAULT '0' COMMENT '健康恢复百分比',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '戒烟状态：0-失败，1-进行中，2-已完成',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户戒烟记录表';

-- 用户每日打卡记录表
CREATE TABLE `checkin` (
  `checkin_id` int NOT NULL AUTO_INCREMENT COMMENT '打卡ID，主键自增',
  `user_id` int NOT NULL COMMENT '用户ID，关联user表',
  `checkin_date` date NOT NULL COMMENT '打卡日期',
  `status` varchar(20) NOT NULL DEFAULT 'success' COMMENT '打卡状态：success-成功，tempted-有烟瘾但忍住了，failed-没忍住吸烟了',
  `craving_level` tinyint DEFAULT NULL COMMENT '烟瘾强度：1-5级',
  `smoked_count` int DEFAULT NULL COMMENT '吸烟数量，仅status为failed时有效',
  `mood` varchar(20) DEFAULT NULL COMMENT '心情：happy-开心，normal-平静，sad-难过，anxious-焦虑，angry-愤怒',
  `note` text COMMENT '打卡笔记，记录当天的感受',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '打卡创建时间',
  PRIMARY KEY (`checkin_id`),
  UNIQUE KEY `idx_user_date` (`user_id`,`checkin_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_checkin_date` (`checkin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户每日打卡记录表';

-- 成就系统表
CREATE TABLE `achievement` (
  `achievement_id` int NOT NULL AUTO_INCREMENT COMMENT '成就ID，主键自增',
  `title` varchar(100) NOT NULL COMMENT '成就标题',
  `description` varchar(255) DEFAULT NULL COMMENT '成就描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '成就图标',
  `type` varchar(20) NOT NULL COMMENT '成就类型：time-时间相关，health-健康相关，money-金钱相关，social-社交相关',
  `condition_type` varchar(20) NOT NULL COMMENT '条件类型：days-戒烟天数，saves-节省金额，posts-发帖数，etc',
  `condition_value` int NOT NULL COMMENT '达成条件值',
  `points` int NOT NULL DEFAULT '0' COMMENT '获得此成就奖励的积分',
  `level` tinyint NOT NULL DEFAULT '1' COMMENT '成就等级，数字越大越高级',
  `is_hidden` tinyint NOT NULL DEFAULT '0' COMMENT '是否隐藏成就：0-显示，1-隐藏直到达成',
  PRIMARY KEY (`achievement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就系统表，定义所有可获得的成就';

-- 用户成就表
CREATE TABLE `user_achievement` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `achievement_id` int NOT NULL COMMENT '成就ID',
  `achieved_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获得成就的时间',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_achievement` (`user_id`,`achievement_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_achievement_id` (`achievement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户获得的成就记录表';

-- 戒烟健康时间线表
CREATE TABLE `health_timeline` (
  `timeline_id` int NOT NULL AUTO_INCREMENT COMMENT '时间线ID，主键自增',
  `time_point` varchar(50) NOT NULL COMMENT '时间点，如"20分钟"、"8小时"、"1个月"等',
  `time_value` int NOT NULL COMMENT '时间数值，转换为小时计算，用于排序和判断',
  `title` varchar(100) NOT NULL COMMENT '健康效果标题',
  `description` text COMMENT '详细描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标名称',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `days_required` int DEFAULT NULL COMMENT '需要的天数',
  PRIMARY KEY (`timeline_id`),
  KEY `idx_time_value` (`time_value`),
  KEY `idx_order_num` (`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='戒烟健康时间线表，记录不同时间点的健康恢复情况';

-- 健康时间线收益表
CREATE TABLE `health_benefit` (
  `benefit_id` int NOT NULL AUTO_INCREMENT COMMENT '收益ID，主键自增',
  `timeline_id` int NOT NULL COMMENT '关联的时间线ID',
  `benefit_text` varchar(255) NOT NULL COMMENT '健康收益描述文本',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`benefit_id`),
  KEY `idx_timeline_id` (`timeline_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康时间线收益表，记录每个时间点的具体健康收益';

-- 戒烟资讯文章表
CREATE TABLE `article` (
  `article_id` int NOT NULL AUTO_INCREMENT COMMENT '文章ID，主键自增',
  `title` varchar(100) NOT NULL COMMENT '文章标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '文章摘要',
  `content` text NOT NULL COMMENT '文章内容，支持HTML格式',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图URL',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `source` varchar(100) DEFAULT NULL COMMENT '来源',
  `category` varchar(20) NOT NULL COMMENT '分类：knowledge-知识，experience-经验，research-研究，news-新闻',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '阅读次数',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `is_top` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶：0-否，1-是',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-草稿，1-已发布',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`article_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_is_top` (`is_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='戒烟资讯文章表';

-- 文章阅读记录表
CREATE TABLE `article_read` (
  `read_id` int NOT NULL AUTO_INCREMENT COMMENT '阅读记录ID，主键自增',
  `article_id` int NOT NULL COMMENT '文章ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `read_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
  `is_liked` tinyint NOT NULL DEFAULT '0' COMMENT '是否点赞：0-否，1-是',
  PRIMARY KEY (`read_id`),
  UNIQUE KEY `idx_article_user` (`article_id`,`user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章阅读记录表';

-- 戒烟工具表
CREATE TABLE `tool` (
  `tool_id` int NOT NULL AUTO_INCREMENT COMMENT '工具ID，主键自增',
  `name` varchar(50) NOT NULL COMMENT '工具名称',
  `description` varchar(255) DEFAULT NULL COMMENT '工具描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '工具图标',
  `type` varchar(20) NOT NULL COMMENT '工具类型：breathing-深呼吸练习，tips-戒烟建议，emergency-紧急求助，etc',
  `content` text COMMENT '工具内容，JSON格式存储',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  PRIMARY KEY (`tool_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='戒烟工具表，包含各种辅助戒烟的工具';

-- 用户工具使用记录表
CREATE TABLE `tool_usage` (
  `usage_id` int NOT NULL AUTO_INCREMENT COMMENT '使用记录ID，主键自增',
  `user_id` int NOT NULL COMMENT '用户ID',
  `tool_id` int NOT NULL COMMENT '工具ID',
  `usage_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '使用时间',
  `duration` int DEFAULT NULL COMMENT '使用时长（秒）',
  `feedback` tinyint DEFAULT NULL COMMENT '使用反馈：1-有用，0-无用',
  PRIMARY KEY (`usage_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_tool_id` (`tool_id`),
  KEY `idx_usage_time` (`usage_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户工具使用记录表';

-- 紧急求助记录表
CREATE TABLE `emergency_help` (
  `help_id` int NOT NULL AUTO_INCREMENT COMMENT '求助ID，主键自增',
  `user_id` int NOT NULL COMMENT '用户ID',
  `help_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '求助时间',
  `craving_level` tinyint NOT NULL COMMENT '烟瘾级别：1-5',
  `emotion` varchar(20) DEFAULT NULL COMMENT '情绪状态',
  `trigger` varchar(255) DEFAULT NULL COMMENT '诱发因素',
  `solution_used` varchar(255) DEFAULT NULL COMMENT '采用的解决方法',
  `result` tinyint DEFAULT NULL COMMENT '结果：0-失败，1-成功',
  `description` text COMMENT '描述',
  `severity` tinyint DEFAULT NULL COMMENT '紧急程度：1-轻微，2-中等，3-严重',
  `handle_type` tinyint DEFAULT NULL COMMENT '处理方式：1-等待，2-求助，3-其他',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-删除，1-正常',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`help_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_help_time` (`help_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='紧急求助记录表，记录用户烟瘾发作的求助情况';

-- 社区帖子表
CREATE TABLE `community_post` (
  `post_id` int NOT NULL AUTO_INCREMENT COMMENT '帖子ID，主键自增',
  `user_id` int NOT NULL COMMENT '发帖用户ID',
  `group_id` int DEFAULT NULL COMMENT '所属小组ID，NULL表示不属于任何小组',
  `title` varchar(100) DEFAULT NULL COMMENT '帖子标题，可为空',
  `content` text NOT NULL COMMENT '帖子内容',
  `post_type` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '帖子类型：normal-普通，question-问题，share-分享',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-删除，1-正常，2-置顶',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '评论数',
  `is_anonymous` tinyint NOT NULL DEFAULT '0' COMMENT '是否匿名：0-否，1-是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

-- 评论表
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID，主键自增',
  `post_id` int NOT NULL COMMENT '所属帖子ID',
  `user_id` int NOT NULL COMMENT '评论用户ID',
  `parent_id` int NOT NULL DEFAULT '0' COMMENT '父评论ID，回复某条评论时使用',
  `content` text NOT NULL COMMENT '评论内容',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `is_anonymous` tinyint NOT NULL DEFAULT '0' COMMENT '是否匿名：0-否，1-是',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-删除，1-正常',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`comment_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 点赞记录表
CREATE TABLE `like_record` (
  `like_id` int NOT NULL AUTO_INCREMENT COMMENT '点赞ID，主键自增',
  `user_id` int NOT NULL COMMENT '点赞用户ID',
  `target_id` int NOT NULL COMMENT '点赞目标ID（帖子ID或评论ID）',
  `type` varchar(10) NOT NULL COMMENT '点赞目标类型：post-帖子，comment-评论',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`like_id`),
  UNIQUE KEY `idx_user_target` (`user_id`,`target_id`,`type`),
  KEY `idx_target_id` (`target_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';

-- 社区小组表
CREATE TABLE `community_group` (
  `group_id` int NOT NULL AUTO_INCREMENT COMMENT '小组ID，主键自增',
  `name` varchar(50) NOT NULL COMMENT '小组名称',
  `description` text COMMENT '小组描述',
  `logo` varchar(255) DEFAULT NULL COMMENT '小组图标URL',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '小组封面图URL',
  `category_id` int DEFAULT NULL COMMENT '小组分类ID',
  `creator_id` int NOT NULL COMMENT '创建者用户ID',
  `member_count` int NOT NULL DEFAULT '0' COMMENT '成员数量',
  `post_count` int NOT NULL DEFAULT '0' COMMENT '帖子数量',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-封禁，1-正常',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`group_id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_status` (`status`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区小组表';

-- 小组成员表
CREATE TABLE `group_member` (
  `member_id` int NOT NULL AUTO_INCREMENT COMMENT '成员ID，主键自增',
  `group_id` int NOT NULL COMMENT '小组ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `role` varchar(20) NOT NULL DEFAULT 'member' COMMENT '角色：member-普通成员，admin-管理员，creator-创建者',
  `joined_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-审核中，1-已加入，2-已退出',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `idx_group_user` (`group_id`,`user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小组成员表';

-- 帖子图片表
CREATE TABLE `post_image` (
  `image_id` int NOT NULL AUTO_INCREMENT COMMENT '图片ID，主键自增',
  `post_id` int NOT NULL COMMENT '关联的帖子ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `width` int DEFAULT NULL COMMENT '图片宽度',
  `height` int DEFAULT NULL COMMENT '图片高度',
  `order_num` int DEFAULT '0' COMMENT '图片顺序',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`image_id`),
  KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子图片表';

-- 帖子标签表
CREATE TABLE `post_tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签ID，主键自增',
  `tag_name` varchar(30) NOT NULL COMMENT '标签名称',
  `description` varchar(255) DEFAULT NULL COMMENT '标签描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '标签图标',
  `use_count` int NOT NULL DEFAULT '0' COMMENT '使用该标签的次数',
  `is_hot` tinyint NOT NULL DEFAULT '0' COMMENT '是否热门：0-否，1-是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `idx_tag_name` (`tag_name`),
  KEY `idx_use_count` (`use_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子标签表';

-- 帖子与标签的关联表
CREATE TABLE `post_tag_relation` (
  `relation_id` int NOT NULL AUTO_INCREMENT COMMENT '关联ID，主键自增',
  `post_id` int NOT NULL COMMENT '帖子ID',
  `tag_id` int NOT NULL COMMENT '标签ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `idx_post_tag` (`post_id`,`tag_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子与标签的关联表'; 