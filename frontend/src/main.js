import { createApp } from "vue";

import PrimeVue from "primevue/config";
import Aura from "@primeuix/themes/aura";
import ToastService from "primevue/toastservice";

import "./style.css";
import router from "./router";
import App from "./App.vue";

createApp(App)
  .use(router)
  .use(PrimeVue, {
    ripple: true,
    theme: {
      preset: Aura,
    },
  })
  .use(ToastService)
  .mount("#app");
