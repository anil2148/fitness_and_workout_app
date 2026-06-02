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

## Future Integrations

- Google Play Billing and AdMob
- Firebase Auth, Analytics, Crashlytics, Firestore backup, and video CDN storage
- Health Connect, Google Fit, and Wear OS
- Real exercise videos and MediaPipe or ML Kit form analysis

## Future Monetization Ideas

- Google Play Billing subscriptions for Premium
- Personalized AI-assisted workout plans
- Advanced charts and goal insights
- Diet plans and grocery lists
- Optional ad network integration using the existing ad placeholder component
- Wearable and health platform integrations
