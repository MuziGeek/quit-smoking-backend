# 异常处理机制

本项目中的异常处理机制主要包含以下几个部分：

## 异常体系结构

- `BaseException`：所有自定义异常的基类，包含错误码和错误消息
- `BusinessException`：业务异常，表示业务处理过程中出现的错误，例如用户不存在、积分不足等
- `CustomException`：自定义异常，用于特定场景的错误处理

## 错误码定义

在`ErrorCode`枚举类中定义了所有的错误码：

- 0xxx：成功状态码
- 1xxx：用户相关错误码
- 2xxx：业务处理错误码
- 5xxx：系统内部错误码
- 6xxx：外部服务错误码
- 7xxx：文件上传错误码
- 9xxx：自定义错误码

## 全局异常处理

`GlobalExceptionHandler`类负责捕获和处理所有未被处理的异常，将其转换为统一的结果格式返回给客户端。

## 异常使用示例

### 抛出业务异常

```java
// 用户不存在
if (user == null) {
    throw new BusinessException(ErrorCode.USER_NOT_FOUND);
}

// 操作失败
if (!success) {
    throw new BusinessException(ErrorCode.OPERATION_FAILED, "积分更新失败");
}

// 参数错误
if (userId == null) {
    throw new BusinessException(ErrorCode.PARAM_ERROR, "用户ID不能为空");
}
```

### 抛出自定义异常

```java
// 自定义错误
if (achievementId == null) {
    throw new CustomException("成就ID不能为空");
}

// 自定义错误
if (userId == null) {
    throw new CustomException("用户未登录");
}
```

## 最佳实践

1. 优先使用`BusinessException`处理业务逻辑中的异常情况
2. 当需要自定义错误码和消息时，可以使用`CustomException`
3. 在Service层抛出异常，在Controller层统一处理
4. 不要使用`RuntimeException`直接抛出异常，应该包装为自定义异常
5. 异常信息应当清晰明了，便于定位问题
6. 记得在日志中记录异常信息

## 如何扩展

如果需要添加新的错误码：

1. 在`ErrorCode`枚举类中添加新的错误码定义
2. 在对应的场景中使用新的错误码抛出异常

如果需要添加新的异常类型：

1. 创建新的异常类，继承`BaseException`
2. 实现相应的构造方法
3. 在`GlobalExceptionHandler`中添加对应的异常处理方法 