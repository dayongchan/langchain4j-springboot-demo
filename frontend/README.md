# AI Assistant Frontend

这是AI助手项目的前端应用，基于React构建，使用Ant Design组件库，提供用户友好的界面来与后端AI服务进行交互。

## 功能特性

- 用户注册和登录界面
- 对话管理（创建、删除、重命名对话）
- 实时流式消息显示
- 消息历史记录查看
- 响应式UI设计
- 登录状态持久化

## 技术栈

- React 18
- Ant Design 5
- JavaScript (ES6+)
- react-scripts 5.0.1

## 项目结构

```
src/
├── App.js                 # 应用根组件
├── App.css                # 应用全局样式
├── index.js               # 应用入口文件
├── components/            # UI组件
│   ├── ChatComponent.js   # 聊天界面组件
│   ├── ConversationManager.js  # 对话管理组件
│   └── LoginForm.js       # 登录注册表单组件
├── pages/                 # 页面组件
│   └── ChatPage.js        # 聊天页面
├── services/              # 服务层
│   ├── chatService.js     # 聊天相关服务
│   └── userService.js     # 用户相关服务
└── utils/                 # 工具函数
```

## 组件介绍

### App.js
应用的根组件，负责用户状态管理和页面路由：
- 检查用户登录状态
- 管理登录/聊天页面切换
- 处理用户登出逻辑

### LoginForm.js
登录注册表单组件：
- 提供登录和注册标签页
- 用户名、密码、邮箱输入验证
- 与后端用户认证API交互

### ChatPage.js
聊天主页面组件：
- 管理对话列表和消息状态
- 处理对话创建、删除、切换
- 负责消息发送和接收逻辑
- 与用户服务和聊天服务交互

### ChatComponent.js
聊天界面组件：
- 显示消息列表
- 提供消息输入区域
- 支持流式消息显示效果
- 提供清空对话功能

### ConversationManager.js
对话管理组件：
- 显示用户对话列表
- 支持新建、删除、重命名对话
- 突出显示当前激活的对话

## 服务层

### userService.js
用户相关服务：
- 用户注册、登录、登出
- 对话管理（创建、删除、获取列表）
- 消息管理（保存、获取）
- 用户状态持久化（localStorage）

### chatService.js
聊天相关服务：
- 发送消息到后端并处理流式响应
- 提供错误处理机制
- 支持实时消息接收

## 环境要求

- Node.js 14+
- npm 或 yarn

## 运行项目

### 安装依赖

```bash
cd frontend
npm install
```

### 启动开发服务器

```bash
npm start
```

应用将在 `http://localhost:3000` 启动。

### 构建生产版本

```bash
npm run build
```

构建后的文件将位于 `build/` 目录中。

## 配置说明

### 后端API地址

前端默认连接到 `http://localhost:8088` 的后端服务：

```javascript
// userService.js
this.baseUrl = 'http://localhost:8088/api/users';

// chatService.js
this.baseUrl = 'http://localhost:8088/api/chat';
```

如需修改后端地址，请相应调整以上配置。

## 开发说明

### 添加新功能

1. 在 `components/` 目录中创建新的UI组件
2. 在 `services/` 目录中添加相应的服务逻辑
3. 在页面组件中集成新功能

### 样式定制

- 全局样式定义在 `App.css` 中
- 组件内部样式通过 `style` 属性或组件内联CSS定义
- 可引入Ant Design的样式变量进行主题定制

## 部署

### 部署到静态服务器

1. 构建生产版本：
   ```bash
   npm run build
   ```

2. 将 `build/` 目录中的所有文件部署到静态服务器

### 注意事项

1. 确保后端服务已启动并可访问
2. 如部署到不同域名，需要配置CORS支持
3. 生产环境中建议使用HTTPS

## 许可证

本项目基于MIT许可证开源，详情请见[LICENSE](../LICENSE)文件。