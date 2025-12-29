<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { lessonService } from '../services/lessonService';
import type { Lesson } from '../types';
import MathRenderer from "@/components/MathRenderer.vue";

const route = useRoute();
const lesson = ref<Lesson | null>(null);
const loading = ref(true);
const error = ref<string | null>(null);

onMounted(async () => {
  const slug = route.params.slug as string;
  try {
    lesson.value = await lessonService.getBySlug(slug);
  } catch (err) {
    error.value = 'Unterrichtsstunde konnte nicht geladen werden.';
    console.error(err);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="max-w-6xl mx-auto p-6">
    <header v-if="lesson" class="mb-10 text-center">
      <h1 class="text-4xl font-extrabold text-gray-900 mb-2">{{ lesson.title }}</h1>
      <p class="text-gray-500">Scanne den QR-Code oder nutze diesen Link, um die Karten zu sehen.</p>
    </header>

    <div v-if="loading" class="text-center py-20">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
    </div>

    <div v-else-if="error" class="bg-red-50 text-red-700 p-4 rounded-lg text-center">
      {{ error }}
    </div>

    <div v-else-if="lesson" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="card in lesson.cards" :key="card.id"
           class="bg-white border border-gray-200 rounded-xl shadow-sm hover:shadow-md transition-shadow p-6">

        <div class="flex items-center mb-4">
          <span class="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded mr-2">
            {{ card.type }}
          </span>
          <h2 class="text-xl font-bold text-gray-800">{{ card.title }}</h2>
        </div>

        <div class="text-gray-600 leading-relaxed">
          <p v-if="card.type === 'TEXT'">{{ card.content }}</p>

          <div v-else-if="card.type === 'MATH'" class="bg-gray-50 p-4 rounded-md font-mono text-center overflow-x-auto">
            <MathRenderer :formula="card.content" :display-mode="true"/>
          </div>

          <div v-else-if="card.type === 'VIDEO'" class="aspect-video bg-black rounded-lg flex items-center justify-center text-white">
            <p>Video-Player Platzhalter</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
