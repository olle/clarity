import tingelingelingSound from "../assets/sounds/ting.mp3";
import tocSound from "../assets/sounds/toc.mp3";

import { useSound } from "@vueuse/sound";
import { useSettingsStore } from "../stores/useSettingsStore";

const volumes = {
  low: 0.3,
  medium: 0.6,
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

  const maybePlay = (soundFunc) => {
    if (store.settings.sound) {
      soundFunc();
    }
  };

  const bells = () => maybePlay(playBells);
  const toc = () => maybePlay(playToc);

  return { bells, toc };
}
