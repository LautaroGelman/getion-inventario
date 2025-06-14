import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],

  // ──────────────────────────────────────────────
  // Ajustes de servidor SOLO para npm run dev
  server: {
    port: 5173,          // <-- puerto fijo
    proxy: {
      // ← redirige llamadas a /auth/* hacia el backend
      '/api/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      /* Si tu API expone otros prefijos (ej. /api, /productos),
         añade aquí más entradas copiando el mismo patrón. */
    },
  },
});
