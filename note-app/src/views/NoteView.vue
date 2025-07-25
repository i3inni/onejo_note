<template>
  <div class="flex flex-col md:flex-row flex-1 bg-gray-100">
    <!-- 내가 쓴 글 불러오기 -->
    <aside class="w-full md:w-1/4 p-4 bg-white shadow-md">
      <button
        @click="handleNewNoteClick"
        :class="[
          'w-full font-bold py-2 px-4 rounded mb-4 border border-black shadow-md',
          isLoggedIn
            ? 'bg-white text-gray-800 hover:bg-gray-100'
            : 'bg-white text-gray-500 cursor-not-allowed',
        ]"
      >
        새 글 쓰기
      </button>
      <ul>
        <li
          v-for="(note, index) in notes"
          :key="index"
          @click="selectNote(note)"
          class="flex justify-between items-center p-2 rounded cursor-pointer hover:bg-gray-200 border-b border-dotted border-gray-300 pb-2 mb-2"
        >
          <span>{{ note.title }}</span>
          <button @click.stop="deleteNote(note.id)" class="text-red-500 hover:text-red-700">
            x
          </button>
        </li>
      </ul>
    </aside>

    <!-- 우측 : 노트 기능 -->
    <main class="flex-1 p-4">
      <div v-if="!isLoggedIn" class="flex items-center justify-center h-full">
        <p class="text-gray-500 text-xl">로그인 후 사용 가능합니다.</p>
      </div>
      <div v-else class="flex flex-col flex-1 h-full">
        <div v-if="editingNote">
          <div class="bg-white p-6 rounded-lg shadow-md flex flex-col h-200">
            <input
              type="text"
              v-model="editingNote.title"
              placeholder="제목을 입력하세요"
              class="w-full p-3 mb-4 border border-gray-300 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-lg"
            />
            <div class="flex flex-col md:flex-row gap-4 h-150">
              <textarea
                ref="textareaRef"
                v-model="editingNote.content"
                @scroll="syncScroll"
                placeholder="write your note..."
                class="w-full md:w-1/2 p-3 border border-gray-300 rounded-lg shadow-md resize-none overflow-y-auto focus:outline-none focus:ring-2 focus:ring-blue-500 text-base flex-1"
              ></textarea>
              <div
                ref="previewRef"
                class="w-full md:w-1/2 bg-gray-50 p-4 rounded-lg border border-gray-200 shadow-md overflow-y-auto flex-1"
              >
                <h3 class="text-lg font-semibold mb-2 text-gray-700">미리보기</h3>
                <VueMarkdown :source="editingNote.content" class="prose max-w-none" />
              </div>
            </div>
            <div class="mt-10 flex justify-end">
              <button
                @click="saveNote"
                class="bg-blue-500 text-white font-bold py-2 px-4 rounded-lg shadow-md mr-2 hover:bg-blue-600"
              >
                저장
              </button>
              <button
                @click="cancelEdit"
                class="bg-gray-200 text-gray-800 font-bold py-2 px-4 rounded-lg shadow-md hover:bg-gray-300"
              >
                취소
              </button>
            </div>
          </div>
        </div>
        <div v-else-if="selectedNote">
          <div class="bg-white p-6 rounded-lg shadow-md flex flex-col h-200">
            <h2 class="text-2xl font-bold mb-4 pl-2">{{ selectedNote.title }}</h2>
            <div class="prose max-w-none flex-1 overflow-y-auto border border-gray-300 rounded-lg p-3">
              <VueMarkdown :source="selectedNote.content" />
            </div>
            <div class="mt-4 flex justify-end">
              <button
                @click="editNote"
                class="bg-blue-500 text-white font-bold py-2 px-4 rounded-lg shadow-md mr-2 hover:bg-blue-600"
              >
                수정
              </button>
              <button
                @click="deleteNote(selectedNote.id)"
                class="bg-red-500 text-white font-bold py-2 px-4 rounded-lg shadow-md hover:bg-red-600"
              >
                삭제
              </button>
            </div>
          </div>
        </div>
        <div v-else class="flex flex-col items-center justify-center h-full min-h-full">
          <p class="text-gray-500 text-lg text-center my-0">
            왼쪽에서 노트를 선택하거나 새 노트를 작성하세요
          </p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import VueMarkdown from 'vue-markdown-render'
import { useAuthStore } from '@/stores/auth'
import api from '@/services/apiService'

interface Note {
  id: number | null
  title: string
  content: string
}

const notes = ref<Note[]>([])
const selectedNote = ref<Note | null>(null)
const editingNote = ref<Note | null>(null)

const textareaRef = ref<HTMLTextAreaElement | null>(null)
const previewRef = ref<HTMLDivElement | null>(null)

const authStore = useAuthStore()
const isLoggedIn = computed(() => authStore.isLoggedIn)
const router = useRouter()

let isSyncing = false

const syncScroll = () => {
  if (!textareaRef.value || !previewRef.value) return
  if (isSyncing) return

  isSyncing = true

  const textarea = textareaRef.value
  const preview = previewRef.value

  const scrollRatio = textarea.scrollTop / (textarea.scrollHeight - textarea.clientHeight)
  preview.scrollTop = scrollRatio * (preview.scrollHeight - preview.clientHeight)

  nextTick(() => {
    isSyncing = false
  })
}

const fetchNotes = async () => {
  if (isLoggedIn.value) {
    try {
      const response = await api.get('/notes')
      notes.value = response.data
    } catch (error) {
      console.error('Failed to fetch notes:', error)
    }
  }
}

onMounted(() => {
  fetchNotes()
})

function handleNewNoteClick() {
  if (isLoggedIn.value) {
    createNewNote()
  } else {
    router.push({ name: 'login' })
  }
}

function createNewNote() {
  editingNote.value = { id: null, title: '', content: '' }
  selectedNote.value = null
}

function selectNote(note: Note) {
  selectedNote.value = note
  editingNote.value = null
}

function editNote() {
  if (selectedNote.value) {
    editingNote.value = { ...selectedNote.value }
  }
}

async function saveNote() {
  if (!editingNote.value) return

  try {
    if (editingNote.value.id) {
      // Update existing note
      const response = await api.put(`/notes/${editingNote.value.id}`, editingNote.value)
      const updatedNote = response.data
      const index = notes.value.findIndex((n) => n.id === updatedNote.id)
      if (index !== -1) {
        notes.value[index] = updatedNote
      }
      selectNote(updatedNote)
    } else {
      // Create new note
      const response = await api.post('/notes', editingNote.value)
      const newNote = response.data
      notes.value.push(newNote)
      selectNote(newNote)
    }
    editingNote.value = null
  } catch (error) {
    console.error('Failed to save note:', error)
  }
}

function cancelEdit() {
  editingNote.value = null
}

async function deleteNote(id: number | null) {
  if (id === null) return
  try {
    await api.delete(`/notes/${id}`)
    notes.value = notes.value.filter((n) => n.id !== id)
    if (selectedNote.value && selectedNote.value.id === id) {
      selectedNote.value = null
    }
  } catch (error) {
    console.error('Failed to delete note:', error)
  }
}
</script>

<style scoped>
.prose {
  max-width: 100%;
}
</style>
