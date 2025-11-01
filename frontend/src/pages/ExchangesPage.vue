<template>
  <article class="clarity--content-page">
    <h1>Exchanges</h1>
    <ul>
      <li>...</li>
    </ul>
  </article>
</template>

<script setup>
import { onMounted } from "vue";
import { useExchangeStore } from "../composables/useExchangeStore";
const store = useExchangeStore();
onMounted(() => {
  store.reload();
});
</script>
