import { ref, computed } from "vue";
import { useWebSocket } from "@vueuse/core";

export function useEvents() {
  const failed = ref(false);

  const { status, data } = useWebSocket("ws://localhost:8080/ws/v0", {
    autoConnect: true,
    autoReconnect: {
      delay: 12345,
      retries: 2,
      onFailed: () => (failed.value = true),
    },
  });

  const events = computed(() => JSON.parse(data.value));

  return { status, failed, events };
}
