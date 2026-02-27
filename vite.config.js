import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Dev proxy so the frontend can call /api/* without CORS headaches
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:4000",
        changeOrigin: true
      },
      "/health": {
        target: "http://localhost:4000",
        changeOrigin: true
      }
    }
  }
})
