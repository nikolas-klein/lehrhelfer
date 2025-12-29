<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  if (!username.value || !password.value) {
    error.value = 'Bitte fülle alle Felder aus.'
    return
  }

  loading.value = true
  error.value = ''

  try {
    // Ruft die Login-Funktion im Store auf
    await authStore.login(username.value, password.value)

    // Nach erfolgreichem Login zum Dashboard weiterleiten
    router.push({ name: 'dashboard' })
  } catch (err) {
    error.value = 'Anmeldung fehlgeschlagen. Bitte prüfe deine Daten.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-[80vh] flex items-center justify-center px-4">
    <div class="max-w-md w-full bg-white p-8 rounded-2xl shadow-xl border border-slate-100">

      <div class="text-center mb-8">
        <h1 class="text-3xl font-extrabold text-slate-900">Lehrer-Login</h1>
        <p class="text-slate-500 mt-2">Verwalte deine Unterrichtsstunden</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2">Benutzername</label>
          <input
              v-model="username"
              type="text"
              placeholder="z.B. lehrer"
              class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition"
          />
        </div>

        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2">Passwort</label>
          <input
              v-model="password"
              type="password"
              placeholder="••••••••"
              class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition"
          />
        </div>

        <p v-if="error" class="text-red-500 text-sm font-medium text-center">
          {{ error }}
        </p>

        <button
            type="submit"
            :disabled="loading"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg shadow-lg shadow-blue-200 transition-all flex items-center justify-center"
        >
          <span v-if="loading" class="animate-spin mr-2">◌</span>
          {{ loading ? 'Anmeldung...' : 'Einloggen' }}
        </button>
      </form>

      <div class="mt-8 pt-6 border-t border-slate-100 text-center">
        <router-link to="/lesson/mathe-funktionen-01" class="text-sm text-blue-500 hover:underline">
          Zurück zur Schüler-Ansicht (Beispiel)
        </router-link>
      </div>
    </div>
  </div>
</template>
