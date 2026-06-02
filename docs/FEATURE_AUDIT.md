# Fitness App Feature Audit

This audit records the stabilized offline-first MVP. Automated checks and source-level route audits are complete. Final touch testing on a physical Android phone or emulator is tracked separately in `docs/MANUAL_QA_CHECKLIST.md`.

## Feature Status

| Feature/Page | Status | Tested | Fix Applied | Notes |
|---|---|---|---|---|
| Gradle project and Android manifest | Working | Debug compilation, Room KSP generation, resources, launcher activity, and RTL manifest flag | No additional change required | Minimum SDK 26 and target SDK 35 remain intact. |
| Splash and returning-user launch | Working | Source audit of loading state, profile lookup, medical acknowledgement, and splash back-stack removal | No additional change required | Device touch test remains in the manual checklist. |
| Fresh-install onboarding | Fixed | Empty, non-numeric, zero, and negative profile input paths; acknowledgement requirement; local save sequence | Added friendly validation error and safe parsing before profile persistence | Onboarding choices now wrap on narrow screens. |
| Home dashboard | Working | Null-safe greeting, stats, recommendation, fitness score, habits, recent plan, and quick-action routes | No additional change required | Empty plan state remains safe while Room seeds local samples. |
| Bottom navigation | Working | Home, Workouts, Challenges, Progress, and Profile routes against `NavHost` registrations | No additional change required | Selected-tab state uses the active destination route. |
| Workout list and search | Working | Search, level filtering, favorites route, challenge tab, Premium plan lock, and empty results | Added challenge-loading empty state | Sample plans seed only when the Room plan table is empty. |
| Workout detail | Fixed | Plan loading, exercise loading, favorite toggle, illustration fallback, Premium video placeholder, timer, finish, and stop paths | Added friendly loading states for unavailable plan or exercise data | Manual completion requires each exercise before save. |
| Guided workout player | Fixed | Safe exercise indexing, current and next illustration, video placeholder, timer, rest, pause, resume, next, skip, finish, and stop warning | Made player content scrollable on small screens | Missing exercise data displays a safe loading message. |
| Completed workout persistence | Working | Repository save paths for detail, guided player, and quick workouts | No additional change required | Saves history, share summary, habit progress, and achievements locally. |
| Exercise media fallback | Working | Drawable resource lookup, missing-name fallback, vector resources, accessibility descriptions, and nullable local-video lookup | No additional change required | Real MP4 files are optional and absent by design. |
| Premium mock unlock and pricing | Fixed | Room-backed Premium status, selectable repository-backed pricing cards, exact numeric regional mock amounts, localized currency formatting, promotional cards, lock routing, ad placeholder hiding, and disabled restore placeholder | Corrected GBP, BRL, AED, and SAR preview values; retained a Billing-ready repository boundary | Real Google Play Billing remains Coming Soon. |
| Promo code, referral code, and affiliate store | Fixed | Premium-screen buttons, route registrations, back behavior, and placeholder copy | Added reachable Coming Soon pages | No redemption, tracking, or external store integration is connected. |
| Challenges | Fixed | List route, lock behavior, detail routing through warm-up, and empty-data behavior | Added explicit loading empty state | Advanced challenge logic remains local. |
| Water tracker | Fixed | Positive custom amount validation, zero-goal progress safety, reset action, and habit synchronization | Reset now clears the daily water-goal habit flag | Input remains milliliters. |
| Health calculators | Working | BMI, BMR, calorie needs, ideal-weight range, water estimate, and invalid-value handling | Existing pure calculations retained | Calculator entry remains metric; converted display support is limited. |
| Body measurement tracker | Fixed | Positive weight, non-negative values, body-fat range, free limit, Premium history, and list rendering | Added visible message when free history is full | Free users retain three local entries. |
| Progress photos | Working | Local picker, nullable URI, before/after view, removal, and free limit | No additional change required | URI previews are local and may depend on Android provider availability. |
| Fitness test, score, habits, recovery, and reports | Working | Validation, empty states, local persistence, score bounds, weekly report, and monthly report | No additional change required | Recovery guidance is general wellness content only. |
| Quick workout mode | Fixed | Empty exercise library, invalid duration input, filters, local generation, preview media, and completion guard | Engine now clamps duration to 5-15 minutes; empty library cannot be completed | UI offers 5, 10, and 15 minutes. |
| Profile and edit profile | Fixed | Profile state, edit validation, reset confirmation, dark mode, and feature links | Added safe missing-profile state and friendly edit validation | Delete-all now routes into onboarding after deletion completes. |
| Settings and language switching | Fixed | DataStore language code, pre-Compose locale wrapper, immediate activity recreation, locale fallback, RTL flag, independent country/currency/unit settings, theme toggle, feature links, and key parity | Normalized saved settings, added a save acknowledgement, and expanded Settings links | Supports `en`, `hi`, `es`, `fr`, and `ar`. |
| Delete all local data | Fixed | Confirmation flow, Room clearing order, callback timing, and navigation after delete | Waits for repository deletion before routing to onboarding | Prevents a blank Profile screen after data deletion. |
| Privacy, terms, disclaimer, consent, and data safety | Working | Registered routes, packaged copy, back buttons, disclaimer acknowledgement, and delete/export links | No additional change required | Export remains Coming Soon. |
| AI previews, PDF export, trainer mode, and form check | Coming Soon | Premium routing, safe local placeholder pages, disabled external operations, and back buttons | No additional change required | No external AI, camera, or PDF API is called. |
| Notifications, billing, ads, cloud, and Firebase | Coming Soon | Placeholder architecture and absence of unsafe permission or service calls | No additional change required | Production integrations require consent and release configuration. |
| Share app and workout share card | Fixed | Nullable share summary and Android share intent paths | Wrapped share launches safely so missing handlers do not crash the app | Rating remains a disabled Play Store placeholder. |
| Localization verifier | Fixed | XML parsing, comments, escaped content, duplicate detection, missing keys, and extra-key reporting across five packs | Added `scripts/verify_strings.py` | All packs currently contain 536 matching keys. |
| Direct Compose string scan | Fixed | Direct `Text`, Snackbar, Toast, and top-app-bar literal patterns | Added `scripts/scan_hardcoded_strings.py` | Current scan reports 0 likely direct hardcoded UI strings. |
| JVM QA suite | Fixed | Locale fallback, region mapping, currency formatting, regional pricing, calculators, unit conversions, streaks, Premium locks, recommendations, validation, and exercise media | Expanded pure local tests to 46 passing cases | Device interaction remains in the manual checklist. |
| UI smoke-test hooks | Fixed | Onboarding, bottom tabs, Settings selectors, Premium unlock, workout start, and workout finish | Added Compose test tags | Instrumented tests are not configured yet; manual emulator checks remain required. |
| Verification script | Fixed | Java check, string parity, hardcoded scan, clean, required unit-test failure behavior, APK build, and APK existence check | Added localization QA gates | Run `./scripts/verify_app.sh`. |
| GitHub Actions APK workflow | Fixed | Trigger branches, manual dispatch, Java 17, Android SDK, Python string QA, combined test/build command, and artifact upload | CI now verifies strings before Gradle validation | Artifact name is `fitness-workout-debug-apk`. |

