<script setup lang="ts">
import { RouterView, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// Initialisierung der Hooks
const authStore = useAuthStore()
const router = useRouter()

/**
 * Meldet den Benutzer ab und leitet ihn zur Login-Seite.
 */
function handleLogout() {
  authStore.logout()
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="min-h-screen bg-slate-50 font-sans antialiased text-slate-900">
    <nav class="p-4 bg-white shadow-sm mb-6 border-b border-slate-100">
      <div class="max-w-6xl mx-auto flex justify-between items-center">
        <router-link to="/" class="flex items-center gap-2 group">
          <span class="text-2xl">📚</span>
          <span class="font-bold text-xl text-blue-600 group-hover:text-blue-700 transition">
            Lehrhelfer
          </span>
        </router-link>

        <div class="flex items-center gap-4">
          <template v-if="authStore.isAuthenticated">
            <span class="hidden md:inline text-sm text-slate-500 italic">
              Lehrer-Modus aktiv
            </span>
            <button
                @click="handleLogout"
                class="bg-red-50 text-red-600 px-4 py-2 rounded-lg text-sm font-semibold hover:bg-red-100 transition-colors"
            >
              Abmelden
            </button>
          </template>

          <router-link
              v-else
              to="/login"
              class="text-sm font-semibold text-slate-600 hover:text-blue-600 transition"
          >
            Login
          </router-link>
        </div>
      </div>
    </nav>

    <main class="pb-20">
      <RouterView :key="$route.fullPath" />
    </main>

    <footer class="fixed bottom-0 w-full p-4 text-center text-xs text-slate-400 bg-white/80 backdrop-blur-sm border-t border-slate-100">
      Lehrhelfer &copy; 2025 | Powered by Java 25 & Vue 3
    </footer>
  </div>
</template>

<style>
/* Optionale globale Übergänge */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
