
# @yss/instagram-story

Capacitor plugin to share a video directly to **Instagram Stories** with the media pre-attached.
Supports **remote URLs**, **local file paths**, and **base64** on **iOS** and **Android**.

> You still need a **Meta (Facebook) App ID**. Pass it from JS; no App Review is required for native share.

## Install (local path)
```bash
npm i ./yss-instagram-story
npx cap sync
```

## Usage
```ts
import { InstagramStory } from '@yss/instagram-story';

await InstagramStory.shareVideo({
  sourceType: 'remoteUrl',        // 'remoteUrl' | 'filePath' | 'base64'
  source: 'https://example.com/clip.mp4',
  appId: '1502991611003829',      // Meta App ID (yours)
  contentUrl: 'https://yoursalonsupport.com/loyalty-rewind',
  topColor: '#111111',
  bottomColor: '#000000',
  mimeType: 'video/mp4'           // Android hint (optional)
});
```

### iOS setup
Add to your app **Info.plist**:
```xml
<key>LSApplicationQueriesSchemes</key>
<array>
  <string>instagram</string>
  <string>instagram-stories</string>
</array>
```

### Android setup
Add a **FileProvider** and query in your **AndroidManifest.xml** (your app layer, not the plugin):
```xml
<queries>
  <package android:name="com.instagram.android"/>
</queries>

<application>
  <provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.instagramstory.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
      android:name="android.support.FILE_PROVIDER_PATHS"
      android:resource="@xml/filepaths_instagram_story" />
  </provider>
</application>
```
Create `android/app/src/main/res/xml/filepaths_instagram_story.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
  <cache-path name="insta_cache" path="." />
</paths>
```

## Web fallback (optional)
Use the Web Share API for browsers where native isn’t available.

## License
MIT
