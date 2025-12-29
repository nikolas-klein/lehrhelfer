<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'

const router = useRouter()
const form = ref({ username: '', password: '', email: '' })
const error = ref('')

async function handleRegister() {
  try {
    await api.post('/public/register', form.value)
    alert('Account erstellt! Du kannst dich jetzt einloggen.')
    router.push('/login')
  } catch (err) {
    error.value = 'Registrierung fehlgeschlagen. Nutzername evtl. vergeben.'
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-20 p-8 bg-white rounded-xl shadow-lg">
    <h1 class="text-2xl font-bold mb-6 text-center">Registrierung</h1>
    <form @submit.prevent="handleRegister" class="space-y-4">
      <input v-model="form.username" type="text" placeholder="Nutzername" class="w-full p-3 border rounded" required />
      <input v-model="form.email" type="email" placeholder="E-Mail" class="w-full p-3 border rounded" required />
      <input v-model="form.password" type="password" placeholder="Passwort" class="w-full p-3 border rounded" required />
      <p v-if="error" class="text-red-500 text-sm">{{ error }}</p>
      <button type="submit" class="w-full bg-green-600 text-white py-3 rounded font-bold hover:bg-green-700">
        Account erstellen
      </button>
    </form>
    <p class="mt-4 text-center text-sm">
      Bereits ein Konto? <router-link to="/login" class="text-blue-500">Hier einloggen</router-link>
    </p>
  </div>
</template>
