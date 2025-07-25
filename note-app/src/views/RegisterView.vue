<template>
  <div class="flex-1 flex items-center justify-center bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
      <h2 class="text-2xl font-bold mb-6 text-center">회원가입</h2>
      
      <!-- Registration Form -->
      <form @submit.prevent="handleInitialRegister" v-if="!isRegistered">
        <div class="mb-4">
          <label for="nickname" class="block text-gray-700 text-sm font-bold mb-2">닉네임</label>
          <input type="text" id="nickname" v-model="nickname" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
        </div>
        <div class="mb-4">
          <label for="email" class="block text-gray-700 text-sm font-bold mb-2">이메일</label>
          <input type="email" id="email" v-model="email" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
        </div>
        <div class="mb-4">
          <label for="password" class="block text-gray-700 text-sm font-bold mb-2">비밀번호</label>
          <input type="password" id="password" v-model="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
        </div>
        <div class="mb-6">
          <label for="confirmPassword" class="block text-gray-700 text-sm font-bold mb-2">비밀번호 재입력</label>
          <input type="password" id="confirmPassword" v-model="confirmPassword" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
        </div>
        <div class="flex items-center justify-between">
          <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
            인증메일 받기
          </button>
        </div>
      </form>

      <!-- Verification Form -->
      <form @submit.prevent="handleVerifyEmail" v-if="isRegistered">
         <p class="mb-4 text-center">{{ email }} 주소로 인증코드를 발송했습니다.</p>
        <div class="mb-4">
          <label for="verificationCode" class="block text-gray-700 text-sm font-bold mb-2">인증 코드</label>
          <input type="text" id="verificationCode" v-model="verificationCode" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
        </div>
        <div class="flex items-center justify-between">
          <button type="submit" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
            인증 완료하기
          </button>
        </div>
      </form>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const nickname = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const verificationCode = ref('');
const isRegistered = ref(false);
const router = useRouter();
const authStore = useAuthStore();

const handleInitialRegister = async () => {
  if (password.value !== confirmPassword.value) {
    alert('비밀번호가 일치하지 않습니다.');
    return;
  }
  try {
    await authStore.register({ 
      nickname: nickname.value, 
      email: email.value, 
      password: password.value 
    });
    isRegistered.value = true;
    alert('회원가입 정보가 등록되었습니다. 이메일 인증을 진행해주세요.');
  } catch (error: any) {
    alert(`회원가입 실패: ${error.response?.data?.message || error.message}`);
  }
};

const handleVerifyEmail = async () => {
  try {
    await authStore.verifyEmail({ 
      email: email.value, 
      code: verificationCode.value 
    });
    alert('이메일 인증이 완료되었습니다. 로그인 페이지로 이동합니다.');
    router.push({ name: 'login' });
  } catch (error: any) {
    alert(`인증 실패: ${error.response?.data?.message || error.message}`);
  }
};

const handleSubmit = () => {
  if (!isRegistered.value) {
    handleInitialRegister();
  } else {
    handleVerifyEmail();
  }
};
</script>

<style scoped>
</style>