import tingelingelingSound from "../assets/sounds/ting.mp3";
import tocSound from "../assets/sounds/toc.mp3";

import { useSound } from "@vueuse/sound";
import { useSettingsStore } from "../stores/useSettingsStore";

const volumes = {
  low: 0.3,
  medium: 0.7,
  high: 1.0,
};

export function useSounds() {
  const store = useSettingsStore();

  const { play: playBells } = useSound(tingelingelingSound, {
    volume: volumes.low,
    interrupt: true,
  });

  const { play: playToc } = useSound(tocSound, {
    volume: volumes.medium,
    interrupt: true,
  });

  const bells = () => {
    store.settings.sound && playBells();
  };

  const toc = () => {
    store.settings.sound && playToc();
  };

  return { bells, toc };
}
