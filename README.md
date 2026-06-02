# Fitness and Workout App

A modern Android fitness app built with Kotlin and Jetpack Compose. It helps users choose structured workout plans, complete guided exercise sessions, track local progress, manage their profile, and build consistency with a 30-day workout challenge.

## Features

- Personalized onboarding for name, age, weight, height, and fitness goal
- Home dashboard with greeting, motivational quote, workout summary, calories, weekly progress, and quick actions
- Beginner, intermediate, and advanced workout plans for full body, chest, legs, arms, abs, and cardio sessions
- Exercise details with sets, reps or duration, rest time, instructions, completion controls, and timers
- Room-backed progress history with completed workouts, calories, weekly count, and streak tracking
- Editable profile with BMI calculation, goal updates, and progress reset
- 30-day workout challenge with gradually increasing difficulty
- Locally mocked Premium plans with free-vs-premium locks and ad hiding
- Water tracking, health calculators, diet guidance, reminders, and local custom workout plans
- Premium challenges for fat loss, muscle gain, and abs plus a free beginner challenge
- Exercise visual and video placeholders with safety tips, equipment, muscle groups, and common mistakes
- Dark mode preference, reusable ad placeholder, and advanced progress summary
- AI-style local workout generator with goal, level, time, equipment, and body-focus inputs
- Body measurement history for weight, waist, chest, arms, thighs, hips, and body fat
- Local progress photo picker with before-and-after comparison and Premium limits
- Achievement badges, streak rewards, fitness tests, and workout share cards
- Smart reminders for workouts, water, meals, weight check-ins, and progress photos
- Privacy policy, terms, medical disclaimer, feedback, and local delete-all-data screens
- Improved onboarding for goal, level, available time, equipment, age, height, weight, and workout style
- Global onboarding and settings for country, language, metric or imperial units, diet, and workout location
- English, Hindi, Spanish, French, and Arabic resource packs with RTL manifest support
- Regional pricing display models for USD, INR, EUR, GBP, and BRL
- Offline community feed, trainer mode, AI form-check, recovery score, calendar, and specialized routine placeholders
- Desk-worker, Indian fitness, low-impact, no-jumping, and apartment-friendly routine ideas
- Material 3 UI with a responsive scrollable layout, rounded cards, icons, and simple animations
- Quick Workout Mode with 5, 10, and 15-minute locally generated sessions plus no-equipment, low-impact, no-jumping, and office-friendly filters
- Local adaptive workout recommendations based on goal, fitness level, recent completions, skipped sessions, available time, location, and Premium access
- Fitness Score from 0 to 100 with a clear explanation and practical improvement tips
- Premium AI-ready workout coach, meal suggestion, progress analysis, and motivation chat previews powered by local rule-based responses
- Premium progress-report preview for workouts, calories, streak, body measurements, water intake, and fitness score with a future PDF export placeholder
- Warm-up, cool-down, stop-workout warning, exercise safety tips, medical acknowledgement, and pregnancy or injury guidance
- Splash screen, launcher icon placeholder, About, Contact Support, Rate App, Share App, Data Safety, and version-information sections
- In-app workout and diet category hub for home training, beginner plans, HIIT, yoga, walking, office fitness, and Indian or vegetarian meal guidance
- Offline app announcements, promo cards, featured workout cards, and new-challenge cards ready for a future Remote Config connection
- Smart daily habit checklist for workouts, water, steps placeholder, meals, sleep, and stretching with a Home completion percentage
- Weekly and monthly fitness reports with timeline, missed workout summary, best-week context, and suggested improvements
- Favorite workouts, recently viewed sessions, and a continue-last-workout Home card
- Premium conversion page with a 7-day trial banner, yearly savings, lifetime offer, comparison table, FAQ, testimonial, restore, terms, and privacy placeholders
- GitHub Actions debug build that uploads the APK as `fitness-workout-debug-apk`

## Tech Stack

- Kotlin
- Jetpack Compose and Material 3
- MVVM architecture
- Room database
- DataStore preferences
- Repository pattern
- Navigation Compose
- Kotlin Coroutines and Flow
- Gradle Kotlin DSL
- Minimum SDK 26

## Project Structure

```text
app/src/main/java/com/example/fitnessworkout/
├── data/
│   ├── local/
│   └── model/
├── repository/
├── ui/
│   ├── components/
│   ├── navigation/
│   ├── screens/
│   └── theme/
├── viewmodel/
└── MainActivity.kt
```

## How to Run

1. Open the project root in Android Studio.
2. Allow Gradle sync to finish. Android Studio can install the required Android SDK 35 platform if it is not already present.
3. Create or select an Android emulator running API 26 or newer.
4. Run the `app` configuration.

Workout plans are inserted into the local Room database the first time the app launches.

## Global Support

- Supported resource packs: English, Hindi, Spanish, French, and Arabic
- Supported units: metric and imperial
- Add translations in `app/src/main/res/values-<locale>/strings.xml`
- Regional prices are display models only. Replace them with Google Play Billing product details before release.

## Privacy and Legal

Health records remain offline-first in Room. Settings include analytics and cloud-sync consent placeholders. The Profile and Settings screens link to privacy, terms, medical disclaimer, export, feedback, bug report, and delete-local-data controls.

## Build in Codespaces

Install Android SDK 35 and run:

```bash
./gradlew clean assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Download APK from GitHub Actions

1. Open the repository on GitHub and select the **Actions** tab.
2. Open a successful **Android Debug APK** workflow run.
3. Download the `fitness-workout-debug-apk` artifact.
4. Unzip the artifact to get `app-debug.apk`.

## Install APK on an Android Phone

1. Transfer `app-debug.apk` to the Android phone.
2. Open the APK from the phone's Files app.
3. Allow installation from that source if Android asks for permission.
4. Tap **Install**.

The APK is a debug build for testing. Use a signed release bundle before Play Store submission.

## Future Integrations

- Google Play Billing and AdMob
- Firebase Authentication, Google Sign-In, Analytics, Crashlytics, Firestore backup, Storage for video, Remote Config, and push notifications
- Health Connect, Google Fit, and Wear OS
- Real exercise videos and MediaPipe or ML Kit form analysis
- Android `PdfDocument` export for Premium progress reports

## Future Monetization Ideas

- Google Play Billing subscriptions for Premium
- Personalized AI-assisted workout plans
- Advanced charts and goal insights
- Diet plans and grocery lists
- Optional ad network integration using the existing ad placeholder component
- Wearable and health platform integrations
