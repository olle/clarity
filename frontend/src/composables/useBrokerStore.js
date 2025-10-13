import { ref } from "vue";
import { defineStore } from "pinia";

export const useBrokerStore = defineStore("brokers", () => {
  const brokers = ref([]);
  const newBroker = ref({});
  return { brokers, newBroker };
});
