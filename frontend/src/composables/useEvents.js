import { ref, computed, watch } from "vue";
import { useWebSocket } from "@vueuse/core";

const webSocketFailed = ref(false);
const { status, data } = useWebSocket("ws://localhost:8080/ws/v0", {
  autoConnect: true,
  autoReconnect: {
    delay: 12345,
    retries: 5,
    onFailed: () => (webSocketFailed.value = true),
  },
});

const webSocketStatus = ref("CONNECTING");
watch(status, (curr, prev) => {
  if (prev === "CONNECTING" && curr === "CLOSED") {
    webSocketStatus.value = "RECONNECTING";
  } else {
    webSocketStatus.value = curr;
  }
});

export function useEvents() {
  const events = computed(() => JSON.parse(data.value));
  return { status: webSocketStatus, failed: webSocketFailed, events };
}