## Navigation Audit

Every visible route referenced by Home, Profile, Settings, workout cards, Premium links, legal links, and bottom navigation has a matching `NavHost` registration. Dynamic plan and exercise routes parse IDs with `toIntOrNull()` and render safe loading or unavailable states instead of throwing navigation exceptions.

Incomplete integrations route to stable placeholder pages or disabled actions. The reusable `ComingSoonScreen` accepts a title, description, back action, and optional Premium-lock indicator.

## Database Audit

- Room entities and DAO queries compile through KSP.
- Database schema version changes use `fallbackToDestructiveMigration()` for development stability.
- Sample workouts, community cards, and announcements seed only when their corresponding tables are empty.
- Workout completion persists history, a share card, habits, and achievements.
- DataStore persists the selected locale code independently of structured Room state.

## Language Switching Audit

| Scenario | Result | Evidence |
|---|---|---|
| Default language is English | Working | DataStore falls back to `en`; JVM fallback test passes. |
| Change language to Hindi | Working | Settings stores `hi`, shows a restart toast, and recreates the activity. |
| Home, Profile, and Settings update | Working | Major labels use localized resources; source-level code-path audit completed. |
| Restart keeps Hindi | Working | `MainActivity.attachBaseContext()` reads DataStore before Compose loads. |
| Change back to English | Working | Settings persists `en`; JVM name/code mapping test passes. |
| Arabic RTL | Working | `ar` mapping test passes, layout direction updates, and manifest sets `android:supportsRtl="true"`. |
| Missing translations | Fixed | English, Hindi, Spanish, French, and Arabic folders contain matching keys. Secondary untranslated phrases safely fall back to English copy. |

## Country, Currency, And Unit Audit

| Scenario | Result | Evidence |
|---|---|---|
| United States default | Working | `US` maps to `USD` and Imperial units. |
| India default | Working | `IN` maps to `INR` and Metric units. |
| European default | Working | Germany, France, and Spain map to `EUR` and Metric units. |
| Additional regions | Working | Canada, Australia, Brazil, UAE, and Saudi Arabia have explicit currency and unit defaults. |
| Independent overrides | Working | Settings persists selected country, currency, and units as separate fields. |
| Currency fallback | Working | Unsupported codes fall back to `USD`; JVM test passes. |
| Regional Premium pricing | Working | `PremiumPricingRepository` reads numeric mock regional amounts and the UI formats them with `CurrencyFormatter`. |
| Invalid direct unit mapping | Working | Unknown country values fall back to Metric units; JVM test passes. |

## Automated Verification

Run:

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew assembleDebug
./scripts/verify_app.sh
```

`verify_app.sh` also runs:

```bash
python3 scripts/verify_strings.py
python3 scripts/scan_hardcoded_strings.py
```

Expected APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Known Limitations

- Final touch testing on an installed APK still requires an Android phone or emulator; use `docs/MANUAL_QA_CHECKLIST.md`.
- Instrumented Compose smoke tests are not configured in this MVP. Stable test tags are present for future UI automation.
- The generated APK is a debug build. Play Store submission requires a signed release bundle.
- Some secondary UI content remains English when translated copy is unavailable.
- Profile and calculator entry fields remain metric; selected units affect supported displays.
- Progress photos store local picker URIs and are not cloud backed up.
- Notification scheduling and external service integrations remain intentionally disabled.
