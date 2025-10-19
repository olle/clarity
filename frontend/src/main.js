import { createApp } from "vue";
import { createPinia } from "pinia";
import PrimeVue from "primevue/config";
import Aura from "@primeuix/themes/aura";
import ToastService from "primevue/toastservice";
import Tooltip from "primevue/tooltip";

import "./style.css";
import router from "./router";
import App from "./App.vue";

const pinia = createPinia();

createApp(App)
  .use(pinia)
  .use(router)
  .use(PrimeVue, {
    ripple: true,
    theme: {
      preset: Aura,
    },
  })
  .use(ToastService)
  .directive("tooltip", Tooltip)
  .mount("#app");
