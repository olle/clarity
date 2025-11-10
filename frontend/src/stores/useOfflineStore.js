import { ref, watch } from "vue";
import { defineStore } from "pinia";
import { useEvents } from "../composables/useEvents";

export const useOfflineStore = defineStore("offline", () => {
  const { failed, status: webSocketStatus } = useEvents();

  const offline = ref(false);
  watch(failed, (hasFailed) => (offline.value = !!hasFailed));

  return { offline, status: webSocketStatus };
});
