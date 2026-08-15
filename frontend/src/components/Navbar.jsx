import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Link2, LogOut, User, Shield, LayoutDashboard, LogIn, UserPlus } from 'lucide-react';

export const Navbar = () => {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header style={{ borderBottom: '1px solid var(--border-color)', backdropFilter: 'blur(12px)', sticky: 'top', top: 0, zIndex: 50, background: 'rgba(11, 15, 25, 0.8)' }}>
      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '1rem 1.5rem', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        
        {/* Brand Logo */}
        <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <div style={{ width: '38px', height: '38px', borderRadius: '10px', background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 4px 12px rgba(99, 102, 241, 0.3)' }}>
            <Link2 size={22} color="#ffffff" />
          </div>
          <div>
            <span className="brand-font" style={{ fontSize: '1.25rem', fontWeight: 700, color: '#ffffff', letterSpacing: '-0.02em' }}>
              Shortify<span style={{ color: '#818cf8' }}>.io</span>
            </span>
          </div>
        </Link>

        {/* Navigation / Actions */}
        <nav style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          {isAuthenticated ? (
            <>
              <Link to="/dashboard" className="btn btn-secondary btn-sm">
                <LayoutDashboard size={16} />
                Dashboard
              </Link>

              {isAdmin && (
                <Link to="/admin" className="btn btn-secondary btn-sm" style={{ borderColor: 'rgba(139, 92, 246, 0.4)', color: '#c084fc' }}>
                  <Shield size={16} />
                  Admin
                </Link>
              )}

              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.4rem 0.8rem', background: 'rgba(255,255,255,0.04)', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
                <User size={16} color="#9ca3af" />
                <span style={{ fontSize: '0.85rem', color: '#e5e7eb', fontWeight: 500 }}>{user?.email}</span>
                {isAdmin && <span className="badge badge-admin">ADMIN</span>}
              </div>

              <button onClick={handleLogout} className="btn btn-secondary btn-sm" style={{ color: '#f87171' }}>
                <LogOut size={16} />
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-secondary btn-sm">
                <LogIn size={16} />
                Sign In
              </Link>
              <Link to="/register" className="btn btn-primary btn-sm">
                <UserPlus size={16} />
                Get Started
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};
