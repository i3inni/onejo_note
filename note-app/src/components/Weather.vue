<template>
  <div class="pt-5">
    <h2 class="text-2xl font-bold mb-4">오늘의 날씨</h2>
    <!-- Current Weather -->
    <div
      v-if="currentWeather"
      class="bg-zinc-50 border-1 border-zinc-200 p-10 rounded-lg shadow-lg text-center w-full"
    >
      <h2 class="text-lg font-semibold mb-7 flex items-center justify-center">
        <svg
          class="w-6 h-6 mr-2 text-gray-600"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.828 0L6.343 16.657a8 8 0 1111.314 0z"
          ></path>
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
          ></path>
        </svg>
        <p>한국교통대학교</p>
      </h2>
      <div class="flex justify-between items-center mb-4">
        <div class="text-left">
          <p class="text-5xl font-bold mb-5">{{ Math.round(currentWeather.main.temp) }}°C</p>
          <p class="text-sm text-gray-600">습도: {{ currentWeather.main.humidity }}%</p>
        </div>
        <div class="flex-shrink-0 p-3">
          <svg
            v-if="weatherIconPath"
            :class="weatherIconPath.class"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              :d="weatherIconPath.d"
            ></path>
          </svg>
        </div>
      </div>
    </div>
    <div v-else-if="weatherError" class="bg-red-100 p-4 rounded-lg text-red-700">
      <p>날씨 정보를 불러오는데 실패했습니다: {{ weatherError }}</p>
    </div>
    <div v-else class="bg-zinc-50 border-1 border-zinc-200 p-6 rounded-lg shadow-lg text-center">
      <p>날씨 정보를 불러오는 중...</p>
    </div>

    <!-- Forecast -->
    <div
      v-if="tomorrowWeather"
      class="bg-zinc-50 border-1 border-zinc-200 p-5 rounded-lg shadow-lg text-center w-full mt-4"
    >
      <h2 class="text-xl font-semibold mb-4">내일 아침 날씨 (9시)</h2>
      <div class="flex justify-center items-center">
        <p class="text-3xl font-bold mr-4">{{ Math.round(tomorrowWeather.main.temp) }}°C</p>
        <svg
          v-if="forecastIconPath"
          :class="forecastIconPath.class"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            :d="forecastIconPath.d"
          ></path>
        </svg>
      </div>
    </div>
    <div v-else-if="forecastError" class="bg-red-100 p-4 rounded-lg text-red-700 w-full mt-4">
      <p>내일 날씨 정보를 불러오는데 실패했습니다: {{ forecastError }}</p>
    </div>
    <div
      v-else
      class="bg-zinc-50 border-1 border-zinc-200 p-4 rounded-lg shadow-lg text-center w-full mt-4"
    >
      <p>내일 날씨 정보를 불러오는 중...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { getCurrentWeather, getForecast } from '../services/weatherService'

// Weather Refs
const currentWeather = ref<any>(null)
const weatherError = ref<string | null>(null)
const tomorrowWeather = ref<any>(null)
const forecastError = ref<string | null>(null)

// Weather Icon Logic
const getIconPath = (weatherId: number, iconClass: string) => {
  if (weatherId >= 200 && weatherId < 300)
    return { d: 'M13 10V3L4 14h7v7l9-11h-7z', class: `${iconClass} text-yellow-600` } // Bolt
  if (weatherId >= 300 && weatherId < 400)
    return {
      d: 'M14 10l-2 2m0 0l-2 2m2-2V3m2 7a4 4 0 01-4 4H7a4 4 0 01-4-4V7a4 4 0 014-4h4a4 4 0 014 4v3z',
      class: `${iconClass} text-blue-500`,
    } // Drizzle
  if (weatherId >= 500 && weatherId < 600)
    return {
      d: 'M14 10l-2 2m0 0l-2 2m2-2V3m2 7a4 4 0 01-4 4H7a4 4 0 01-4-4V7a4 4 0 014-4h4a4 4 0 014 4v3z',
      class: `${iconClass} text-blue-600`,
    } // Rain
  if (weatherId >= 600 && weatherId < 700)
    return {
      d: 'M14 10l-2 2m0 0l-2 2m2-2V3m2 7a4 4 0 01-4 4H7a4 4 0 01-4-4V7a4 4 0 014-4h4a4 4 0 014 4v3z',
      class: `${iconClass} text-blue-300`,
    } // Snow
  if (weatherId >= 700 && weatherId < 800)
    return {
      d: 'M3 15a4 4 0 004 4h9a5 5 0 00-1.727-9.999L15 9a3 3 0 00-3-3H9a3 3 0 00-3 3v.5M3 15a4 4 0 004 4h9a5 5 0 00-1.727-9.999L15 9a3 3 0 00-3-3H9a3 3 0 00-3 3v.5',
      class: `${iconClass} text-gray-400`,
    } // Atmosphere
  if (weatherId === 800)
    return {
      d: 'M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z',
      class: `${iconClass} text-yellow-500`,
    } // Clear
  if (weatherId > 800 && weatherId < 900)
    return {
      d: 'M3 15a4 4 0 004 4h9a5 5 0 00-1.727-9.999L15 9a3 3 0 00-3-3H9a3 3 0 00-3 3v.5M3 15a4 4 0 004 4h9a5 5 0 00-1.727-9.999L15 9a3 3 0 00-3-3H9a3 3 0 00-3 3v.5',
      class: `${iconClass} text-gray-500`,
    } // Clouds
  return {
    d: 'M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z',
    class: `${iconClass} text-gray-400`,
  } // Default
}

const weatherIconPath = computed(() => {
  if (!currentWeather.value) return null
  const weatherId = currentWeather.value.weather[0].id
  return getIconPath(weatherId, 'w-16 h-16 md:w-24 md:h-24')
})

const forecastIconPath = computed(() => {
  if (!tomorrowWeather.value) return null
  const weatherId = tomorrowWeather.value.weather[0].id
  return getIconPath(weatherId, 'w-12 h-12')
})

onMounted(async () => {
  // Fetch weather data
  try {
    currentWeather.value = await getCurrentWeather()
  } catch (error: any) {
    console.error('Error fetching current weather:', error)
    weatherError.value = error.message
  }

  // Fetch forecast data
  try {
    const forecast = await getForecast()
    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    tomorrow.setHours(9, 0, 0, 0)
    const targetTimeUTC = tomorrow.getTime() - 9 * 60 * 60 * 1000

    let closestForecast = null
    let minTimeDiff = Infinity

    forecast.list.forEach((item: any) => {
      const forecastTime = new Date(item.dt * 1000).getTime()
      const timeDiff = Math.abs(forecastTime - targetTimeUTC)
      if (timeDiff < minTimeDiff) {
        minTimeDiff = timeDiff
        closestForecast = item
      }
    })

    if (closestForecast && minTimeDiff <= 3 * 60 * 60 * 1000) {
      tomorrowWeather.value = closestForecast
    } else {
      forecastError.value = '내일 오전 9시 근처의 예보를 찾을 수 없습니다.'
    }
  } catch (error: any) {
    console.error('Error fetching forecast:', error)
    forecastError.value = error.message
  }
})
</script>

<style scoped>
/* Add any specific styles for Weather.vue here */
</style>
