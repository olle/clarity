<template>
  <div class="wrapper">
    <nav>
      <ul class="nav">
        <li v-tooltip="'Home - Clarity!'" tooltip-position="right">
          <RouterLink to="/">
            <IconHome />
          </RouterLink>
        </li>
        <NavItemBrokers />
        <NavItemExchanges />
        <li class="mt-auto" v-tooltip="'Help & Documentation'" tooltip-position="right">
          <RouterLink to="/help">
            <IconHelpHexagon />
          </RouterLink>
        </li>
        <NavItemSettings />
        <NavItemOnlineStatus />
      </ul>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import { IconHome, IconHelpHexagon } from "@tabler/icons-vue";
import NavItemBrokers from "./components/NavItemBrokers.vue";
import NavItemOnlineStatus from "./components/NavItemOnlineStatus.vue";
import NavItemExchanges from "./components/NavItemExchanges.vue";
import NavItemSettings from "./components/NavItemSettings.vue";
</script>

<style>
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

<style scoped>
.wrapper {
  border-top: 1px solid #eee;
  height: 100%;
  --width: 65px;
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
  gap: 15px;
  padding-top: 22px;
  padding-bottom: 25px;
  height: 100%;
  max-height: 100vh;
}

.mt-auto {
  margin-top: auto;
}

.disabled {
  opacity: 0.3;
  pointer-events: none;
}
</style>
