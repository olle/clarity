import { ref } from "vue";
import { defineStore } from "pinia";
import { usePost } from "./usePost";

export const useBrokerStore = defineStore("brokers", () => {
  const { json } = usePost();

  const brokers = ref([]);

  const addBroker = async (values) => {
    await json("/api/v0/brokers", values);
  };

  return { brokers, addBroker };
});
