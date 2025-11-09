class UserService {
  constructor() {
    this.baseUrl = 'http://localhost:8088/api/users';
     // 从localStorage恢复用户状态
    this.user = this.loadUserFromStorage();
  }

  /**
   * 从localStorage加载用户信息
   * @returns {Object|null} 用户信息或null
   */
  loadUserFromStorage() {
    try {
      const userData = localStorage.getItem('currentUser');
      return userData ? JSON.parse(userData) : null;
    } catch (error) {
      console.error('加载用户数据失败:', error);
      return null;
    }
  }

  /**
   * 保存用户信息到localStorage
   * @param {Object} user - 用户信息
   */
  saveUserToStorage(user) {
    try {
      localStorage.setItem('currentUser', JSON.stringify(user));
    } catch (error) {
      console.error('保存用户数据失败:', error);
    }
  }

  /**
   * 从localStorage清除用户信息
   */
  clearUserFromStorage() {
    try {
      localStorage.removeItem('currentUser');
    } catch (error) {
      console.error('清除用户数据失败:', error);
    }
  }

  /**
   * 用户注册
   * @param {string} username - 用户名
   * @param {string} password - 密码
   * @param {string} email - 邮箱
   * @returns {Promise} 注册结果
   */
  async register(username, password, email) {
    try {
      const response = await fetch(`${this.baseUrl}/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password, email }),
      });

      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '注册失败');
      }
      
      return data;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }

  /**
   * 用户登录
   * @param {string} username - 用户名
   * @param {string} password - 密码
   * @returns {Promise} 登录结果
   */
  async login(username, password) {
    try {
      const response = await fetch(`${this.baseUrl}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '登录失败');
      }
      
      this.user = data.user;
      this.saveUserToStorage(this.user); // 保存到localStorage
      return data;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }

  /**
   * 用户登出
   */
  logout() {
    this.user = null;
    this.clearUserFromStorage(); // 从localStorage清除
  }

  /**
   * 获取当前用户
   * @returns {Object|null} 当前用户信息
   */
  getCurrentUser() {
    return this.user;
  }

  /**
   * 检查用户是否已登录
   * @returns {boolean} 是否已登录
   */
  isLoggedIn() {
    return !!this.user;
  }

  /**
   * 获取用户对话列表
   * @param {number} userId - 用户ID
   * @returns {Promise} 对话列表
   */
  async getUserConversations(userId) {
    try {
      const response = await fetch(`${this.baseUrl}/${userId}/conversations`);
      
      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '获取对话列表失败');
      }
      
      return data.conversations;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }

  /**
   * 创建新对话
   * @param {number} userId - 用户ID
   * @param {string} title - 对话标题
   * @returns {Promise} 创建结果
   */
  async createConversation(userId, title) {
    try {
      const response = await fetch(`${this.baseUrl}/${userId}/conversations`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title }),
      });

      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '创建对话失败');
      }
      
      return data.conversation;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }

  /**
   * 删除对话
   * @param {number} conversationId - 对话ID
   * @returns {Promise} 删除结果
   */
  async deleteConversation(conversationId) {
    try {
      const response = await fetch(`${this.baseUrl}/conversations/${conversationId}`, {
        method: 'DELETE',
      });

      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '删除对话失败');
      }
      
      return data;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }

  /**
   * 获取对话消息
   * @param {number} conversationId - 对话ID
   * @returns {Promise} 消息列表
   */
  async getConversationMessages(conversationId) {
    try {
      const response = await fetch(`${this.baseUrl}/conversations/${conversationId}/messages`);
      
      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '获取消息失败');
      }
      
      return data.messages;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }

  /**
   * 保存消息
   * @param {number} conversationId - 对话ID
   * @param {number} userId - 用户ID
   * @param {string} content - 消息内容
   * @param {string} senderType - 发送者类型 (USER 或 AI)
   * @returns {Promise} 保存结果
   */
  async saveMessage(conversationId, userId, content, senderType) {
    try {
      const response = await fetch(`${this.baseUrl}/conversations/${conversationId}/messages`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId, content, senderType }),
      });

      // 检查响应是否为空
      const text = await response.text();
      if (!text) {
        throw new Error('服务器返回空响应');
      }

      // 尝试解析JSON
      let data;
      try {
        data = JSON.parse(text);
      } catch (parseError) {
        throw new Error(`服务器响应格式错误: ${text}`);
      }

      if (!data.success) {
        throw new Error(data.message || '保存消息失败');
      }
      
      return data.message;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }
}

export default new UserService();