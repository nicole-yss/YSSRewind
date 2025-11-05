
import { registerPlugin } from '@capacitor/core';
import type { InstagramStoryPlugin } from './definitions';

export const InstagramStory = registerPlugin<InstagramStoryPlugin>('InstagramStory', {
  web: () => ({
    async shareVideo() {
      throw new Error('InstagramStory plugin is only available on iOS/Android.');
    },
  } as InstagramStoryPlugin),
});

export * from './definitions';
