import axios from 'axios';
import {useAuthStore} from "@/stores/auth.ts";

const api = axios.create({
  baseURL: 'http://localhost:8080/api' // Die URL deines Backends
});

// Interceptor: Hängt den Auth-Header an jede Anfrage an
api.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  if (authStore.token) {
    config.headers.Authorization = authStore.token;
  }
  return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && error.response.status === 401) {
        const authStore = useAuthStore();
        authStore.logout();
        window.location.href = '/login';
      }
      return Promise.reject(error);
    }
);

export default api;
