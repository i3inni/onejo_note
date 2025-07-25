<template>
  <div class="mb-6 pt-10">
    <h2 class="text-2xl font-bold mb-4">오늘의 수업 일정</h2>
    <div class="bg-zinc-50 border-1 border-zinc-200 p-5 rounded-lg shadow-lg text-center">
      <div v-if="todaySchedule">
        <p class="text-lg font-bold">{{ todaySchedule.subject }}</p>
      </div>
      <div v-if="nextSchedule" class="mt-4 pt-4 border-t border-zinc-200">
        <p class="text-sm text-gray-500">다음 : {{ nextSchedule.subject }}</p>
      </div>
      <div v-if="!todaySchedule && !nextSchedule">
        <p class="text-gray-500">오늘 일정이 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { todaySchedules, type Schedule } from '../TodaySchedule'

// Schedule Refs
const todaySchedule = ref<Schedule | null>(null)
const nextSchedule = ref<Schedule | null>(null)

onMounted(() => {
  const today = new Date().getDate()
  const todayIndex = todaySchedules.findIndex((schedule) => schedule.day === today)

  if (todayIndex !== -1) {
    todaySchedule.value = todaySchedules[todayIndex]
    if (todayIndex + 1 < todaySchedules.length) {
      nextSchedule.value = todaySchedules[todayIndex + 1]
    }
  }
})
</script>

<style scoped>
/* Add any specific styles for Schedule.vue here */
</style>
