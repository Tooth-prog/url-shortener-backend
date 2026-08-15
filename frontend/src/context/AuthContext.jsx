import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

// Helper function to decode JWT payload safely
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch {
    return null;
  }
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('accessToken'));
  const [loading, setLoading] = useState(true);

  const syncUserFromToken = (accessToken) => {
    if (!accessToken) {
      setUser(null);
      return;
    }
    const decoded = parseJwt(accessToken);
    if (decoded && decoded.sub) {
      const userInfo = {
        email: decoded.sub,
        role: decoded.role || 'USER',
      };
      setUser(userInfo);
      localStorage.setItem('user', JSON.stringify(userInfo));
    } else {
      setUser(null);
    }
  };

  useEffect(() => {
    const currentToken = localStorage.getItem('accessToken');
    setToken(currentToken);
    syncUserFromToken(currentToken);
    setLoading(false);

    const handleAuthChange = () => {
      const newToken = localStorage.getItem('accessToken');
      setToken(newToken);
      syncUserFromToken(newToken);
    };

    window.addEventListener('authChange', handleAuthChange);
    return () => window.removeEventListener('authChange', handleAuthChange);
  }, []);

  const login = async (email, password) => {
    const res = await authService.login(email, password);
    const newAccessToken = localStorage.getItem('accessToken');
    setToken(newAccessToken);
    syncUserFromToken(newAccessToken);
    return res;
  };

  const register = async (username, email, password) => {
    return await authService.register(username, email, password);
  };

  const logout = () => {
    authService.logout();
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token,
        isAdmin: user?.role === 'ADMIN',
        loading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
