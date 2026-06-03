# Manual Android QA Checklist

Use this checklist after installing `app/build/outputs/apk/debug/app-debug.apk` on an API 26+ Android phone or emulator.

## Fresh Install

- [ ] Clear app data or install fresh.
- [ ] Confirm the splash screen appears briefly.
- [ ] Confirm onboarding opens instead of a blank page.
- [ ] Try empty and invalid age, height, and weight values; confirm a friendly error appears.
- [ ] Enter valid values, accept the disclaimer, finish onboarding, and confirm Home opens.

## Navigation

- [ ] Open Home, Workouts, Challenges, Progress, and Profile from the bottom bar.
- [ ] Confirm the selected bottom tab updates.
- [ ] Confirm top-bar back buttons return safely.
- [ ] Open every Profile and Settings link; confirm a working page or safe Coming Soon page appears.

## Full Page Inventory

- [ ] Core: Splash, Onboarding, Home, Workouts, Workout Detail, Workout Player, Workout Summary share card, Progress, Profile, Settings, and Premium.
- [ ] Workout: Exercise Detail, Challenges, challenge detail flow, Custom Workout Plan, Quick Workout, Recommended for You, Specialized Routines, Indian fitness content, and injury-safe Settings filter.
- [ ] Health: calculators for BMI, BMR, daily calories, and water intake; Water Tracker, Body Measurements, Progress Photos, Fitness Test, Fitness Score, Recovery Score, Daily Habits, Weekly Report, and Monthly Report.
- [ ] Monetization: Premium Plans, Promo Code, Referral Code, Affiliate Store, rewarded-ad placeholder, and disabled Restore Purchase placeholder.
- [ ] Advanced: AI Workout Coach, AI Meal Suggestions, AI Progress Analysis, AI Motivation Chat, PDF preview, Community, Trainer Mode, AI Form Check, Smart Calendar, Reminders, Rate App, Share App, Feedback, Bug Report, What's New, Contact Support, and About.
- [ ] Legal: Privacy Policy, Terms, Medical Disclaimer, Data Consent, Delete My Data, Export My Data, and Data Safety.
- [ ] Confirm every unavailable integration shows Coming Soon copy or a disabled placeholder and every top-bar back button returns safely.

## Action Feedback

- [ ] Save onboarding profile and confirm Home opens after a valid save.
- [ ] Save Settings and confirm a success message appears.
- [ ] Change language, country, currency, and unit system; confirm localized feedback appears.
- [ ] Start a workout and confirm the flow advances safely.
- [ ] Pause and resume the workout player and confirm feedback appears.
- [ ] Finish a workout and confirm progress is saved.
- [ ] Unlock Premium with the mock button and confirm success feedback appears.
- [ ] Add water and confirm progress plus success feedback.
- [ ] Save a measurement and confirm history plus success feedback.
- [ ] Submit feedback and confirm success feedback.
- [ ] Reset/delete data only after a confirmation dialog.
- [ ] Open a Coming Soon feature and confirm it has a friendly title, description, and back button.

## Workout Flow

- [ ] Open Workouts and select a plan.
- [ ] Confirm warm-up appears before workout detail.
- [ ] Confirm exercise illustrations or fallback artwork appear.
- [ ] Open an exercise guide and return safely.
- [ ] Start the guided workout player.
- [ ] Confirm start, pause, resume, next, skip, rest countdown, and finish controls work.
- [ ] Confirm the player remains scrollable on a small screen.
- [ ] Finish a workout and confirm Progress updates.

## Premium

- [ ] Open a locked plan or Premium-only feature as a free user.
- [ ] Confirm Premium opens safely.
- [ ] Tap the mock Premium unlock.
- [ ] Restart the app and confirm Premium access persists.
- [ ] Confirm Premium-only local previews open and the ad placeholder is hidden.
- [ ] Open Promo Code, Referral Code, and Affiliate Store; confirm each opens a safe Coming Soon page.
- [ ] Confirm Restore Purchase remains visibly disabled and does not pretend to complete a purchase.

## Calculators And Trackers

- [ ] Test BMI, BMR, daily calorie, water, and ideal-weight estimates with valid values.
- [ ] Try empty, zero, negative, and non-numeric calculator values; confirm errors appear.
- [ ] Add and reset water; confirm the daily habit state stays consistent.
- [ ] Add body measurements and confirm the free limit shows a clear message.
- [ ] Pick and remove a local progress photo without a permission crash.
- [ ] Save a fitness test and recovery score.

## Language

- [ ] Change English to Hindi in Global Settings and confirm visible labels refresh.
- [ ] Confirm Home, Profile, Settings, Premium, workout detail, workout player, calculator, and tracker labels refresh.
- [ ] Restart the app and confirm Hindi remains selected.
- [ ] Change Hindi back to English and restart.
- [ ] Change to Arabic and confirm the app does not crash.
- [ ] Confirm Arabic layout direction updates where supported.
- [ ] Confirm any untranslated secondary content safely falls back to English.

## Country, Currency, And Units

- [ ] Select India and save settings; confirm the default currency changes to `INR` and units change to Metric.
- [ ] Open Premium and confirm mock amounts use the rupee symbol and India mock amounts.
- [ ] Select United States and save settings; confirm `USD` and Imperial defaults.
- [ ] Select France and save settings; confirm `EUR` and Metric defaults.
- [ ] Override the currency independently and confirm Premium pricing updates without changing the app language.
- [ ] Override the unit system independently and confirm supported weight, height, and water displays update.

## Settings And Legal

- [ ] Toggle dark mode.
- [ ] Change metric and imperial display settings.
- [ ] Open Privacy, Terms, Medical Disclaimer, Data Consent, and Data Safety.
- [ ] Open Export and confirm a safe Coming Soon page.
- [ ] Delete all local data, confirm the warning, and verify onboarding opens after deletion.

## Media And Sharing

- [ ] Confirm exercise images never show a blank media area.
- [ ] Confirm video cards safely show the offline placeholder.
- [ ] Confirm locked video cards route to Premium.
- [ ] Complete a workout and open the share card.
- [ ] Try Share App and workout sharing.

## APK

- [ ] Run `python3 scripts/verify_strings.py`.
- [ ] Run `python3 scripts/scan_hardcoded_strings.py`.
- [ ] Run `./scripts/verify_app.sh`.
- [ ] Confirm `app/build/outputs/apk/debug/app-debug.apk` exists.
- [ ] Install the APK on an Android phone or emulator.
