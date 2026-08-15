import React, { useEffect, useState } from 'react';
import { urlService } from '../services/urlService';
import { 
  Link2, Copy, Check, ExternalLink, ArrowRight, BarChart2, 
  Clock, Eye, RefreshCw, Layers, MousePointer, ShieldAlert, X
} from 'lucide-react';

export const DashboardPage = () => {
  const [urls, setUrls] = useState([]);
  const [loadingUrls, setLoadingUrls] = useState(true);

  // Shortener form state
  const [url, setUrl] = useState('');
  const [customCode, setCustomCode] = useState('');
  const [expiryDays, setExpiryDays] = useState('7');
  const [shortenLoading, setShortenLoading] = useState(false);
  const [formError, setFormError] = useState('');
  const [createdResult, setCreatedResult] = useState(null);
  const [copiedId, setCopiedId] = useState(null);

  // Inspector modal state
  const [selectedStats, setSelectedStats] = useState(null);
  const [statsLoading, setStatsLoading] = useState(false);

  const fetchUserUrls = async () => {
    try {
      setLoadingUrls(true);
      const data = await urlService.getUserUrls();
      setUrls(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error('Error fetching URLs:', err);
    } finally {
      setLoadingUrls(false);
    }
  };

  useEffect(() => {
    fetchUserUrls();
  }, []);

  const handleShorten = async (e) => {
    e.preventDefault();
    setFormError('');
    setCreatedResult(null);

    if (!url.trim()) {
      setFormError('Please enter a valid URL');
      return;
    }

    if (!url.startsWith('http://') && !url.startsWith('https://')) {
      setFormError('URL must start with http:// or https://');
      return;
    }

    try {
      setShortenLoading(true);
      const res = await urlService.shortenUrl(url, customCode, expiryDays);
      setCreatedResult(res);
      setUrl('');
      setCustomCode('');
      fetchUserUrls(); // Refresh table
    } catch (err) {
      console.error('Shorten error:', err);
      if (err.response?.data?.message) {
        setFormError(err.response.data.message);
      } else if (err.response?.data && typeof err.response.data === 'object' && !err.response.data.success) {
        if (err.response.data.data && typeof err.response.data.data === 'object') {
          const firstErr = Object.values(err.response.data.data)[0];
          setFormError(firstErr || 'Validation failed');
        } else {
          setFormError(err.response.data.message || 'Failed to shorten URL');
        }
      } else {
        setFormError('Unable to connect to the server. Please try again.');
      }
    } finally {
      setShortenLoading(false);
    }
  };

  const copyToClipboard = (text, id) => {
    navigator.clipboard.writeText(text);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  const handleInspectStats = async (shortCode) => {
    try {
      setStatsLoading(true);
      const stats = await urlService.getAnalytics(shortCode);
      setSelectedStats({ ...stats, shortCode });
    } catch (err) {
      console.error('Failed to load stats:', err);
    } finally {
      setStatsLoading(false);
    }
  };

  // Calculate quick summary metrics
  const totalUrls = urls.length;
  const totalClicks = urls.reduce((acc, curr) => acc + (curr.clickCount || 0), 0);
  const activeUrls = urls.filter((u) => !u.expiryDate || new Date(u.expiryDate) > new Date()).length;
  const expiredUrls = totalUrls - activeUrls;

  return (
    <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2.5rem 1.5rem' }}>
      
      {/* Dashboard Heading */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '2rem' }}>
        <div>
          <h1 style={{ fontSize: '2.25rem', fontWeight: 800 }}>Dashboard</h1>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.95rem', marginTop: '0.2rem' }}>
            Manage your shortened links, monitor click performance, and create new aliases.
          </p>
        </div>
        <button onClick={fetchUserUrls} className="btn btn-secondary btn-sm" title="Refresh URL table">
          <RefreshCw size={16} /> Refresh Data
        </button>
      </div>

      {/* Analytics Summary Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', marginBottom: '2.5rem' }}>
        
        <div className="glass-card" style={{ padding: '1.25rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: 'var(--text-muted)', marginBottom: '0.5rem' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: 600 }}>Total Short URLs</span>
            <Layers size={18} color="#818cf8" />
          </div>
          <div style={{ fontSize: '2rem', fontWeight: 700 }}>{totalUrls}</div>
        </div>

        <div className="glass-card" style={{ padding: '1.25rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: 'var(--text-muted)', marginBottom: '0.5rem' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: 600 }}>Total Click Count</span>
            <MousePointer size={18} color="#c084fc" />
          </div>
          <div style={{ fontSize: '2rem', fontWeight: 700, color: '#c084fc' }}>{totalClicks}</div>
        </div>

        <div className="glass-card" style={{ padding: '1.25rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: 'var(--text-muted)', marginBottom: '0.5rem' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: 600 }}>Active URLs</span>
            <Clock size={18} color="#34d399" />
          </div>
          <div style={{ fontSize: '2rem', fontWeight: 700, color: '#34d399' }}>{activeUrls}</div>
        </div>

        <div className="glass-card" style={{ padding: '1.25rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: 'var(--text-muted)', marginBottom: '0.5rem' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: 600 }}>Expired Links</span>
            <ShieldAlert size={18} color="#f87171" />
          </div>
          <div style={{ fontSize: '2rem', fontWeight: 700, color: '#f87171' }}>{expiredUrls}</div>
        </div>

      </div>

      {/* URL Creation Form */}
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '3rem' }}>
        <h2 style={{ fontSize: '1.25rem', marginBottom: '1.25rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Link2 size={20} color="#818cf8" /> Shorten a New URL
        </h2>

        <form onSubmit={handleShorten} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
            <div style={{ flex: 1, minWidth: '300px' }}>
              <input
                type="url"
                className="input-field"
                placeholder="Enter your long URL (https://...)"
                value={url}
                onChange={(e) => setUrl(e.target.value)}
                required
              />
            </div>
            <div style={{ width: '180px' }}>
              <input
                type="text"
                className="input-field"
                placeholder="Custom Alias (Opt)"
                value={customCode}
                onChange={(e) => setCustomCode(e.target.value)}
              />
            </div>
            <div style={{ width: '140px' }}>
              <select
                className="input-field"
                value={expiryDays}
                onChange={(e) => setExpiryDays(e.target.value)}
                style={{ cursor: 'pointer' }}
              >
                <option value="1">1 Day</option>
                <option value="7">7 Days</option>
                <option value="30">30 Days</option>
                <option value="365">1 Year</option>
              </select>
            </div>
            <button type="submit" className="btn btn-primary" disabled={shortenLoading}>
              {shortenLoading ? 'Shortening...' : 'Shorten URL'}
            </button>
          </div>

          {formError && (
            <div style={{ padding: '0.75rem 1rem', borderRadius: '10px', background: 'rgba(239, 68, 68, 0.1)', border: '1px solid rgba(239, 68, 68, 0.3)', color: '#f87171', fontSize: '0.9rem' }}>
              {formError}
            </div>
          )}
        </form>

        {/* Newly created URL banner */}
        {createdResult && (
          <div style={{ marginTop: '1.5rem', padding: '1.25rem', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.08)', border: '1px solid rgba(16, 185, 129, 0.3)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
            <div>
              <span style={{ fontSize: '0.8rem', color: '#34d399', fontWeight: 600, textTransform: 'uppercase' }}>✓ Created:</span>
              <span style={{ marginLeft: '0.75rem', fontFamily: 'monospace', fontWeight: 600, color: '#ffffff' }}>{createdResult.shortUrl}</span>
            </div>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button onClick={() => copyToClipboard(createdResult.shortUrl, 'new-created')} className="btn btn-secondary btn-sm">
                {copiedId === 'new-created' ? <Check size={16} color="#34d399" /> : <Copy size={16} />}
                {copiedId === 'new-created' ? 'Copied' : 'Copy'}
              </button>
              <a href={createdResult.shortUrl} target="_blank" rel="noopener noreferrer" className="btn btn-primary btn-sm">
                <ExternalLink size={16} /> Open
              </a>
            </div>
          </div>
        )}
      </div>

      {/* URL Management Table */}
      <div className="glass-card" style={{ padding: '1.75rem' }}>
        <h2 style={{ fontSize: '1.25rem', marginBottom: '1.25rem' }}>Your Shortened URLs</h2>

        {loadingUrls ? (
          <div style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>
            Loading your URLs...
          </div>
        ) : urls.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '3rem 1rem', color: 'var(--text-muted)' }}>
            <Link2 size={40} style={{ opacity: 0.3, marginBottom: '1rem' }} />
            <p style={{ fontSize: '1.05rem', fontWeight: 500 }}>No URLs shortened yet.</p>
            <p style={{ fontSize: '0.85rem', marginTop: '0.25rem' }}>Use the form above to generate your first trackable short link.</p>
          </div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.9rem' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                  <th style={{ padding: '0.75rem 1rem' }}>Original URL</th>
                  <th style={{ padding: '0.75rem 1rem' }}>Short Code</th>
                  <th style={{ padding: '0.75rem 1rem', textAlign: 'center' }}>Clicks</th>
                  <th style={{ padding: '0.75rem 1rem' }}>Created</th>
                  <th style={{ padding: '0.75rem 1rem' }}>Status</th>
                  <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {urls.map((item) => {
                  const isExpired = item.expiryDate && new Date(item.expiryDate) < new Date();
                  return (
                    <tr key={item.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)', transition: 'background 0.15s' }}>
                      
                      {/* Original URL with truncate */}
                      <td style={{ padding: '1rem', maxWidth: '300px' }}>
                        <div style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', color: '#e5e7eb' }} title={item.originalUrl}>
                          {item.originalUrl}
                        </div>
                      </td>

                      {/* Short Code */}
                      <td style={{ padding: '1rem' }}>
                        <span style={{ background: 'rgba(99, 102, 241, 0.12)', color: '#818cf8', padding: '0.2rem 0.5rem', borderRadius: '6px', fontFamily: 'monospace', fontWeight: 600 }}>
                          {item.shortCode}
                        </span>
                      </td>

                      {/* Click count */}
                      <td style={{ padding: '1rem', textAlign: 'center', fontWeight: 700, color: '#c084fc' }}>
                        {item.clickCount ?? 0}
                      </td>

                      {/* Created date */}
                      <td style={{ padding: '1rem', color: 'var(--text-muted)' }}>
                        {item.createdAt ? new Date(item.createdAt).toLocaleDateString() : 'N/A'}
                      </td>

                      {/* Status pill */}
                      <td style={{ padding: '1rem' }}>
                        <span className={`badge ${isExpired ? 'badge-expired' : 'badge-active'}`}>
                          {isExpired ? 'Expired' : 'Active'}
                        </span>
                      </td>

                      {/* Actions */}
                      <td style={{ padding: '1rem', textAlign: 'right' }}>
                        <div style={{ display: 'inline-flex', gap: '0.4rem' }}>
                          <button
                            onClick={() => copyToClipboard(item.shortUrl, item.id)}
                            className="btn btn-secondary btn-sm"
                            title="Copy Short URL"
                          >
                            {copiedId === item.id ? <Check size={14} color="#34d399" /> : <Copy size={14} />}
                          </button>
                          
                          <a
                            href={item.shortUrl}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-secondary btn-sm"
                            title="Open Short Link"
                          >
                            <ExternalLink size={14} />
                          </a>

                          <button
                            onClick={() => handleInspectStats(item.shortCode)}
                            className="btn btn-secondary btn-sm"
                            title="View Analytics"
                          >
                            <Eye size={14} />
                          </button>
                        </div>
                      </td>

                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Analytics Modal Inspector */}
      {selectedStats && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.7)', backdropFilter: 'blur(4px)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1.5rem' }}>
          <div className="glass-card animate-fade-in" style={{ width: '100%', maxWidth: '500px', padding: '2rem', position: 'relative', background: '#111827' }}>
            <button
              onClick={() => setSelectedStats(null)}
              style={{ position: 'absolute', right: '1.25rem', top: '1.25rem', background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer' }}
            >
              <X size={20} />
            </button>

            <h3 style={{ fontSize: '1.3rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <BarChart2 size={22} color="#818cf8" /> Link Analytics
            </h3>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', fontSize: '0.95rem' }}>
              <div>
                <span style={{ color: 'var(--text-muted)', display: 'block', fontSize: '0.8rem' }}>Short Code</span>
                <span style={{ fontFamily: 'monospace', fontWeight: 600, color: '#818cf8', fontSize: '1.1rem' }}>{selectedStats.shortCode}</span>
              </div>

              <div>
                <span style={{ color: 'var(--text-muted)', display: 'block', fontSize: '0.8rem' }}>Original Destination</span>
                <p style={{ wordBreak: 'break-all', color: '#e5e7eb' }}>{selectedStats.originalUrl}</p>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', background: 'rgba(255,255,255,0.03)', padding: '1rem', borderRadius: '10px' }}>
                <div>
                  <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>Total Clicks</span>
                  <div style={{ fontSize: '1.5rem', fontWeight: 700, color: '#c084fc' }}>{selectedStats.clickCount}</div>
                </div>
                <div>
                  <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>Expiration Date</span>
                  <div style={{ fontSize: '0.9rem', color: '#e5e7eb', marginTop: '0.2rem' }}>
                    {selectedStats.expiryDate ? new Date(selectedStats.expiryDate).toLocaleDateString() : 'Never'}
                  </div>
                </div>
              </div>
            </div>

            <button onClick={() => setSelectedStats(null)} className="btn btn-primary" style={{ width: '100%', marginTop: '1.5rem' }}>
              Close Inspector
            </button>
          </div>
        </div>
      )}

    </div>
  );
};
