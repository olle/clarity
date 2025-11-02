<template>
  <div class="wrapper">
    <nav>
      <ul class="nav">
        <li>
          <RouterLink to="/">
            <IconHome />
          </RouterLink>
        </li>
        <NavItemBrokers />
        <NavItemExchanges />
        <li class="mt-auto">
          <RouterLink to="/help">
            <IconHelpHexagon />
          </RouterLink>
        </li>
        <li>
          <IconSettings @click="showSettingsDialog()" />
          <Dialog
            v-model:visible="settingsVisible"
            modal
            header="Settings"
            position="bottomleft"
          >
            <p class="setting">
              <ToggleSwitch v-model="store.settings.sound" />
              <IconVolume v-if="store.settings.sound" />
              <IconVolumeOff v-else />
              Sound and audio on/off.
            </p>
          </Dialog>
        </li>
        <NavItemOnlineStatus />
      </ul>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import {
  IconHome,
  IconHelpHexagon,
  IconSettings,
  IconVolumeOff,
  IconVolume,
} from "@tabler/icons-vue";
import NavItemBrokers from "./components/NavItemBrokers.vue";
import NavItemOnlineStatus from "./components/NavItemOnlineStatus.vue";
import NavItemExchanges from "./components/NavItemExchanges.vue";
import Dialog from "primevue/dialog";
import ToggleSwitch from "primevue/toggleswitch";
import { ref } from "vue";
import { useSettingsStore } from "./stores/useSettingsStore";

const store = useSettingsStore();

const settingsVisible = ref(false);
const showSettingsDialog = () => {
  settingsVisible.value = true;
};
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
  gap: 20px;
  padding-top: 20px;
  padding-bottom: 20px;
  height: 100%;
}

.mt-auto {
  margin-top: auto;
}

.disabled {
  opacity: 0.3;
  pointer-events: none;
}

.setting {
  display: flex;
  gap: 0.5rem;
}
.setting > :first-child {
  margin-right: 1rem;
}
</style>
