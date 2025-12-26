import userService from './userService';

class ChatService {
  constructor() {
    this.baseUrl = 'http://localhost:8089/api/chat';
  }

  /**
   * 发送消息并处理流式响应
   * @param {string} message - 用户输入的消息
   * @param {function} onChunkReceived - 接收到每个数据块时的回调函数
   * @param {function} onComplete - 完成时的回调函数
   * @param {function} onError - 出错时的回调函数
   */
  async sendStreamingMessage(message, conversationId, onChunkReceived, onComplete, onError) {
    let reader = null;
    try {
      const response = await fetch(`${this.baseUrl}/streaming?conversationId=${conversationId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain',
          'Authorization': `Bearer ${userService.getToken()}`
        },
        body: message,
      });

      console.log('Stream response status:', response.status);
      console.log('Stream response headers:', response.headers);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      if (!response.body) {
        throw new Error('ReadableStream not supported in this browser.');
      }

      reader = response.body.getReader();
      const decoder = new TextDecoder('utf-8');
      
      let done = false;
      while (!done) {
        const { done: readerDone, value } = await reader.read();
        done = readerDone;
        
        if (done) {
          console.log('Stream completed successfully');
          onComplete();
          break;
        }
        
        if (value) {
          const chunk = decoder.decode(value, { stream: true });
          console.log('Received chunk:', chunk);
          // 立即处理每个chunk
          onChunkReceived(chunk);
        }
      }
      
      reader.releaseLock();
    } catch (error) {
      console.error('Stream error:', error);
      // 提供更详细的错误信息
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        onError(new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行'));
      } else if (error.message.includes('ERR_INCOMPLETE_CHUNKED_ENCODING')) {
        onError(new Error('服务器响应中断，请稍后重试'));
      } else {
        onError(error);
      }
    } finally {
      if (reader && !reader.closed) {
        try {
          reader.cancel();
        } catch (cancelError) {
          console.error('Error canceling reader:', cancelError);
        }
      }
    }
  }

  /**
   * 发送消息并获取完整响应
   * @param {string} message - 用户输入的消息
   * @returns {Promise<string>} AI回复
   */
  async sendMessage(message) {
    try {
      const response = await fetch(`${this.baseUrl}/message?msg=${encodeURIComponent(message)}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${userService.getToken()}`
        }
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
        throw new Error(data.message || '发送消息失败');
      }
      
      return data.data;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }
  
  /**
   * 发送消息并获取完整响应（带搜索功能）
   * @param {string} message - 用户输入的消息
   * @returns {Promise<string>} AI回复
   */
  async sendSearchMessage(message) {
    try {
      const response = await fetch(`${this.baseUrl}/search-message?msg=${encodeURIComponent(message)}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${userService.getToken()}`
        }
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
        throw new Error(data.message || '发送消息失败');
      }
      
      return data.data;
    } catch (error) {
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        throw new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行');
      }
      throw new Error(error.message || '网络错误');
    }
  }
  
  /**
   * 发送消息并处理流式响应（带搜索功能）
   * @param {string} message - 用户输入的消息
   * @param {function} onChunkReceived - 接收到每个数据块时的回调函数
   * @param {function} onComplete - 完成时的回调函数
   * @param {function} onError - 出错时的回调函数
   */
  async sendStreamingSearchMessage(message, conversationId, onChunkReceived, onComplete, onError) {
    let reader = null;
    try {
      const response = await fetch(`${this.baseUrl}/streaming-search?conversationId=${conversationId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain',
          'Authorization': `Bearer ${userService.getToken()}`
        },
        body: message,
      });

      console.log('Stream search response status:', response.status);
      console.log('Stream search response headers:', response.headers);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      if (!response.body) {
        throw new Error('ReadableStream not supported in this browser.');
      }

      reader = response.body.getReader();
      const decoder = new TextDecoder('utf-8');
      
      let done = false;
      while (!done) {
        const { done: readerDone, value } = await reader.read();
        done = readerDone;
        
        if (done) {
          console.log('Stream search completed successfully');
          onComplete();
          break;
        }
        
        if (value) {
          const chunk = decoder.decode(value, { stream: true });
          console.log('Received search chunk:', chunk);
          // 立即处理每个chunk
          onChunkReceived(chunk);
        }
      }
      
      reader.releaseLock();
    } catch (error) {
      console.error('Stream search error:', error);
      // 提供更详细的错误信息
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        onError(new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行'));
      } else if (error.message.includes('ERR_INCOMPLETE_CHUNKED_ENCODING')) {
        onError(new Error('服务器响应中断，请稍后重试'));
      } else {
        onError(error);
      }
    } finally {
      if (reader && !reader.closed) {
        try {
          reader.cancel();
        } catch (cancelError) {
          console.error('Error canceling search reader:', cancelError);
        }
      }
    }
  }
}

export default new ChatService();