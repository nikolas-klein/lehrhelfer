<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../api';
import type { Lesson, Card, CardType } from '../types';

const router = useRouter();

// --- STATE ---
const myLessons = ref<Lesson[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

// Formular-State für eine neue Lektion
const newLesson = ref<Lesson>({
  title: '',
  slug: '',
  cards: []
});

// --- LOGIK ---

// 1. Eigene Stunden vom Backend laden
const fetchLessons = async () => {
  loading.value = true;
  try {
    const response = await api.get('/lessons/my-lessons');
    myLessons.value = response.data;
  } catch (err) {
    error.value = "Stunden konnten nicht geladen werden.";
  } finally {
    loading.value = false;
  }
};

// 2. Eine neue leere Karte zum Formular hinzufügen
const addCard = () => {
  const card: Card = {
    title: '',
    content: '',
    type: 'TEXT',
    orderIndex: newLesson.value.cards.length + 1
  };
  newLesson.value.cards.push(card);
};

// 3. Karte aus dem Formular entfernen
const removeCard = (index: number) => {
  newLesson.value.cards.splice(index, 1);
};

// 4. Die neue Lektion im Backend speichern
const saveLesson = async () => {
  if (!newLesson.value.title || !newLesson.value.slug) {
    alert("Bitte Titel und Slug angeben!");
    return;
  }

  try {
    const response = await api.post('/lessons', newLesson.value);
    alert("Stunde erfolgreich erstellt!");
    // Zur neuen Stunde navigieren oder Liste aktualisieren
    myLessons.value.push(response.data);
    // Formular zurücksetzen
    newLesson.value = { title: '', slug: '', cards: [] };
  } catch (err) {
    alert("Fehler beim Speichern. Ist der Slug eventuell schon vergeben?");
  }
};

onMounted(fetchLessons);
</script>

<template>
  <div class="max-w-6xl mx-auto p-6 space-y-12">

    <section>
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-extrabold text-slate-900">Meine Unterrichtsstunden</h1>
        <div v-if="loading" class="animate-pulse text-blue-600 font-medium">Lade Daten...</div>
      </div>

      <div v-if="myLessons.length === 0 && !loading" class="bg-white border-2 border-dashed border-slate-200 rounded-xl p-12 text-center text-slate-500">
        <p class="text-lg">Du hast noch keine Stunden erstellt. Fang unten mit deiner ersten an!</p>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="lesson in myLessons" :key="lesson.id"
             class="bg-white p-6 rounded-xl shadow-sm border border-slate-100 hover:shadow-md transition-shadow">
          <h3 class="font-bold text-xl mb-2 text-slate-800">{{ lesson.title }}</h3>
          <p class="text-sm text-slate-400 mb-4">Slug: <span class="font-mono">{{ lesson.slug }}</span></p>
          <div class="flex justify-between items-center">
            <span class="text-xs bg-slate-100 px-2 py-1 rounded text-slate-600">
              {{ lesson.cards?.length || 0 }} Karten
            </span>
            <router-link :to="'/lesson/' + lesson.slug" class="text-blue-600 font-semibold hover:underline">
              Vorschau ➔
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <hr class="border-slate-200" />

    <section class="bg-white rounded-2xl shadow-xl border border-slate-100 overflow-hidden">
      <div class="bg-slate-900 p-6">
        <h2 class="text-xl font-bold text-white text-center">Neue Unterrichtsstunde erstellen</h2>
      </div>

      <div class="p-8 space-y-8">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-bold text-slate-700 mb-2">Titel der Stunde</label>
            <input v-model="newLesson.title" type="text" placeholder="z.B. Einführung in die Photosynthese"
                   class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
          </div>
          <div>
            <label class="block text-sm font-bold text-slate-700 mb-2">URL-Kürzel (Slug)</label>
            <input v-model="newLesson.slug" type="text" placeholder="bio-01-photo"
                   class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
          </div>
        </div>

        <div class="space-y-4">
          <h3 class="text-lg font-bold text-slate-800 flex items-center gap-2">
            Hilfekarten <span class="text-sm font-normal text-slate-400">({{ newLesson.cards.length }})</span>
          </h3>

          <div v-for="(card, index) in newLesson.cards" :key="index"
               class="p-6 bg-slate-50 rounded-xl border border-slate-200 relative group">

            <button @click="removeCard(index)" class="absolute top-4 right-4 text-red-400 hover:text-red-600 transition">
              Entfernen
            </button>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="md:col-span-2">
                <label class="text-xs uppercase font-bold text-slate-400">Kartentitel</label>
                <input v-model="card.title" type="text" class="w-full p-2 border rounded mt-1" />
              </div>
              <div>
                <label class="text-xs uppercase font-bold text-slate-400">Inhaltstyp</label>
                <select v-model="card.type" class="w-full p-2 border rounded mt-1 bg-white text-sm">
                  <option value="TEXT">Reiner Text</option>
                  <option value="MATH">Mathematik (LaTeX)</option>
                  <option value="VIDEO">YouTube Video</option>
                </select>
              </div>
              <div class="md:col-span-3">
                <label class="text-xs uppercase font-bold text-slate-400">Inhalt / Formel / URL</label>
                <textarea v-model="card.content" rows="3" class="w-full p-2 border rounded mt-1 font-mono text-sm"></textarea>
              </div>
            </div>
          </div>

          <button @click="addCard"
                  class="w-full py-4 border-2 border-dashed border-slate-300 rounded-xl text-slate-500 font-bold hover:bg-slate-50 hover:border-blue-400 hover:text-blue-600 transition-all">
            + Hilfekarte hinzufügen
          </button>
        </div>

        <div class="pt-6 border-t flex justify-end">
          <button @click="saveLesson"
                  class="bg-blue-600 text-white px-10 py-3 rounded-xl font-bold shadow-lg shadow-blue-100 hover:bg-blue-700 transition-transform active:scale-95">
            Stunde jetzt veröffentlichen
          </button>
        </div>
      </div>
    </section>
  </div>
</template>
