<template>
  <li v-tooltip="'Settings'" tooltip-position="right">
    <IconSettings @click="showSettingsDialog()" />
    <Dialog
      v-model:visible="settingsVisible"
      modal
      header="Settings"
      position="bottomleft"
    >
      <p class="setting">
        <ToggleSwitch
          v-model="store.settings.sound"
          @click="!store.settings.sound && toc(true)"
        />
        <IconVolume v-if="store.settings.sound" />
        <IconVolumeOff v-else />
        Sound and audio on/off.
      </p>
      <p class="setting single">
        <Button
          label="Reset to factory defaults"
          severity="danger"
          variant="outlined"
          @click.confirm="store.reset()"
        >
          <template #icon>
            <IconReload />
          </template>
        </Button>
      </p>
    </Dialog>
  </li>
</template>

<script setup>
import { ref } from "vue";
import { useSettingsStore } from "../stores/useSettingsStore";
import { useSounds } from "../composables/useSounds";
import Dialog from "primevue/dialog";
import Button from "primevue/button";
import ToggleSwitch from "primevue/toggleswitch";
import {
  IconSettings,
  IconVolumeOff,
  IconVolume,
  IconReload,
} from "@tabler/icons-vue";

const { toc } = useSounds();
const store = useSettingsStore();
const settingsVisible = ref(false);
const showSettingsDialog = () => {
  settingsVisible.value = true;
};
</script>

<style scoped>
.setting {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}
.setting > :first-child {
  margin-right: 1rem;
  justify-content: center;
}
.setting.single > :first-child {
  width: 100%;
  margin: auto;
}
</style>
