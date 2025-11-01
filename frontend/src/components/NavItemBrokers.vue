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
  $root.value?.classList.add("shake");
  setTimeout(() => {
    $root.value?.classList.remove("shake");
  }, 345);
});

onMounted(() => {
  store.reload();
});
</script>

<style scoped>
a {
  color: var(--primary);
}
a:active,
a:hover {
  color: var(--color-green);
}
a.router-link-active {
  color: var(--color-green-dark);
}

.shake {
  animation: wiggle 200ms 3;
}

@keyframes wiggle {
  0% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(5deg);
  }
  75% {
    transform: rotate(-5deg);
  }
  100% {
    transform: rotate(0deg);
  }
}
</style>
