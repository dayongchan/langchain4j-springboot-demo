import React, { useState, useRef, useEffect } from 'react';
import { Input, Button, List, Card, Typography, Space, Alert, Dropdown, Menu } from 'antd';
import { SendOutlined, DeleteOutlined, ClearOutlined } from '@ant-design/icons';
import chatService from '../services/chatService';

const { TextArea } = Input;
const { Text } = Typography;

const ChatComponent = ({ 
  messages = [], 
  onSendMessage, 
  onClearConversation,
  isLoading = false 
}) => {
  const [inputValue, setInputValue] = useState('');
  const [error, setError] = useState(null);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSend = async () => {
    if (!inputValue.trim() || isLoading) return;

    // 清除之前的错误
    setError(null);
    
    try {
      await onSendMessage(inputValue);
      setInputValue('');
    } catch (error) {
      console.error('Error sending message:', error);
      setError(error.message);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const menu = (
    <Menu>
      <Menu.Item 
        key="clear" 
        icon={<ClearOutlined />} 
        onClick={onClearConversation}
      >
        清空对话
      </Menu.Item>
    </Menu>
  );

  return (
    <Card style={{ height: '100%', display: 'flex', flexDirection: 'column', borderRadius: 0 }}>
      {error && (
        <Alert 
          message={error} 
          type="error" 
          showIcon 
          closable 
          onClose={() => setError(null)}
          style={{ marginBottom: '10px' }}
        />
      )}
      
      {/* 聊天头部 - 添加操作菜单 */}
      <div style={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center', 
        padding: '10px 15px',
        borderBottom: '1px solid #f0f0f0'
      }}>
        <div style={{ fontWeight: 'bold' }}>对话</div>
        <Dropdown overlay={menu} trigger={['click']}>
          <Button type="text" icon={<DeleteOutlined />} />
        </Dropdown>
      </div>
      
      {/* 消息区域 */}
      <div style={{ 
        flex: 1, 
        overflowY: 'auto', 
        marginBottom: '16px',
        padding: '20px',
        backgroundColor: '#f5f5f5'
      }}>
        {messages.length === 0 ? (
          <div style={{ 
            textAlign: 'center', 
            color: '#999', 
            marginTop: '50px' 
          }}>
            开始一个新的对话
          </div>
        ) : (
          <List
            dataSource={messages}
            renderItem={(message) => (
              <List.Item style={{ 
                border: 'none', 
                padding: '8px 0',
                alignItems: message.sender === 'user' ? 'flex-end' : 'flex-start',
                flexDirection: message.sender === 'user' ? 'row-reverse' : 'row'
              }}>
                {/* 消息气泡 */}
                <div style={{ 
                  maxWidth: '80%',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: message.sender === 'user' ? 'flex-end' : 'flex-start'
                }}>
                  <div style={{ 
                    padding: '10px 15px',
                    borderRadius: '15px',
                    backgroundColor: message.sender === 'user' ? '#1890ff' : '#ffffff',
                    color: message.sender === 'user' ? '#fff' : '#000',
                    whiteSpace: 'pre-wrap',
                    wordBreak: 'break-word',
                    boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
                    textAlign: 'left'
                  }}>
                    {message.text}
                    {message.isStreaming && <Text type="secondary">▍</Text>}
                  </div>
                  <div style={{ 
                    fontSize: '12px', 
                    color: '#999', 
                    marginTop: '4px',
                    textAlign: message.sender === 'user' ? 'right' : 'left'
                  }}>
                    {message.sender === 'user' ? '你' : 'AI助手'}
                  </div>
                </div>
              </List.Item>
            )}
          />
        )}
        <div ref={messagesEndRef} />
      </div>
      
      {/* 输入区域 */}
      <div style={{ 
        display: 'flex', 
        gap: '8px',
        alignItems: 'flex-end',
        width: '100%'
      }}>
        <TextArea
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onPressEnter={handleKeyPress}
          placeholder="输入消息..."
          autoSize={{ minRows: 2, maxRows: 4 }}
          disabled={isLoading}
          style={{ 
            borderRadius: '20px',
            flex: 1
          }}
        />
        <Button 
          type="primary" 
          icon={<SendOutlined />} 
          onClick={handleSend}
          loading={isLoading}
          disabled={!inputValue.trim()}
          style={{ 
            borderRadius: '20px',
            height: 'auto',
            padding: '4px 16px'
          }}
        >
          发送
        </Button>
      </div>
    </Card>
  );
};

export default ChatComponent;