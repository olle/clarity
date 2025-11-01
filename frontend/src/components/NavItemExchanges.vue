<template>
  <li>
    <RouterLink to="/exchanges">
      <OverlayBadge :value="store.count" size="small">
        <IconReplace />
      </OverlayBadge>
    </RouterLink>
  </li>
</template>

<script setup>
import { IconReplace } from "@tabler/icons-vue";
import { useExchangeStore } from "../composables/useExchangeStore";
import OverlayBadge from "primevue/overlaybadge";
import { onMounted } from "vue";

const store = useExchangeStore();
onMounted(() => {
  store.reload();
});
</script>
