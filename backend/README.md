# AI Assistant Backend

这是AI助手项目的后端服务，基于Spring Boot构建，提供RESTful API接口，支持用户管理、对话历史记录和AI聊天功能。

## 功能特性

- 用户注册和登录认证
- 对话管理（创建、删除、查询对话列表）
- 消息管理（保存和查询对话消息）
- 集成Langchain4j实现AI聊天功能
- 支持流式响应的聊天接口
- 数据持久化（MySQL）

## 技术栈

- Java 17
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL 8.0
- Langchain4j 1.8.0-beta15
- Maven

## 项目结构

```
src/main/java/com/angelai/assistant/demo/langchain4j
├── Langchain4jDemoApplication.java  # 应用启动类
├── config/                          # 配置类
│   ├── SecurityConfig.java          # Spring Security配置
│   └── WebConfig.java               # Web配置（CORS等）
├── controller/                      # 控制器层
│   ├── ChatController.java          # 聊天相关接口
│   └── UserController.java          # 用户相关接口
├── dto/                             # 数据传输对象
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── UserDto.java
├── entity/                          # 实体类
│   ├── User.java
│   ├── Conversation.java
│   └── Message.java
├── repository/                      # 数据访问层
│   ├── UserRepository.java
│   ├── ConversationRepository.java
│   └── MessageRepository.java
└── service/                         # 业务逻辑层
    ├── UserService.java
    ├── ConversationService.java
    ├── StreamingChatAssistant.java
    ├── OpenAiAssistant.java
    └── ...
```

## 数据模型

### User（用户）
- id: 用户ID
- username: 用户名
- password: 密码（加密存储）
- email: 邮箱
- createdAt: 创建时间
- updatedAt: 更新时间

### Conversation（对话）
- id: 对话ID
- userId: 关联的用户ID
- title: 对话标题
- createdAt: 创建时间
- updatedAt: 更新时间

### Message（消息）
- id: 消息ID
- conversationId: 关联的对话ID
- userId: 关联的用户ID
- content: 消息内容
- senderType: 发送者类型（USER/AI）
- createdAt: 创建时间

## API接口

### 用户相关接口

- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/{userId}/conversations` - 获取用户对话列表
- `POST /api/users/{userId}/conversations` - 创建新对话
- `DELETE /api/users/conversations/{conversationId}` - 删除对话
- `GET /api/users/conversations/{conversationId}/messages` - 获取对话消息
- `POST /api/users/conversations/{conversationId}/messages` - 保存消息

### 聊天相关接口

- `GET /api/chat/message` - 简单聊天接口
- `POST /api/chat/streaming` - 流式聊天接口

## 配置说明

### 数据库配置

在 `src/main/resources/application.yaml` 中配置数据库连接：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/langchain4j?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false
    username: your_username
    password: your_password
```

### AI模型配置

配置AI模型API密钥和相关信息：

```yaml
langchain4j:
  open-ai:
    streaming-chat-model:
      api-key: your_api_key
      model-name: deepseek-chat
      base-url: https://api.deepseek.com/v1
```

## 运行项目

### 环境要求

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### 构建和运行

1. 克隆项目到本地
2. 创建MySQL数据库：
   ```sql
   CREATE DATABASE langchain4j;
   ```
3. 修改 `application.yaml` 中的数据库配置
4. 修改 `application.yaml` 中的AI模型配置
5. 使用Maven运行项目：
   ```bash
   cd backend
   mvn spring-boot:run
   ```

### 打包部署

```bash
cd backend
mvn package
java -jar target/langchain4j-springboot-demo-0.0.1-SNAPSHOT.jar
```

## 安全配置

项目使用Spring Security进行安全控制：
- 用户注册和登录接口无需认证即可访问
- 其他接口暂时开放访问（可根据需要调整）
- 配置了CORS支持，允许跨域请求

## 开发说明

### 添加新的API接口

1. 在 `controller` 包中创建新的控制器类
2. 在 `service` 包中实现业务逻辑
3. 如需要，创建相应的实体类和Repository接口

### 数据库变更

项目使用Hibernate的自动DDL功能（`ddl-auto: update`），会根据实体类自动创建和更新表结构。

## 许可证

本项目基于MIT许可证开源，详情请见[LICENSE](../LICENSE)文件。