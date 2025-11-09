class ChatService {
  constructor() {
    this.baseUrl = 'http://localhost:8088/api/chat';
  }

  /**
   * 发送消息并处理流式响应
   * @param {string} message - 用户输入的消息
   * @param {function} onChunkReceived - 接收到每个数据块时的回调函数
   * @param {function} onComplete - 完成时的回调函数
   * @param {function} onError - 出错时的回调函数
   */
  async sendStreamingMessage(message, onChunkReceived, onComplete, onError) {
    try {
      const response = await fetch(`${this.baseUrl}/streaming`, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain',
        },
        body: message,
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      if (!response.body) {
        throw new Error('ReadableStream not supported in this browser.');
      }

      const reader = response.body.getReader();
      const decoder = new TextDecoder('utf-8');
      
      try {
        while (true) {
          const { done, value } = await reader.read();
          
          if (done) {
            onComplete();
            break;
          }
          
          const chunk = decoder.decode(value, { stream: true });
          onChunkReceived(chunk);
        }
      } catch (error) {
        onError(error);
      } finally {
        reader.releaseLock();
      }
    } catch (error) {
      // 提供更详细的错误信息
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        onError(new Error('无法连接到服务器，请检查网络连接或确保后端服务正在运行'));
      } else {
        onError(error);
      }
    }
  }
}

export default new ChatService();