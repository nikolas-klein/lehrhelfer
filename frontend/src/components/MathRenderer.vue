<script setup lang="ts">
import { computed } from 'vue';
import katex from 'katex';

const props = defineProps<{
  formula: string;
  displayMode?: boolean; // true für zentriert, false für inline
}>();

const renderedFormula = computed(() => {
  try {
    return katex.renderToString(props.formula, {
      displayMode: props.displayMode ?? true,
      throwOnError: false
    });
  } catch (e) {
    return `<span class="text-red-500">Fehler in der Formel: ${props.formula}</span>`;
  }
});
</script>

<template>
  <div v-html="renderedFormula" class="math-container overflow-x-auto"></div>
</template>
