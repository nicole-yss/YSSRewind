# YSS Stories (Static Site)

A single-file mobile site that plays 5 "story" video slides with tap left/right, swipe, hold-to-pause, progress bars, and a Share panel for Instagram.

## Deploy to Vercel (GitHub flow)

1. Create a new GitHub repo (e.g., `yss-stories`).
2. Add this `index.html` to the repo root and commit.
3. Go to Vercel → **New Project** → **Import Git Repository**.
4. Pick your repo. Framework preset: **Other** (no build step).
5. Root directory: `/` (contains `index.html`). Click **Deploy**.

That’s it. Every push to your repo redeploys automatically.

## Deploy with Vercel CLI (no GitHub)

```bash
npm i -g vercel
vercel deploy --prod
```

Run the command in the folder that contains `index.html`.

## Notes

- Sound is **on by default** after the user taps to start (required by mobile browser policies).
- The Share button uses the Web Share API. If “Share with files” isn’t supported or CORS blocks fetching the video, it falls back to sharing the URL, and finally to downloading the file for manual upload to Instagram Stories.
- The video is referenced directly from Google Cloud Storage:
  `https://storage.googleapis.com/yss_media/YSSRewindv4.mp4`

### Customize

To change slides to different videos, open `index.html` and replace:

```js
const SLIDE_COUNT = 5;
const slides = Array.from({ length: SLIDE_COUNT }, () => VIDEO_URL);
```

with:

```js
const slides = [
  "https://.../clip1.mp4",
  "https://.../clip2.mp4",
  "https://.../clip3.mp4",
  "https://.../clip4.mp4",
  "https://.../clip5.mp4",
];
```

(Then remove `SLIDE_COUNT` or set it to `slides.length`.)

---

Made for Nicole @ YSS.
