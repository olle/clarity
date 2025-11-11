<template>
  <article>
    <h1 @click="clearIntro()" v-if="store.settings.intro">
      Welcome to <em class="highlight">Clarity!</em>
    </h1>
    <aside v-if="!store.settings.intro">
      <h1 class="highlight" v-tooltip="'Clarity!'">C</h1>
    </aside>
  </article>
</template>

<script setup>
import { useSettingsStore } from "../stores/useSettingsStore";
const store = useSettingsStore();
function clearIntro() {
  store.settings.intro = false;
}
</script>

<style scoped>
article {
  align-self: center;
  justify-content: center;
  align-items: center;
  align-content: center;
  text-align: center;
}
h1 {
  font-size: 3.2em;
  line-height: 1.1;
}
.highlight {
  color: var(--color-green);
}

aside > h1 {
  width: 20px;
  height: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  margin: 0;
  padding: 0;
  position: fixed;
  top: 25px;
  right: 25px;
  font-size: 10pt;
  font-weight: 900;
  font-style: italic;
  position: fixed;
  color: white !important;
  background: var(--color-green);
}
</style>
