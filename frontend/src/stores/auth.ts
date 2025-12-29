import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import axios from "axios";

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('user_auth'));
  const isAuthenticated = computed(() => !!token.value);

  async function login(user: string, pass: string) {
    // 1. Sende Login-Daten ans Backend (neuer Endpoint: /api/public/login)
    const response = await axios.post('http://localhost:8080/api/public/login', { username: user, password: pass });

    // 2. Das Backend sendet nun einen String (den JWT) zurück
    const tokenFromServer = response.data.token;

    // 3. Speichere den Token als "Bearer ..."
    token.value = `Bearer ${tokenFromServer}`;
    localStorage.setItem('user_auth', token.value);
  }

  function logout() {
    token.value = null;
    localStorage.removeItem('user_auth');
  }

  return { token, isAuthenticated, login, logout };
});
