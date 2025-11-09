import React, { useState, useEffect } from 'react';
import './App.css';
import LoginForm from './components/LoginForm';
import ChatPage from './pages/ChatPage';
import userService from './services/userService';

function App() {
  const [currentUser, setCurrentUser] = useState(null);

  // 页面加载时检查用户登录状态
  useEffect(() => {
    const storedUser = userService.getCurrentUser();
    if (storedUser) {
      setCurrentUser(storedUser);
    }
  }, []);

  const handleLoginSuccess = (user) => {
    setCurrentUser(user);
  };

  const handleLogout = () => {
    userService.logout();
    setCurrentUser(null);
  };

  return (
    <div className="App">
      {currentUser ? (
        <ChatPage currentUser={currentUser} onLogout={handleLogout} />
      ) : (
        <LoginForm onLoginSuccess={handleLoginSuccess} />
      )}
    </div>
  );
}

export default App;