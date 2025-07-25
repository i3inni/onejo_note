import { defineStore } from 'pinia';
import api from '@/services/apiService';
import router from '@/router';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null as string | null,
    user: null as { nickname: string; email: string } | null,
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    async register(credentials: any) {
      await api.post('/auth/register', credentials);
      // Optionally send verification email right after registration
      await this.sendVerificationCode(credentials.email);
    },
    async sendVerificationCode(email: string) {
      await api.post('/auth/send-verification-code', { email });
    },
    async verifyEmail(verificationData: any) {
      await api.post('/auth/verify-email', verificationData);
    },
    async login(credentials: any) {
      const response = await api.post('/auth/login', credentials);
      const token = response.data.jwt;
      this.token = token;
      localStorage.setItem('token', token);
      await this.fetchUser();
      router.push({ name: 'home' });
    },
    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('token');
      router.push({ name: 'login' });
    },
    async fetchUser() {
      if (this.token) {
        // This endpoint needs to be created in the backend.
        // It should return the user details based on the JWT.
        try {
          const response = await api.get('/users/me'); 
          this.user = response.data;
        } catch (error) {
          console.error('Failed to fetch user:', error);
          this.logout();
        }
      }
    },
    async initializeAuth() {
        if (this.token) {
            await this.fetchUser();
        }
    }
  },
});
