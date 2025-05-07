# 对话记录

## 对话1
### 用户对话
项目中用到了自定义异常，但是并没有定义相关的异常 ，根据项目中使用到的自定义异常 构建通用异常信息及SecurityUtils工具类

### 助手对话
根据项目需求，我已创建了以下异常处理相关类和工具类：

1. 异常体系：
   - `BaseException`：所有自定义异常的基类，包含错误码和错误消息
   - `BusinessException`：业务异常，处理业务逻辑中的错误情况
   - `CustomException`：自定义异常，用于特定场景的错误处理
   - `ErrorCode`：错误码枚举类，定义了各类错误码

2. 全局异常处理：
   - `GlobalExceptionHandler`：全局异常处理器，统一处理各类异常并返回标准格式的响应

3. 安全工具类：
   - `SecurityUtils`：提供用户认证相关的功能，包括获取当前用户ID、存储和移除Token等

4. 使用说明文档：
   - `src/main/java/com/muzi/quitsmoking/exception/README.md`：异常处理机制使用说明
   - `src/main/java/com/muzi/quitsmoking/utils/README.md`：SecurityUtils使用说明

这些类的实现遵循了以下原则：
- 统一的异常处理机制，确保API返回一致的错误格式
- 细分的错误码体系，便于前端进行不同错误的处理
- 安全实用的认证工具类，简化用户认证相关的操作
- 详细的使用说明文档，方便团队成员理解和使用

现在可以在项目中使用这些类来替代直接抛出的RuntimeException，提高代码的可维护性和错误处理的一致性。