<template>
  <li ref="$root">
    <RouterLink to="/brokers">
      <OverlayBadge :value="store.count" size="small">
        <IconRabbitMq />
      </OverlayBadge>
    </RouterLink>
  </li>
</template>

<script setup>
import { watch, ref, onMounted } from "vue";
import { RouterLink } from "vue-router";
import { storeToRefs } from "pinia";
import IconRabbitMq from "./IconRabbitMq.vue";
import { useBrokerStore } from "../composables/useBrokerStore";
import OverlayBadge from "primevue/overlaybadge";

const store = useBrokerStore();
const $root = ref(null);

const { count } = storeToRefs(store);
watch(count, () => {
  $root.value?.classList.add("animation-shake");
  setTimeout(() => {
    $root.value?.classList.remove("animation-shake");
  }, 1234);
});

onMounted(() => {
  store.reload();
});
</script>
