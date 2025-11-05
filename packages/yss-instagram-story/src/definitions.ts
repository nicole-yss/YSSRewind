
export type SourceType = 'remoteUrl' | 'filePath' | 'base64';

export interface ShareVideoOptions {
  sourceType: SourceType;
  /** For 'remoteUrl' supply https URL; for 'filePath' use file://... or path; for 'base64' pass data WITHOUT the prefix. */
  source: string;
  appId: string;            // Meta (Facebook) App ID
  contentUrl?: string;      // Deep link to attach in story
  topColor?: string;        // Hex
  bottomColor?: string;     // Hex
  mimeType?: string;        // Android MIME hint (e.g., 'video/mp4')
}

export interface InstagramStoryPlugin {
  shareVideo(options: ShareVideoOptions): Promise<void>;
}
