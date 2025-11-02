<template>
  <li v-tooltip="tooltip">
    <IconBroadcastOff v-if="store.offline" class="icon offline" />
    <IconBroadcast v-else class="icon" ref="$icon" :class="store.status" />
  </li>
</template>

<script setup>
import { watch, ref, computed } from "vue";
import { IconBroadcast, IconBroadcastOff } from "@tabler/icons-vue";
import { useOfflineStore } from "../stores/useOfflineStore";
import { useEvents } from "../composables/useEvents";
import { $dt } from "@primeuix/themes";

const store = useOfflineStore();

const tooltip = computed(() =>
  store.offline
    ? "Failed to reconnect after several attempts. Please refresh the page to try again."
    : "Current connection status: " + store.status
);

const $icon = ref(null);
const { events } = useEvents();

watch(events, (message) => {
  if (message.event.startsWith("now")) {
    $icon.value.classList.add("thump");
    setTimeout(() => {
      $icon.value.classList.remove("thump");
    }, 1234);
  }
});

const thumpColor = $dt("blue.500").value + "B2";
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
    box-shadow: 0 0 0 0 v-bind(thumpColor);
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
