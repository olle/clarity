import { ref, watch } from "vue";
import { defineStore } from "pinia";
import { useEvents } from "./useEvents";

export const useOfflineStore = defineStore("offline", () => {

  const { failed, status: webSocketStatus } = useEvents();
  
  const status = webSocketStatus;

  const offline = ref(false);
  watch(failed, (hasFailed) => (offline.value = !!hasFailed));

  return { offline, status };
});
