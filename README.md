# AI Assistant Demo with Langchain4j

这是一个基于Spring boot和React构建的AI助手演示项目，展示了如何使用Langchain4j库与大语言模型集成，创建一个具有用户认证、对话历史管理和流式响应功能的完整聊天应用。

## 目录结构

```
.
├── backend/          # Spring Boot后端服务
├── frontend/         # React前端应用
└── README.md         # 项目说明文档
```

## 功能特性

- 用户注册和登录系统
- 实时流式聊天响应
- 对话历史记录保存与查看
- 多轮对话上下文管理
- 响应式UI设计

## 技术栈

### 后端技术栈

- Java 17
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- MySQL 8.0
- Langchain4j 1.8.0-beta15
- OpenAI/DeepSeek API集成

### 前端技术栈

- React 18
- Ant Design 5
- JavaScript (ES6+)

## 系统要求

- Java 17+
- Node.js 14+
- MySQL 8.0+
- Maven 3.6+

## 快速开始
### 克隆项目
```bash
git clone https://github.com/dayongchan/langchain4j-springboot-demo.git
cd langchain4j-springboot-demo
```

### 后端设置

1. 配置数据库：
   ```bash
   # 创建MySQL数据库
   mysql -u root -p
   CREATE DATABASE langchain4j;
   ```

2. 更新数据库配置：
   复制示例的配置文件：
   ```bash
   cd backend/src/main/resources/
   cp application.yaml.example application.yaml
   ```
   编辑 `backend/src/main/resources/application.yaml` 文件中的数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/langchain4j?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false
       username: your_username
       password: your_password
   ```

3. 配置AI模型API：
   在 `application.yaml` 中更新你的API密钥和模型信息：
   ```yaml
   langchain4j:
     open-ai:
       streaming-chat-model:
         api-key: your_api_key
         model-name: deepseek-chat
         base-url: https://api.deepseek.com/v1
   ```

4. 构建并运行后端：
   ```bash
   cd backend
   mvn spring-boot:run
   ```

   后端服务将在 `http://localhost:8088` 启动。

### 前端设置

1. 安装依赖：
   ```bash
   cd frontend
   npm install
   ```

2. 启动开发服务器：
   ```bash
   npm start
   ```

   前端应用将在 `http://localhost:3000` 启动。

## API接口

### 聊天接口

- `POST /api/chat/streaming` - 发送消息并接收流式响应
- `POST /api/chat/message` - 发送消息并一次性返回

### 用户接口

- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/{userId}/conversations` - 获取用户对话列表
- `POST /api/users/{userId}/conversations` - 创建新对话
- `DELETE /api/users/conversations/{conversationId}` - 删除对话
- `GET /api/users/conversations/{conversationId}/messages` - 获取对话消息
- `POST /api/users/conversations/{conversationId}/messages` - 保存消息

## 开发说明

### 后端开发

- 使用Spring Boot构建RESTful API
- 集成Langchain4j实现AI对话功能
- 使用Spring Security处理用户认证
- 使用JPA和MySQL存储用户数据和对话历史

### 前端开发

- 使用React构建用户界面
- 使用Ant Design组件库
- 实现用户认证状态管理
- 支持流式消息显示

## 部署

### 后端部署

```bash
cd backend
mvn package
java -jar target/langchain4j-springboot-demo-0.0.1-SNAPSHOT.jar
```

### 前端部署

```bash
cd frontend
npm run build
# 将build目录中的文件部署到Web服务器
```

## 注意事项

1. 本项目仅供演示和学习用途
2. 生产环境中应加强安全措施，如：
    - 使用HTTPS
    - 添加输入验证
    - 实施速率限制
    - 使用更强的密码策略
3. 建议使用环境变量管理敏感信息，如API密钥和数据库密码

## 许可证

本项目基于MIT许可证开源，详情请见[LICENSE](LICENSE)文件。