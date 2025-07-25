<template>
  <div v-html="iconHtml" class="w-24 h-24"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{ iconCode: string }>();

const iconHtml = computed(() => {
  const iconUrl = `https://openweathermap.org/img/wn/${props.iconCode}@4x.png`;
  return `<img src="${iconUrl}" alt="weather icon" />`;
});
</script>
