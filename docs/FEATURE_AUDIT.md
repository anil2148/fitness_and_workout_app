# Fitness App Feature Audit

This audit documents the stabilized offline-first Android app. Statuses describe the implementation at the time of the verified debug build.

## Feature Status

| # | Feature | Status | Notes |
|---|---|---|---|
| 1 | App launch | Working | `MainActivity` creates Room repository and Compose UI. |
| 2 | Splash screen | Fixed | Splash waits for local state, routes new users into onboarding, and routes returning users directly home. |
| 3 | Onboarding | Fixed | Positive-number validation and one-time medical acknowledgement are required before profile save. |
| 4 | Home dashboard | Working | Stats, recommendation, score, habit progress, recent workout, and quick actions render from state. |
| 5 | Bottom navigation | Working | Home, Workouts, Challenges, Progress, and Profile routes are registered and selected correctly. |
| 6 | Workouts list | Fixed | Search, level filters, empty results, and Free or Premium plan cards route safely through warm-up. |
| 7 | Workout detail | Fixed | Exercise list, app-owned illustrations, Premium-safe video placeholders, guide links, safety text, favorite action, manual completion, and guided-player entry work. |
| 8 | Workout player | Fixed | Guided player supports current and next exercise illustrations, video placeholder, start, pause, resume, next, skip, rest countdowns, finish, progress, and safe exit. |
| 9 | Exercise visuals | Fixed | Core exercises use distinct app-owned vector illustrations with a guaranteed `exercise_placeholder.xml` fallback. |
| 10 | Exercise video placeholder | Hidden as Coming Soon | Premium-safe video card uses thumbnails, accessibility labels, and nullable local-video lookup without requiring MP4 files. |
| 11 | Completed workout saving | Working | Guided, manual, and quick workout completion save to Room. |
| 12 | Progress screen | Working | Totals, timeline, reports, PDF preview, and Premium analytics link are safe. |
| 13 | Streak calculation | Fixed | Extracted pure logic with JVM regression test. |
| 14 | Calories burned | Working | Saved completed workouts aggregate into dashboard and reports. |
| 15 | Premium screen | Fixed | Scrollable comparison, mock trial, disabled restore placeholder, terms, and privacy links. |
| 16 | Premium mock unlock | Working | Device-local Room status persists. |
| 17 | Premium locked content | Working | Locked plans and Premium screens route to Premium safely. |
| 18 | Challenges | Working | Free beginner and locked Premium challenge cards display. |
| 19 | Custom workout plan | Working | Premium local rule-based plan generator persists plans. |
| 20 | Diet screen | Working | Premium local guidance only; not medical advice. |
| 21 | BMI calculator | Fixed | Rejects invalid values and uses tested pure calculation. |
| 22 | BMR calculator | Fixed | Rejects invalid values and uses tested pure calculation. |
| 23 | Calorie calculator | Fixed | Uses validated values and extracted calorie-needs calculation. |
| 24 | Water tracker | Fixed | Rejects invalid custom amounts and avoids zero-goal division. |
| 25 | Body measurement tracker | Fixed | Validates positive weight, non-negative values, and body-fat range. |
| 26 | Progress photo placeholder | Working | Local picker and preview are stable with free-tier limit. |
| 27 | Fitness score | Fixed | Ring, level, weekly comparison, and tips display safely. |
| 28 | Quick workout mode | Working | Existing exercise library generates filtered 5, 10, or 15-minute sessions. |
| 29 | Recommended workouts | Working | Local rule engine uses profile, history, skips, location, injury-safe setting, and Premium status. |
| 30 | AI coach placeholders | Hidden as Coming Soon | Premium local mock responses only; no external AI call. |
| 31 | PDF report placeholder | Hidden as Coming Soon | Premium preview works; PDF export button is disabled with Coming Soon label. |
| 32 | Reminder settings | Working | Preferences persist locally; notification scheduling remains placeholder. |
| 33 | Settings screen | Working | Country, language preference, units, diet, location, consent placeholders, and injury-safe setting persist. |
| 34 | Profile screen | Fixed | Edit validation, reset confirmation, and safe links work. |
| 35 | Language settings | Known limitation | Preference persists and resource packs exist; runtime locale switching is not implemented. |
| 36 | Metric/imperial unit settings | Working | Display conversions work; profile entry remains metric. |
| 37 | Privacy policy screen | Working | Local privacy summary renders safely. |
| 38 | Terms screen | Working | Local terms summary renders safely. |
| 39 | Medical disclaimer screen | Working | Packaged localized string is available in all values folders. |
| 40 | Delete data | Fixed | Requires confirmation before local data deletion. |
| 41 | Export data placeholder | Hidden as Coming Soon | Uses the generic stable Coming Soon screen for future CSV / JSON export. |
| 42 | Community placeholder | Working | Local seeded mock feed. |
| 43 | Trainer mode placeholder | Hidden as Coming Soon | Premium stable placeholder only. |
| 44 | Smart calendar | Working | Local weekly summary placeholder. |
| 45 | Recovery score | Fixed | Rejects invalid hours and out-of-range ratings. |
| 46 | Desk worker fitness | Working | Specialized content screen. |
| 47 | Indian fitness | Working | Specialized content and category guidance. |
| 48 | Rate app/share app/feedback | Fixed | Share works; feedback validates and saves locally; rating is a disabled Coming Soon placeholder. |
| 49 | GitHub Actions APK workflow | Fixed | Java 17, Android SDK setup, build, tests, and `fitness-workout-debug-apk` upload. |
| 50 | README instructions | Fixed | Build, verification, APK download, install, placeholder, limitation, and troubleshooting sections. |

