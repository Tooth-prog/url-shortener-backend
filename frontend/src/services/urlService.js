import api from './api';

export const urlService = {
  // Shorten a URL (supports anonymous and authenticated users)
  async shortenUrl(url, customCode = null, expiryDays = null) {
    const payload = { url };
    if (customCode && customCode.trim() !== '') {
      payload.customCode = customCode.trim();
    }
    if (expiryDays !== null && expiryDays !== undefined && expiryDays !== '') {
      payload.expiryDays = parseInt(expiryDays, 10);
    }

    const response = await api.post('/api/v1/shorten', payload);
    return response.data;
  },

  // Get current authenticated user's URLs
  async getUserUrls() {
    const response = await api.get('/api/user/urls');
    return response.data;
  },

  // Get analytics for a specific short code
  async getAnalytics(shortCode) {
    const response = await api.get(`/api/v1/analytics/${shortCode}`);
    return response.data;
  },

  // Get all users (Admin only)
  async getAdminUsers() {
    const response = await api.get('/api/admin/users');
    return response.data;
  },
};
