import ting from "../assets/sounds/ting.mp3";

import { useSound } from "@vueuse/sound";
import { useSettingsStore } from "../stores/useSettingsStore";

const volumes = {
  low: 0.3,
  medium: 0.7,
  high: 1.0,
};

export function useSounds() {

  const store = useSettingsStore();

  const { play: playBells } = useSound(ting, {
    volume: volumes.low,
    interrupt: true,
  });

  const bells = () => {
    store.settings.sound && playBells();
  };

  return { bells };
}
