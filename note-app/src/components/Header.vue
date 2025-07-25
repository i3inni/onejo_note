<template>
  <header
    class="bg-white text-gray-800 p-4 flex justify-between items-center shadow-md px-5 select-none"
  >
    <router-link to="/" class="text-2xl font-bold no-underline text-gray-800">Note</router-link>
    <nav>
      <router-link to="/" class="mx-2" active-class="underline underline-offset-8 font-bold"
        >Home</router-link
      >
      <router-link to="/note" class="mx-2" active-class="underline underline-offset-8 font-bold"
        >Note</router-link
      >
    </nav>
    <div>
      <div v-if="authStore.isLoggedIn" class="flex items-center">
        <span class="mr-4">{{ authStore.user?.nickname }}님</span>
        <button @click="authStore.logout" class="text-gray-700 font-bold px-4 rounded">
          <span class="material-symbols-outlined align-middle">logout</span>
        </button>
      </div>
      <div v-else>
        <router-link to="/login" class="bg-white font-bold py-2 px-4 mr-2">Login</router-link>
        <router-link to="/register" class="font-bold py-2 px-4 rounded border border-black"
          >Register</router-link
        >
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
</script>
