import { ref, watch } from "vue";
import { defineStore } from "pinia";
import { useApi } from "./useApi";
import { useEvents } from "./useEvents";

export const useBrokerStore = defineStore("brokers", () => {
  const { events } = useEvents();
  const { brokers: brokersApi } = useApi();

  const brokers = ref({});

  const addBroker = async (values) => {
    await brokersApi.create(values);
  };

  async function reload() {
    const results = await brokersApi.fetchAll();
    if (!!results) {
      results.elements.forEach(
        (element) => (brokers.value[element.id] = element)
      );
    }
  }

  async function removeBroker(broker) {
    await brokersApi.delete(broker);
    delete brokers.value[broker.id];
  }

  watch(events, async (message) => {
    if (
      message.event.startsWith("broker-updated") ||
      message.event.startsWith("broker-added") ||
      message.event.startsWith("broker-removed")
    ) {
      await reload();
    }
  });

  return { brokers, addBroker, removeBroker, reload };
});
