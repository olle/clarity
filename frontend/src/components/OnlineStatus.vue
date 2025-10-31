<template>
  <IconBroadcastOff
    v-if="store.offline"
    class="icon offline"
    v-tooltip.right="
      'Failed to reconnect after several attempts. Please refresh the page to try again.'
    "
  />
  <IconBroadcast
    v-else
    class="icon"
    ref="$icon"
    :class="[store.status, tictoc]"
    v-tooltip.right="'Current connection status: ' + store.status"
  />
</template>

<script setup>
import { watch, ref, onMounted } from "vue";
import { IconBroadcast, IconBroadcastOff } from "@tabler/icons-vue";
import { useOfflineStore } from "../composables/useOfflineStore";
import { useEvents } from "../composables/useEvents";
const store = useOfflineStore();

const $icon = ref(null);
const { events } = useEvents();
watch(events, (message) => {
  if (message.event.startsWith("now")) {
    console.log("HERE", message);
    $icon.value.classList.add("thump");
    setTimeout(() => {
      $icon.value.classList.remove("thump");
    }, 1234);
  }
});
</script>

<style scoped>
.OPEN {
  color: var(--p-blue-500);
}
.CLOSED {
  color: var(--p-zinc-400);
}
.CONNECTING {
  color: var(--p-amber-300);
}
.offline {
  color: var(--p-rose-400);
}
.thump {
  border-radius: 50%;
  animation: pulse 2s;
}

@keyframes pulse {
  0% {
    transform: scale(0.8);
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0.7);
  }

  70% {
    transform: scale(1);
    box-shadow: 0 0 0 10px rgba(0, 0, 0, 0);
  }

  100% {
    transform: scale(0.95);
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0);
  }
}
</style>
