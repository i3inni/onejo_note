/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_OPENWEATHERMAP_API_KEY: string
  readonly VITE_OPENWEATHERMAP_API_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
