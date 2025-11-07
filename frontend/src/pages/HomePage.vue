<template>
  <article>
    <h1 @click="clearIntro()" v-if="store.settings.intro">
      Welcome to <em class="highlight">Clarity!</em>
    </h1>
    <aside v-if="!store.settings.intro">
      <h1 class="highlight">Clarity!</h1>
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

aside {
  margin: 0;
  padding: 0;
  font-size: 4pt;
  font-style: italic;
  position: fixed;
  top: 5px;
  right: 20px;
}
</style>
