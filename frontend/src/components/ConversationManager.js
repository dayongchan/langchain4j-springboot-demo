import React, { useState, useEffect } from 'react';
import { Button, List, Modal, Input, message } from 'antd';
import { PlusOutlined, DeleteOutlined, EditOutlined } from '@ant-design/icons';

const ConversationManager = ({ 
  conversations, 
  activeConversationId, 
  onConversationChange, 
  onConversationCreate, 
  onConversationDelete,
  onConversationRename
}) => {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [newConversationTitle, setNewConversationTitle] = useState('');
  const [editingConversationId, setEditingConversationId] = useState(null);
  const [editingTitle, setEditingTitle] = useState('');

  const showModal = () => {
    setIsModalVisible(true);
    setNewConversationTitle('');
  };

  const handleOk = () => {
    if (newConversationTitle.trim()) {
      onConversationCreate(newConversationTitle);
      setIsModalVisible(false);
      setNewConversationTitle('');
    } else {
      message.warning('请输入对话标题');
    }
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setNewConversationTitle('');
  };

  const startEditing = (conversation) => {
    setEditingConversationId(conversation.id);
    setEditingTitle(conversation.title);
  };

  const saveEditing = (conversationId) => {
    if (editingTitle.trim()) {
      onConversationRename(conversationId, editingTitle);
      setEditingConversationId(null);
      setEditingTitle('');
    } else {
      message.warning('请输入对话标题');
    }
  };

  const cancelEditing = () => {
    setEditingConversationId(null);
    setEditingTitle('');
  };

  return (
    <div>
      <div style={{ padding: '10px', borderBottom: '1px solid #f0f0f0' }}>
        <Button 
          type="primary" 
          icon={<PlusOutlined />} 
          onClick={showModal}
          block
        >
          新建对话
        </Button>
      </div>
      
      <List
        dataSource={conversations}
        renderItem={conversation => (
          <List.Item
            style={{
              padding: '10px 15px',
              cursor: 'pointer',
              backgroundColor: conversation.id === activeConversationId ? '#e6f7ff' : 'transparent',
              borderLeft: conversation.id === activeConversationId ? '3px solid #1890ff' : 'none'
            }}
            onClick={() => onConversationChange(conversation.id)}
          >
            {editingConversationId === conversation.id ? (
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input
                  value={editingTitle}
                  onChange={(e) => setEditingTitle(e.target.value)}
                  onPressEnter={() => saveEditing(conversation.id)}
                  onBlur={() => saveEditing(conversation.id)}
                  autoFocus
                />
              </div>
            ) : (
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <div style={{ flex: 1, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  {conversation.title}
                </div>
                <div>
                  <Button
                    type="text"
                    icon={<EditOutlined />}
                    onClick={(e) => {
                      e.stopPropagation();
                      startEditing(conversation);
                    }}
                    size="small"
                  />
                  <Button
                    type="text"
                    icon={<DeleteOutlined />}
                    onClick={(e) => {
                      e.stopPropagation();
                      onConversationDelete(conversation.id);
                    }}
                    size="small"
                    danger
                  />
                </div>
              </div>
            )}
          </List.Item>
        )}
      />
      
      <Modal
        title="新建对话"
        open={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="创建"
        cancelText="取消"
      >
        <Input
          placeholder="请输入对话标题"
          value={newConversationTitle}
          onChange={(e) => setNewConversationTitle(e.target.value)}
          onPressEnter={handleOk}
        />
      </Modal>
    </div>
  );
};

export default ConversationManager;