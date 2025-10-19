<template>
  <IconBroadcastOff
    v-if="store.offline"
    class="offline"
    v-tooltip.right="
      'Failed to reconnect after several attempts. Please refresh the page to try again.'
    "
  />
  <IconBroadcast
    v-else
    :class="store.status"
    v-tooltip.right="'Current connection status: ' + store.status"
  />
</template>

<script setup>
import { IconBroadcast, IconBroadcastOff } from "@tabler/icons-vue";
import { useOfflineStore } from "../composables/useOfflineStore";
const store = useOfflineStore();
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
</style>
