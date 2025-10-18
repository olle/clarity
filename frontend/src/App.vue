<template>
  <div class="wrapper">
    <nav>
      <ul class="nav">
        <RouterLink to="/"><IconHome /></RouterLink>
        <RouterLink to="/brokers"><IconDatabase /></RouterLink>
        <RouterLink to="/help"><IconHelpHexagon /></RouterLink>
      </ul>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
import { watch } from "vue";
import { RouterLink, RouterView } from "vue-router";
import { IconHome, IconDatabase, IconHelpHexagon } from "@tabler/icons-vue";
import { useOfflineStore } from "./composables/useOfflineStore";

const offlineStore = useOfflineStore();
watch(offlineStore.offline, (v) => console.log(v));
</script>

<style scoped>
.wrapper {
  border-top: 1px solid #eee;
  height: 100%;
  --width: 61px;
  display: grid;
  grid-template-columns: var(--width) calc(100vw - var(--width));
  background: #fcfcfb;
}

nav {
  background-color: white;
  border-right: 1px solid #eee;
  box-shadow: 0px 2px 9px rgba(0, 0, 0, 0.1);
}

.nav {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding-top: 20px;
}

.nav a {
  color: #000;
}
.nav a:active,
.nav a:hover {
  color: var(--color-green);
}
.nav a.router-link-active {
  color: var(--color-green-dark);
}
</style>
