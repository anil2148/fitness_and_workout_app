# Exercise Media Guide

The app ships with app-owned vector exercise illustrations under `app/src/main/res/drawable`.

Real exercise videos are intentionally not bundled yet. Future local prototypes can place licensed MP4 files under `app/src/main/res/raw` with names such as:

- `video_push_ups.mp4`
- `video_squats.mp4`
- `video_plank.mp4`

For production, prefer a CDN or Firebase Storage so a growing video library does not inflate the APK. Only add media that is owned by the app, properly licensed, or confirmed royalty-free.
