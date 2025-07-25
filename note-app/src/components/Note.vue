<template>
  <main class="flex-1 p-4 flex flex-col p-10">
    <div class="bg-white p-6 rounded-lg shadow-md flex flex-col h-160">
      <h2 class="text-2xl font-bold mb-4 text-gray-800">새로운 노트 작성</h2>
      <input
        type="text"
        v-model="noteTitle"
        placeholder="제목을 입력하세요"
        class="w-full p-3 mb-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-lg"
      />
      <div class="flex flex-col md:flex-row gap-4 h-100">
        <textarea
          ref="editorRef"
          v-model="noteContent"
          @scroll="handleScroll"
          placeholder="write your note..."
          class="w-full md:w-1/2 p-3 border border-gray-300 rounded-lg resize-none overflow-y-auto focus:outline-none focus:ring-2 focus:ring-blue-500 text-base flex-1"
        ></textarea>
        <div
          ref="previewRef"
          class="w-full md:w-1/2 bg-gray-50 p-4 rounded-lg border border-gray-200 overflow-y-auto flex-1"
        >
          <h3 class="text-lg font-semibold mb-2 text-gray-700">미리보기</h3>
          <VueMarkdown :source="noteContent" class="prose max-w-none" />
        </div>
      </div>
      <div class="my-4 flex justify-end items-center">
        <button
          @click="downloadNote"
          class="font-bold p-2 rounded-lg transition duration-300 ease-in-out transform hover:scale-105"
        >
          <span class="material-symbols-outlined align-middle">download</span>
        </button>
        <button
          @click="saveNote"
          class="font-bold py-2 px-5 rounded-lg mr-2 transition duration-300 ease-in-out transform hover:scale-105"
        >
          Save
        </button>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import VueMarkdown from 'vue-markdown-render'
import { useAuthStore } from '@/stores/auth'
import api from '@/services/apiService'

const noteTitle = ref('')
const noteContent = ref('')

const editorRef = ref<HTMLTextAreaElement | null>(null)
const previewRef = ref<HTMLDivElement | null>(null)

const authStore = useAuthStore()

const handleScroll = () => {
  if (!editorRef.value || !previewRef.value) return

  const editor = editorRef.value
  const preview = previewRef.value

  const scrollRatio = editor.scrollTop / (editor.scrollHeight - editor.clientHeight)
  preview.scrollTop = scrollRatio * (preview.scrollHeight - preview.clientHeight)
}

const saveNote = async () => {
  if (authStore.isLoggedIn) {
    try {
      const noteData = {
        title: noteTitle.value,
        content: noteContent.value,
      }
      await api.post('/notes', noteData)
      alert('노트가 성공적으로 저장되었습니다!')
      noteTitle.value = ''
      noteContent.value = ''
    } catch (error: any) {
      console.error('노트 저장 실패:', error.response?.data || error.message)
      alert(`노트 저장에 실패했습니다: ${error.response?.data?.message || error.message}`)
    }
  } else {
    alert('로그인 후 저장 가능합니다. 노트 내용을 다운로드합니다.')
    downloadNote()
  }
}

const downloadNote = () => {
  const filename = `${noteTitle.value || 'untitled'}.md`
  const element = document.createElement('a')
  element.setAttribute(
    'href',
    'data:text/plain;charset=utf-8,' + encodeURIComponent(noteContent.value),
  )
  element.setAttribute('download', filename)
  element.style.display = 'none'
  document.body.appendChild(element)
  element.click()
  document.body.removeChild(element)
}
</script>

<style scoped>
/* Add any specific styles for Note.vue here */
</style>
