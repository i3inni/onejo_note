<template>
  <div class="flex flex-col md:flex-row flex-1 bg-gray-100 h-full">
    <!-- 좌측 : 스케줄, 날씨 -->
    <aside class="w-full md:w-1/4 p-4 bg-white shadow-md">
      <Schedule />
      <Weather />
    </aside>

    <!-- 우측 : 노트 -->
    <Note />
  </div>
</template>

<script setup lang="ts">
import Note from '../components/Note.vue'
import Weather from '../components/Weather.vue'
import Schedule from '../components/Schedule.vue'
</script>

<style scoped></style>
