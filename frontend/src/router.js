import { createRouter, createMemoryHistory } from "vue-router";

import HomePage from "./pages/HomePage.vue";
import BrokersPage from "./pages/BrokersPage.vue";
import ExchangesPage from "./pages/ExchangesPage.vue";
import HelpPage from "./pages/HelpPage.vue";

const routes = [
  { path: "/", component: HomePage },
  { path: "/brokers", component: BrokersPage },
  { path: "/exchanges", component: ExchangesPage },
  { path: "/help", component: HelpPage },
];

const router = createRouter({
  history: createMemoryHistory(),
  routes,
});

export default router;
