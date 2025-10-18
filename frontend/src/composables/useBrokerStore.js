import { ref } from "vue";
import { defineStore } from "pinia";
import { usePostJson } from "./usePostJson";

export const useBrokerStore = defineStore("brokers", () => {
  const { post } = usePostJson();

  const brokers = ref([]);

  const addBroker = async (values) => {
    post("/api/v0/brokers", values);
  };

  return { brokers, addBroker };
});