## Placeholder Policy

The following integrations intentionally remain offline-safe Coming Soon placeholders: Google Play Billing, AdMob, real AI API calls, exercise videos, PDF export, Firebase login, cloud backup, push notifications, Health Connect, Wear OS, and ML pose detection.

## MVP Verification Matrix

| Feature name | Status | Notes | Tested command or manual check |
|---|---|---|---|
| New-user onboarding | Working | Requires valid values and disclaimer acknowledgement before saving. | Manual code-path check; `./gradlew assembleDebug` |
| Returning-user launch | Fixed | Splash waits for Room-backed state and routes an acknowledged profile directly to Home. | Manual code-path check; `./gradlew assembleDebug` |
| Home dashboard | Working | Cards, quick actions, recommendations, fitness score, and habits read from state. | Manual code-path check |
| Workout search and filter | Fixed | Case-insensitive search, level chips, and empty results are safe. | Manual code-path check; `./gradlew assembleDebug` |
| Workout player | Fixed | Start, pause, resume, next, skip, rest countdown, finish, and safe exit are available. | Manual code-path check; `./gradlew assembleDebug` |
| Exercise media | Fixed | App-owned vectors, fallback lookup, video placeholders, player thumbnails, and exercise guide routing work offline. | `./gradlew testDebugUnitTest`; `./gradlew clean assembleDebug` |
| Completed-workout persistence | Working | Guided, detail, and quick-workout flows save through the Room repository. | Manual code-path check |
| Progress and streak | Working | Dashboard and reports consume persisted history; streak math has regression coverage. | `./gradlew testDebugUnitTest` |
| Health calculators | Fixed | BMI, BMR, calorie needs, and water intake use extracted validated calculations. | `./gradlew testDebugUnitTest` |
| Unit settings | Fixed | Profile and supported tracking displays use metric or imperial formatting. | Manual code-path check |
| Legal and data controls | Working | Settings and Profile route to privacy, terms, medical disclaimer, consent, export, and confirmed deletion. | Manual code-path check |
| Premium and unfinished integrations | Working | Locked content routes to Premium; incomplete production integrations remain explicit Coming Soon placeholders. | Manual code-path check |
| Debug APK generation | Working | Verification script builds and confirms the expected APK path. | `./scripts/verify_app.sh` |

## Database Audit

- Room entities and DAO queries compile through KSP.
- `fallbackToDestructiveMigration()` remains enabled for development schema changes.
- Sample workout, community, and announcement data load only when their corresponding tables are empty.
- DataStore is reserved for lightweight privacy preference support; structured fitness state stays in Room.

## Verification

Run:

```bash
./scripts/verify_app.sh
```

Expected APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Known Limitations

- Debug APK only; Play Store publishing still requires signed release configuration.
- Language preference persists, but runtime locale switching is not wired yet.
- Profile and calculator entry fields use metric input; selected units affect supported displays.
- Progress photos store local picker URIs and are not cloud backed up.
- Notification scheduling and external service integrations remain intentionally disabled.
