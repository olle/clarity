import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  build: {
    outDir: "../target/classes/static",
    emptyOutDir: true,
    rollupOptions: {
      output: {
        manualChunks: {
          "tabler-icons": ["@tabler/icons-vue"],
          primevue: ["primevue"],
        },
      },
    },
  },
  server: {
    proxy: {
      "/api": "http://localhost:8080",
      "/ws": { target: "http://localhost:8080", ws: true },
    },
  },
});
