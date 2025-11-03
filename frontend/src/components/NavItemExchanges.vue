<template>
  <li ref="$root">
    <RouterLink to="/exchanges">
      <IconReplace />
    </RouterLink>
  </li>
</template>

<script setup>
import { watch, ref, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { IconReplace } from "@tabler/icons-vue";
import { useExchangeStore } from "../stores/useExchangeStore";

const store = useExchangeStore();

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
