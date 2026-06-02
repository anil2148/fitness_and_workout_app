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
- Material 3 UI with a responsive scrollable layout, rounded cards, icons, and simple animations

## Tech Stack

- Kotlin
- Jetpack Compose and Material 3
- MVVM architecture
- Room database
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

## Future Monetization Ideas

- Google Play Billing subscriptions for Premium
- Personalized AI-assisted workout plans
- Advanced charts and goal insights
- Diet plans and grocery lists
- Optional ad network integration using the existing ad placeholder component
- Wearable and health platform integrations
