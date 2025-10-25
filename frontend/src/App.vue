<template>
  <div class="wrapper">
    <nav>
      <ul class="nav">
        <li>
          <RouterLink to="/">
            <IconHome />
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/brokers">
            <OverlayBadge :value="store.count" size="small">
              <IconRabbitMq />
            </OverlayBadge>
          </RouterLink>
        </li>
        <li class="mt-auto">
          <RouterLink to="/help">
            <IconHelpHexagon />
          </RouterLink>
        </li>
        <li class="disabled" disabled>
          <IconSettings />
        </li>
        <li>
          <OnlineStatus />
        </li>
      </ul>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { RouterLink, RouterView } from "vue-router";
import IconRabbitMq from "./components/IconRabbitMq.vue";
import { IconHome, IconHelpHexagon, IconSettings } from "@tabler/icons-vue";
import { useBrokerStore } from "./composables/useBrokerStore";
import OverlayBadge from "primevue/overlaybadge";
import OnlineStatus from "./components/OnlineStatus.vue";

const store = useBrokerStore();
onMounted(() => {
  store.reload();
});
</script>

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

.mt-auto {
  margin-top: auto;
}

.disabled {
  opacity: 0.3;
  pointer-events: none;
}
</style>
