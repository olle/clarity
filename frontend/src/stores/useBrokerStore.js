import { ref, watch, computed } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../composables/useApi";
import { useEvents } from "../composables/useEvents";
import { useDebounceFn } from "@vueuse/core";

export const useBrokerStore = defineStore("brokers", () => {
  const { events } = useEvents();
  const { brokers: brokersApi } = useApi();

  const brokers = ref({});
  const count = computed(() => Object.keys(brokers.value).length);
  const activeCount = computed(
    () =>
      Object.values(brokers.value).filter((broker) => broker.active).length
  );

  const addBroker = async (values) => {
    await brokersApi.create(values);
  };

  const updateBroker = async (id, values) => {
    const current = brokers.value[id];
    await brokersApi.update(id, { ...current, ...values });
  };

  async function reload() {
    const results = await brokersApi.fetchAll();
    if (!!results) {
      results.elements.forEach(
        (element) => (brokers.value[element.id] = element)
      );
      results.removed.forEach((id) => {
        delete brokers.value[id];
      });
    }
  }

  async function removeBroker(broker) {
    if (await brokersApi.delete(broker)) {
      delete brokers.value[broker.id];
    }
  }

  async function activateBroker(broker) {
    await brokersApi.activate(broker);
  }

  async function deactivateBroker(broker) {
    await brokersApi.deactivate(broker);
  }

  const debouncedReload = useDebounceFn(reload, 789);

  watch(events, async (message) => {
    if (
      message.event.startsWith("broker-updated") ||
      message.event.startsWith("broker-added") ||
      message.event.startsWith("broker-removed") ||
      message.event.startsWith("broker-activated")
    ) {
      await debouncedReload();
    }
  });

  return {
    brokers,
    count,
    activeCount,
    addBroker,
    removeBroker,
    updateBroker,
    reload: useDebounceFn(reload, 123),
    activateBroker,
    deactivateBroker,
  };
});
