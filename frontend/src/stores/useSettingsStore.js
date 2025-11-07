import { defineStore } from "pinia";
import { useStorage } from "@vueuse/core";

const DEFAULT_SETTINGS = { sound: true, intro: true };

export const useSettingsStore = defineStore("settings", () => {
  const settings = useStorage("clarity_settings", { ...DEFAULT_SETTINGS });

  const toggle = (key) => {
    settings.value[key] = !settings.value[key];
  };

  const reset = () => {
    Object.keys(DEFAULT_SETTINGS).forEach(
      (key) => (settings.value[key] = DEFAULT_SETTINGS[key])
    );
  };

  return {
    settings,
    toggle,
    reset,
  };
});
