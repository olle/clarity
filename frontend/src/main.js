import { createApp } from "vue";
import { createPinia } from "pinia";
import PrimeVue from "primevue/config";
import { definePreset } from "@primeuix/themes";
import Aura from "@primeuix/themes/aura";
import Tooltip from "primevue/tooltip";

import "./style.css";
import "./assets/fonts/SourceSans3-VariableFont_wght.ttf";
import router from "./router";
import App from "./App.vue";

const pinia = createPinia();

const clarifyPreset = definePreset(Aura, {
  primitive: {
    borderRadius: {
      none: "0",
      xs: "2px",
      sm: "3px",
      md: "5px",
      lg: "7px",
      xl: "11px",
    },
  },
});

createApp(App)
  .use(pinia)
  .use(router)
  .use(PrimeVue, {
    ripple: true,
    theme: {
      preset: clarifyPreset,
    },
  })
  .directive("tooltip", Tooltip)
  .mount("#app");
