# 工具类使用文档

本文档详细介绍了项目中各个工具类的用途和使用方法，以便开发人员正确地使用这些工具类。

## 目录

- [安全工具类](#安全工具类)
- [Redis键工具类](#redis键工具类)
- [IP工具类](#ip工具类)
- [其他工具类](#其他工具类)

## 安全工具类

`SecurityUtils` 类提供了与用户认证相关的功能，主要用于获取和管理当前登录用户的信息。

### 主要功能

1. **获取当前用户ID**

```java
// 获取当前用户ID，如果未登录则抛出异常
Long userId = SecurityUtils.getCurrentUserId();

// 获取当前用户ID，如果未登录则返回null
Long userId = SecurityUtils.getCurrentUserIdOrNull();
```

2. **设置和清除当前用户ID**

```java
// 设置当前用户ID
SecurityUtils.setCurrentUserId(123L);

// 清除当前用户ID
SecurityUtils.clearCurrentUserId();
```

3. **获取Token和判断登录状态**

```java
// 获取请求中的Token
String token = SecurityUtils.getToken();

// 判断当前是否已登录
boolean isLoggedIn = SecurityUtils.isAuthenticated();
```

## Redis键工具类

`RedisKeyUtils` 类用于生成规范化的Redis键，确保项目中使用的Redis键遵循统一的格式，避免键名冲突。

### 主要功能

1. **生成验证码的键**

```java
// 生成验证码的键
String key = RedisKeyUtils.getVerifyCodeKey("13800138000");
// 结果：quit_smoking:user:verify_code:13800138000
```

2. **生成用户Token的键**

```java
// 生成用户Token的键
String key = RedisKeyUtils.getUserTokenKey(123L);
// 结果：quit_smoking:user:token:123
```

3. **生成Token黑名单的键**

```java
// 生成Token黑名单的键
String key = RedisKeyUtils.getTokenBlacklistKey("abc123");
// 结果：quit_smoking:user:token:blacklist:abc123
```

4. **生成其他类型的键**

```java
// 生成用户权限的键
String key = RedisKeyUtils.getUserPermissionKey(123L);

// 生成用户角色的键
String key = RedisKeyUtils.getUserRoleKey(123L);

// 生成文章缓存的键
String key = RedisKeyUtils.getArticleKey(456L);

// 生成文章阅读量的键
String key = RedisKeyUtils.getArticleViewKey(456L);

// 生成系统配置的键
String key = RedisKeyUtils.getSystemConfigKey("app_name");
```

## IP工具类

`IpUtils` 类提供了获取客户端真实IP地址的功能，特别是在使用代理服务器的情况下。

### 主要功能

1. **获取真实IP地址**

```java
// 获取请求的真实IP地址
String ip = IpUtils.getIpAddress(request);
```

该方法会按照以下顺序尝试获取IP地址：
- X-Forwarded-For
- X-Real-IP
- Proxy-Client-IP
- WL-Proxy-Client-IP
- HTTP_CLIENT_IP
- HTTP_X_FORWARDED_FOR
- request.getRemoteAddr()

如果IP地址是本地地址（127.0.0.1或0:0:0:0:0:0:0:1），则会尝试获取本机的网卡IP地址。

## 其他工具类

### 权限和角色常量类

项目中定义了两个常量类，用于存储权限和角色的标识：

1. **权限常量类 `PermissionConstants`**

```java
// 使用权限常量
String permission = PermissionConstants.USER_ADD;
```

2. **角色常量类 `RoleConstants`**

```java
// 使用角色常量
String role = RoleConstants.ADMIN;
```

## 最佳实践

1. **异常处理**

在使用 `SecurityUtils.getCurrentUserId()` 方法时，应该正确处理可能抛出的 `BusinessException` 异常。

```java
try {
    Long userId = SecurityUtils.getCurrentUserId();
    // 处理业务逻辑
} catch (BusinessException e) {
    // 处理未登录的情况
}
```

2. **Redis键的使用**

始终使用 `RedisKeyUtils` 生成Redis键，不要手动拼接键名。这样可以确保键名的一致性，并且在键名规则变更时只需修改工具类。

```java
// 正确的做法
String key = RedisKeyUtils.getUserTokenKey(userId);

// 错误的做法
String key = "quit_smoking:user:token:" + userId;
```

3. **IP地址的获取**

在获取IP地址时，优先使用 `IpUtils.getIpAddress(request)` 方法，不要直接使用 `request.getRemoteAddr()`。

```java
// 正确的做法
String ip = IpUtils.getIpAddress(request);

// 错误的做法
String ip = request.getRemoteAddr();
```

## 注意事项

1. 工具类的方法都是静态方法，不需要创建对象就可以直接使用。
2. 不要尝试实例化工具类，所有工具类的构造方法都被设为私有且会抛出异常。
3. 在使用 `SecurityUtils` 的方法时，必须确保在Web请求上下文中调用，否则会抛出异常。
4. 所有工具类都经过单元测试，可以放心使用。