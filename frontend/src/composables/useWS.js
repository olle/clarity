import { watch } from "vue";
import { useWebSocket } from "@vueuse/core";

export function useWS() {
  const { status, data, send, open, close } = useWebSocket(
    "ws://localhost:8080/ws/v0",
    { autoConnect: true, autoReconnect: { delay: 11123 } }
  );
  watch(status, (s) => console.log(s));
  watch(data, (s) => console.log(s));
  setInterval(() => send("Mellon!"), 12345);
}
