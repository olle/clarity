import { ref, watch, computed } from "vue";
import { defineStore } from "pinia";
import { useApi } from "./useApi";
import { useEvents } from "./useEvents";

export const useExchangeStore = defineStore("exchanges", () => {
  const { events } = useEvents();
  const { exchanges: exchangesApi } = useApi();

  const exchanges = ref({});
  const count = computed(() => Object.keys(exchanges.value).length);

  async function reload() {
    const results = await exchangesApi.fetchAll();
    if (!!results) {
      results.elements.forEach(
        (element) => (exchanges.value[element.id] = element)
      );
    }
  }

  watch(events, async (message) => {
    if (message.event.startsWith("exchange-")) {
      await reload();
    }
  });

  return { exchanges, count, reload };
});
