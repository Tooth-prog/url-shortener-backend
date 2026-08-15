import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { urlService } from '../services/urlService';
import { Link2, Sparkles, Copy, Check, ExternalLink, ArrowRight, Zap, Shield, BarChart3, Clock } from 'lucide-react';

export const LandingPage = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [url, setUrl] = useState('');
  const [customCode, setCustomCode] = useState('');
  const [expiryDays, setExpiryDays] = useState('7');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState(null);
  const [copied, setCopied] = useState(false);

  const handleShorten = async (e) => {
    e.preventDefault();
    setError('');
    setResult(null);

    if (!url.trim()) {
      setError('Please enter a valid URL');
      return;
    }

    if (!url.startsWith('http://') && !url.startsWith('https://')) {
      setError('URL must start with http:// or https://');
      return;
    }

    try {
      setLoading(true);
      const res = await urlService.shortenUrl(url, customCode, expiryDays);
      setResult(res);
    } catch (err) {
      console.error('Shorten error:', err);
      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else if (err.response?.data && typeof err.response.data === 'object' && !err.response.data.success) {
        if (err.response.data.data && typeof err.response.data.data === 'object') {
          const firstErr = Object.values(err.response.data.data)[0];
          setError(firstErr || 'Validation failed');
        } else {
          setError(err.response.data.message || 'Failed to shorten URL');
        }
      } else {
        setError('Unable to connect to the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleCopy = () => {
    if (result?.shortUrl) {
      navigator.clipboard.writeText(result.shortUrl);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  return (
    <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '3rem 1.5rem' }}>
      
      {/* Hero Header */}
      <div style={{ textAlign: 'center', maxWidth: '750px', margin: '0 auto 3.5rem auto' }} className="animate-fade-in">
        <div style={{ display: 'inline-flex', alignItems: 'center', gap: '0.5rem', padding: '0.35rem 1rem', borderRadius: '9999px', background: 'rgba(99, 102, 241, 0.1)', border: '1px solid rgba(99, 102, 241, 0.25)', color: '#a5b4fc', fontSize: '0.85rem', fontWeight: 600, marginBottom: '1.5rem' }}>
          <Sparkles size={16} color="#818cf8" /> High-Performance Enterprise URL Shortener
        </div>
        <h1 style={{ fontSize: '3.2rem', fontWeight: 800, lineHeight: 1.15, marginBottom: '1.25rem', letterSpacing: '-0.03em' }}>
          Shorten Links, <br />
          <span className="gradient-text">Amplify Your Reach.</span>
        </h1>
        <p style={{ fontSize: '1.15rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
          Fast, reliable, and analytics-driven URL shortening platform powered by Redis caching & Spring Boot microservices.
        </p>
      </div>

      {/* Main Shortener Widget */}
      <div className="glass-card" style={{ maxWidth: '800px', margin: '0 auto 4rem auto', padding: '2rem' }}>
        <form onSubmit={handleShorten} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          
          <div style={{ position: 'relative' }}>
            <div style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }}>
              <Link2 size={20} />
            </div>
            <input
              type="url"
              className="input-field"
              style={{ paddingLeft: '3rem', fontSize: '1.05rem', height: '54px' }}
              placeholder="Paste long URL here (e.g. https://example.com/very-long-path)..."
              value={url}
              onChange={(e) => setUrl(e.target.value)}
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: '0.4rem', fontWeight: 500 }}>
                Custom Alias (Optional)
              </label>
              <input
                type="text"
                className="input-field"
                placeholder="e.g. my-custom-link"
                value={customCode}
                onChange={(e) => setCustomCode(e.target.value)}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: '0.4rem', fontWeight: 500 }}>
                Expiration
              </label>
              <select
                className="input-field"
                value={expiryDays}
                onChange={(e) => setExpiryDays(e.target.value)}
                style={{ cursor: 'pointer' }}
              >
                <option value="1">1 Day</option>
                <option value="7">7 Days (Default)</option>
                <option value="30">30 Days</option>
                <option value="365">1 Year</option>
              </select>
            </div>
          </div>

          {error && (
            <div style={{ padding: '0.75rem 1rem', borderRadius: '10px', background: 'rgba(239, 68, 68, 0.1)', border: '1px solid rgba(239, 68, 68, 0.3)', color: '#f87171', fontSize: '0.9rem' }}>
              {error}
            </div>
          )}

          <button type="submit" className="btn btn-primary" style={{ height: '52px', fontSize: '1.05rem' }} disabled={loading}>
            {loading ? (
              <span>Shortening...</span>
            ) : (
              <>
                Shorten URL <ArrowRight size={18} />
              </>
            )}
          </button>
        </form>

        {/* Shortened Result Banner */}
        {result && (
          <div style={{ marginTop: '2rem', padding: '1.5rem', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.08)', border: '1px solid rgba(16, 185, 129, 0.3)' }} className="animate-fade-in">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1rem' }}>
              <span style={{ fontSize: '0.85rem', fontWeight: 600, color: '#34d399', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                ✓ URL Shortened Successfully
              </span>
              {result.expiryDate && (
                <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: '0.3rem' }}>
                  <Clock size={14} /> Expires: {new Date(result.expiryDate).toLocaleDateString()}
                </span>
              )}
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', flexWrap: 'wrap' }}>
              <input
                type="text"
                className="input-field"
                readOnly
                value={result.shortUrl}
                style={{ flex: 1, minWidth: '240px', background: '#0b0f19', fontWeight: 600, color: '#818cf8' }}
              />
              <button onClick={handleCopy} className="btn btn-secondary">
                {copied ? <Check size={18} color="#34d399" /> : <Copy size={18} />}
                {copied ? 'Copied!' : 'Copy'}
              </button>
              <a href={result.shortUrl} target="_blank" rel="noopener noreferrer" className="btn btn-primary">
                <ExternalLink size={18} /> Open
              </a>
            </div>
          </div>
        )}
      </div>

      {/* Feature Highlights Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.5rem' }}>
        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '10px', background: 'rgba(99, 102, 241, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
            <Zap size={22} color="#818cf8" />
          </div>
          <h3 style={{ fontSize: '1.15rem', marginBottom: '0.5rem' }}>Redis Caching</h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: 1.5 }}>
            Sub-millisecond redirect lookups powered by Redis in-memory storage, minimizing database access latency.
          </p>
        </div>

        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '10px', background: 'rgba(139, 92, 246, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
            <Shield size={22} color="#a78bfa" />
          </div>
          <h3 style={{ fontSize: '1.15rem', marginBottom: '0.5rem' }}>JWT & Refresh Tokens</h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: 1.5 }}>
            Stateless authentication with secure refresh token rotation & Spring Security authorization roles.
          </p>
        </div>

        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '10px', background: 'rgba(6, 182, 212, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
            <BarChart3 size={22} color="#22d3ee" />
          </div>
          <h3 style={{ fontSize: '1.15rem', marginBottom: '0.5rem' }}>Asynchronous Analytics</h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: 1.5 }}>
            Non-blocking click tracking and detailed analytics dashboards for link performance insights.
          </p>
        </div>
      </div>
    </div>
  );
};
