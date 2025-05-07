# 戒烟助手系统安全设计文档

## 1. 概述

本文档详细说明了戒烟助手系统的安全设计，包括认证机制、授权机制、数据安全、异常处理等方面。系统安全是保障用户数据和系统稳定运行的重要基础，本设计遵循安全性、可用性、易用性的原则。

## 2. 认证机制

### 2.1 JWT认证流程

系统采用JWT（JSON Web Token）进行用户认证，认证流程如下：

1. **用户登录**：
   - 用户提交手机号和验证码
   - 服务端验证成功后，生成JWT令牌
   - JWT令牌包含用户ID、角色等信息，并使用密钥签名
   - 返回JWT令牌给客户端

2. **请求认证**：
   - 客户端在请求头中添加`Authorization: Bearer {token}`
   - 服务端通过认证拦截器验证令牌有效性
   - 验证成功后，将用户ID存入请求上下文
   - 业务逻辑通过`SecurityUtils`获取当前用户ID

3. **令牌续期**：
   - JWT令牌默认有效期为24小时
   - 客户端可调用刷新令牌接口获取新令牌
   - 若令牌已过期，需要重新登录

### 2.2 认证相关类

- **TokenService**：提供令牌生成、验证、解析等功能
- **SecurityUtils**：提供当前用户上下文管理
- **AuthenticationInterceptor**：请求认证拦截器
- **WebMvcConfig**：配置拦截器和不需要认证的URL

### 2.3 认证配置

JWT相关配置项位于`application.yml`：

```yaml
jwt:
  # JWT加解密使用的密钥
  secret: "ThisIsASampleSecretKey@R2jb6d&Y6XZEDv5PZwDU9rS3Qm7KxJn8"
  # JWT的有效期，单位毫秒
  expiration: 86400000  # 24小时
  # JWT前缀
  token-prefix: "Bearer "
  # JWT的header名称
  header-name: "Authorization"
```

## 3. 授权机制

### 3.1 基于角色和权限的授权

系统采用基于角色和权限的授权机制（RBAC），主要包括：

1. **角色体系**：
   - 普通用户（USER）
   - VIP用户（VIP）
   - 管理员（ADMIN）
   - 运营人员（OPERATOR）
   - 内容编辑（EDITOR）

2. **权限体系**：
   - 用户相关权限（user:add、user:edit等）
   - 内容相关权限（content:add、content:edit等）
   - 社区相关权限（community:post:add等）
   - 系统相关权限（system:config等）

3. **授权方式**：
   - 注解式授权：使用`@RequirePermission`注解标记需要权限的方法
   - 代码式授权：通过`PermissionService`手动检查权限

### 3.2 授权相关类

- **RequirePermission**：权限要求注解
- **RequireLogin**：登录要求注解
- **SecurityAspect**：权限检查切面
- **PermissionService**：权限服务
- **RoleConstants**：角色常量
- **PermissionConstants**：权限常量

### 3.3 授权示例

```java
// 注解式授权：要求用户必须拥有user:edit权限
@RequirePermission("user:edit")
public void updateUserInfo(UserInfoUpdateDTO dto) {
    // 业务逻辑
}

// 代码式授权：手动检查权限
public void deleteUser(Long userId) {
    // 获取当前用户ID
    Long currentUserId = SecurityUtils.getCurrentUserId();
    
    // 检查是否有删除用户的权限
    if (!permissionService.hasPermission(currentUserId, "user:delete")) {
        throw new BusinessException(ErrorCode.FORBIDDEN, "没有删除用户的权限");
    }
    
    // 业务逻辑
    userMapper.deleteById(userId);
}
```

## 4. 数据安全

### 4.1 敏感数据加密

系统对以下敏感数据进行加密存储：

1. **用户密码**：使用BCrypt算法单向加密
2. **手机号**：部分脱敏处理（如：138****8888）
3. **身份证号**：AES双向加密
4. **银行卡号**：部分脱敏处理（如：6222****8888）

### 4.2 数据传输安全

1. **HTTPS传输**：所有API请求通过HTTPS传输
2. **参数加密**：重要参数可选择性进行加密传输
3. **防重放攻击**：时间戳+随机字符串+签名机制

### 4.3 防SQL注入

1. 所有SQL参数使用MyBatis的参数绑定
2. 使用MyBatis-Plus的安全查询方法
3. 避免使用字符串拼接SQL语句

## 5. 异常处理

### 5.1 异常体系

系统定义了统一的异常体系：

1. **BaseException**：基础异常类，所有自定义异常的父类
2. **BusinessException**：业务异常，表示业务流程中的异常情况
3. **CustomException**：自定义异常，用于特定场景的异常处理

### 5.2 全局异常处理

系统通过`GlobalExceptionHandler`统一处理异常，并以标准格式返回错误响应：

```json
{
  "code": 401,
  "message": "登录已过期，请重新登录",
  "data": null
}
```

### 5.3 错误码体系

系统定义了完整的错误码体系，分为以下几类：

1. **成功**：0
2. **客户端错误**：400-499
3. **用户错误**：1000-1999
4. **业务错误**：2000-2999
5. **系统错误**：5000-5999
6. **外部服务错误**：6000-6999
7. **文件上传错误**：7000-7999
8. **自定义错误**：9000-9999

## 6. 其他安全机制

### 6.1 防XSS攻击

1. 输入校验：对用户输入进行严格校验
2. 输出编码：页面输出时进行HTML编码
3. 使用内容安全策略（CSP）防止XSS攻击

### 6.2 防CSRF攻击

1. 使用Token验证：关键操作需要验证CSRF Token
2. 检查Referer/Origin：验证请求来源

### 6.3 限流与防刷机制

1. **IP限流**：同一IP短时间内请求次数限制
2. **接口限流**：关键接口（如登录、验证码）限制调用频次
3. **验证码**：敏感操作需要验证码验证

### 6.4 安全审计

1. **操作日志**：记录用户关键操作（登录、修改密码等）
2. **安全日志**：记录安全相关事件（认证失败、权限拒绝等）
3. **审计追踪**：支持用户操作追踪

## 7. 安全最佳实践

### 7.1 代码安全

1. 定期进行代码安全审计
2. 使用安全扫描工具检查代码漏洞
3. 遵循安全编码规范

### 7.2 运行环境安全

1. 使用最小权限原则配置服务器
2. 定期更新系统和组件版本
3. 配置文件敏感信息加密

### 7.3 开发流程安全

1. 安全需求纳入产品设计阶段
2. 开发人员接受安全培训
3. 上线前进行安全测试

## 8. 安全改进计划

1. **短期计划**（1-3个月）：
   - 完善日志审计系统
   - 增强密码策略
   - 加强接口限流措施

2. **中期计划**（3-6个月）：
   - 引入风控系统
   - 实现多因素认证
   - 优化权限管理

3. **长期计划**（6个月以上）：
   - 安全合规认证
   - 安全应急响应机制
   - 安全自动化运维 