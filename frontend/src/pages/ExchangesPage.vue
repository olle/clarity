<template>
  <article class="clarity--content-page">
    <h1>Exchanges</h1>
    <ul>
      <li v-for="exchange in store.exchanges">{{ exchange }}</li>
    </ul>
  </article>
</template>

<script setup>
import { onMounted } from "vue";
import { useExchangeStore } from "../stores/useExchangeStore";
const store = useExchangeStore();
onMounted(() => {
  store.reload();
});
</script>
