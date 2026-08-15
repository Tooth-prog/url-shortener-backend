import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { AppRoutes } from './routes/AppRoutes';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
          <Navbar />
          <main style={{ flex: 1 }}>
            <AppRoutes />
          </main>
          <footer style={{ borderTop: '1px solid var(--border-color)', padding: '1.5rem', textAlign: 'center', color: 'var(--text-dim)', fontSize: '0.85rem' }}>
            Shortify URL Shortener Service &copy; {new Date().getFullYear()} — Powered by Spring Boot, Redis & React
          </footer>
        </div>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
