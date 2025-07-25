<script setup lang="ts">
import { onMounted } from 'vue';
import { RouterView } from 'vue-router';
import Header from './components/Header.vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

onMounted(() => {
  authStore.initializeAuth();
});
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <Header class="flex-shrink-0" />
    <RouterView class="flex-grow" />
  </div>
</template>

<style scoped></style>
