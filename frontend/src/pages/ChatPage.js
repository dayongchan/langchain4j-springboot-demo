import React, { useState, useEffect } from 'react';
import { Layout, message, Button, Popconfirm } from 'antd';
import { LogoutOutlined } from '@ant-design/icons';
import ChatComponent from '../components/ChatComponent';
import ConversationManager from '../components/ConversationManager';
import userService from '../services/userService';
import chatService from '../services/chatService';

const { Header, Sider, Content } = Layout;

const ChatPage = ({ currentUser, onLogout }) => {
  // 对话管理状态
  const [conversations, setConversations] = useState([]);
  const [activeConversationId, setActiveConversationId] = useState(null);
  
  // 消息状态
  const [messages, setMessages] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  // 初始化默认对话
  useEffect(() => {
    if (currentUser) {
      loadUserConversations();
    }
  }, [currentUser]);

  // 加载用户对话列表
  const loadUserConversations = async () => {
    try {
      const userConversations = await userService.getUserConversations(currentUser.id);
      setConversations(userConversations);
      
      // 如果有对话，激活第一个对话
      if (userConversations.length > 0) {
        setActiveConversationId(userConversations[0].id);
        // 加载第一个对话的消息
        loadConversationMessages(userConversations[0].id);
      } else {
        // 如果没有对话，创建一个默认对话
        handleCreateConversation('默认对话');
      }
    } catch (error) {
      message.error('加载对话列表失败: ' + error.message);
    }
  };

  // 加载对话消息
  const loadConversationMessages = async (conversationId) => {
    try {
      const conversationMessages = await userService.getConversationMessages(conversationId);
      // 转换消息格式以匹配前端组件
      const formattedMessages = conversationMessages.map(msg => ({
        id: msg.id,
        text: msg.content,
        sender: msg.senderType === 'USER' ? 'user' : 'ai',
        timestamp: msg.createdAt
      }));
      
      setMessages(prev => ({
        ...prev,
        [conversationId]: formattedMessages
      }));
    } catch (error) {
      message.error('加载对话消息失败: ' + error.message);
    }
  };

  // 创建新对话
  const handleCreateConversation = async (title) => {
    try {
      const newConversation = await userService.createConversation(currentUser.id, title);
      setConversations(prev => [...prev, newConversation]);
      setMessages(prev => ({ ...prev, [newConversation.id]: [] }));
      setActiveConversationId(newConversation.id);
      message.success('对话创建成功');
    } catch (error) {
      message.error('创建对话失败: ' + error.message);
    }
  };

  // 切换对话
  const handleChangeConversation = (conversationId) => {
    setActiveConversationId(conversationId);
    // 如果该对话的消息尚未加载，则加载消息
    if (!messages[conversationId] || messages[conversationId].length === 0) {
      loadConversationMessages(conversationId);
    }
  };

  // 删除对话
  const handleDeleteConversation = async (conversationId) => {
    if (conversations.length <= 1) {
      message.warning('至少需要保留一个对话');
      return;
    }
    
    try {
      await userService.deleteConversation(conversationId);
      
      const updatedConversations = conversations.filter(conv => conv.id !== conversationId);
      const updatedMessages = { ...messages };
      delete updatedMessages[conversationId];
      
      setConversations(updatedConversations);
      setMessages(updatedMessages);
      
      // 如果删除的是当前激活的对话，则切换到第一个对话
      if (conversationId === activeConversationId) {
        if (updatedConversations.length > 0) {
          setActiveConversationId(updatedConversations[0].id);
        } else {
          setActiveConversationId(null);
        }
      }
      
      message.success('对话删除成功');
    } catch (error) {
      message.error('删除对话失败: ' + error.message);
    }
  };

  // 重命名对话
  const handleRenameConversation = async (conversationId, newTitle) => {
    try {
      // 注意：这里需要在后端添加更新对话标题的API
      setConversations(prev => 
        prev.map(conv => 
          conv.id === conversationId 
            ? { ...conv, title: newTitle } 
            : conv
        )
      );
      message.success('对话重命名成功');
    } catch (error) {
      message.error('重命名对话失败: ' + error.message);
    }
  };

  // 发送消息
  const handleSendMessage = async (messageText) => {
    if (!activeConversationId) return;
    
    // 添加用户消息到数据库
    try {
      await userService.saveMessage(activeConversationId, currentUser.id, messageText, 'USER');
    } catch (error) {
      console.error('保存用户消息失败:', error);
      message.error('保存消息失败: ' + error.message);
    }
    
    // 添加用户消息到前端状态
    const userMessage = { 
      id: Date.now(), 
      text: messageText, 
      sender: 'user',
      timestamp: new Date().toISOString()
    };

    setMessages(prev => ({
      ...prev,
      [activeConversationId]: [...(prev[activeConversationId] || []), userMessage]
    }));
    
    setIsLoading(true);
    
    // 创建AI回复消息占位符
    const aiMessageId = Date.now() + 1;
    const aiMessage = { 
      id: aiMessageId, 
      text: '', 
      sender: 'ai',
      isStreaming: true,
      timestamp: new Date().toISOString()
    };
    
    setMessages(prev => ({
      ...prev,
      [activeConversationId]: [...(prev[activeConversationId] || []), aiMessage]
    }));
    
    let accumulatedResponse = '';
    
    try {
      // 调用聊天服务发送消息
      await chatService.sendStreamingMessage(
        messageText,
        (chunk) => {
          // 处理每个数据块
          accumulatedResponse += chunk;
          setMessages(prev => ({
            ...prev,
            [activeConversationId]: prev[activeConversationId].map(msg => 
              msg.id === aiMessageId 
                ? { ...msg, text: accumulatedResponse } 
                : msg
            )
          }));
        },
        async () => {
          // 完成时的处理 - 保存AI回复到数据库
          try {
            await userService.saveMessage(activeConversationId, currentUser.id, accumulatedResponse, 'AI');
          } catch (error) {
            console.error('保存AI消息失败:', error);
            message.error('保存AI回复失败: ' + error.message);
          }
          
          setMessages(prev => ({
            ...prev,
            [activeConversationId]: prev[activeConversationId].map(msg => 
              msg.id === aiMessageId 
                ? { ...msg, isStreaming: false } 
                : msg
            )
          }));
          setIsLoading(false);
        },
        async (error) => {
          // 错误处理
          console.error('Error sending message:', error);
          
          // 保存错误信息到数据库
          try {
            await userService.saveMessage(activeConversationId, currentUser.id, '抱歉，发送消息时出错: ' + error.message, 'AI');
          } catch (saveError) {
            console.error('保存错误消息失败:', saveError);
          }
          
          setMessages(prev => ({
            ...prev,
            [activeConversationId]: prev[activeConversationId].map(msg => 
              msg.id === aiMessageId 
                ? { ...msg, text: '抱歉，发送消息时出错: ' + error.message, isStreaming: false } 
                : msg
            )
          }));
          setIsLoading(false);
          message.error('消息发送失败: ' + error.message);
        }
      );
    } catch (error) {
      console.error('Error sending message:', error);
      
      // 保存错误信息到数据库
      try {
        await userService.saveMessage(activeConversationId, currentUser.id, '抱歉，发送消息时出错: ' + error.message, 'AI');
      } catch (saveError) {
        console.error('保存错误消息失败:', saveError);
      }
      
      setMessages(prev => ({
        ...prev,
        [activeConversationId]: prev[activeConversationId].map(msg => 
          msg.id === aiMessageId 
            ? { ...msg, text: '抱歉，发送消息时出错: ' + error.message, isStreaming: false } 
            : msg
        )
      }));
      setIsLoading(false);
      message.error('消息发送失败: ' + error.message);
    }
  };

  // 清空当前对话
  const handleClearConversation = () => {
    if (!activeConversationId) return;
    
    setMessages(prev => ({
      ...prev,
      [activeConversationId]: []
    }));
    message.success('对话已清空');
  };

  return (
    <Layout style={{ height: '100vh' }}>
      <Header style={{ 
        color: 'white', 
        textAlign: 'center', 
        padding: '0 20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        backgroundColor: '#1890ff'
      }}>
        <h2 style={{ margin: 0 }}>AI 助手对话</h2>
        <div>
          <span style={{ marginRight: 16 }}>欢迎, {currentUser.username}</span>
          <Popconfirm
            title="确定要退出登录吗？"
            onConfirm={onLogout}
            okText="确定"
            cancelText="取消"
          >
            <Button type="text" icon={<LogoutOutlined />} style={{ color: 'white' }}>
              退出
            </Button>
          </Popconfirm>
        </div>
      </Header>
      
      <Layout>
        {/* 侧边栏 - 对话管理 */}
        <Sider width={250} theme="light">
          <ConversationManager
            conversations={conversations}
            activeConversationId={activeConversationId}
            onConversationChange={handleChangeConversation}
            onConversationCreate={handleCreateConversation}
            onConversationDelete={handleDeleteConversation}
            onConversationRename={handleRenameConversation}
          />
        </Sider>
        
        {/* 主内容区 - 聊天界面 */}
        <Content style={{ padding: 0, overflow: 'hidden' }}>
          <ChatComponent
            messages={messages[activeConversationId] || []}
            onSendMessage={handleSendMessage}
            onClearConversation={handleClearConversation}
            isLoading={isLoading}
          />
        </Content>
      </Layout>
    </Layout>
  );
};

export default ChatPage;