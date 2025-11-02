import { defineStore } from "pinia";
import { useStorage } from "@vueuse/core";

const DEFAULT_SETTINGS = { sound: true };

export const useSettingsStore = defineStore("settings", () => {
  const settings = useStorage("clarity_settings", { ...DEFAULT_SETTINGS });

  const toggle = (key) => {
    settings.value[key] = !settings.value[key];
  };

  return {
    settings,
    toggle,
  };
});
