# Fitness and Workout App — Feature Documentation

This document is for future developers working on the `fitness_and_workout_app` Android project. It describes the intended app features, current MVP scope, premium features, placeholders, technical architecture, data models, testing expectations, and future roadmap.

> Important: This documentation describes the product direction and expected behavior. Some advanced features may currently be implemented as placeholders or `Coming Soon` screens until they are fully stabilized.

---

## 1. App Overview

The Fitness and Workout App is a health and fitness Android application designed to help users:

- Discover workout plans
- Start guided workouts
- Track completed workouts
- Monitor progress, streaks, calories, and water intake
- Use calculators such as BMI, BMR, calorie needs, and water intake
- Access premium workout plans, challenges, analytics, and guidance
- Use the app globally with language and unit-system support

The app follows an offline-first MVP approach using local storage. Advanced cloud, AI, ads, billing, video streaming, and health integrations are planned for future releases.

---

## 2. Core MVP Features

These features should be prioritized and kept stable before adding more advanced functionality.

### 2.1 App Launch Flow

Expected behavior:

- App launches without crashing.
- Splash/loading screen appears briefly if implemented.
- If no user profile exists, app navigates to Onboarding.
- If a user profile exists, app navigates directly to Home.
- No infinite loading or blank screen.

### 2.2 Onboarding

The onboarding flow collects initial user data.

Expected fields:

- Name
- Age
- Height
- Weight
- Fitness goal
- Fitness level
- Unit system
- Country/region
- Preferred language
- Workout location
- Medical disclaimer acknowledgement

Validation requirements:

- Required fields cannot be empty.
- Numeric values must be valid.
- Negative or zero values should show friendly errors where invalid.
- On successful save, user profile is persisted locally.

### 2.3 Home Dashboard

The Home screen should summarize the user's fitness status.

Expected content:

- Greeting with user name
- Today's workout card or empty state
- Current streak
- Weekly progress
- Calories burned
- Water progress if available
- Premium badge if user is premium
- Quick actions such as Start Workout, Workouts, Progress, Premium, and Settings

Stability requirements:

- Must not crash when profile or workout data is missing.
- Must show empty/default states safely.

### 2.4 Bottom Navigation

Primary tabs:

- Home
- Workouts
- Challenges
- Progress
- Profile

Expected behavior:

- Each tab opens safely.
- Selected tab state updates correctly.
- Android back button works reasonably.
- No tab should navigate to a missing route.

### 2.5 Workouts List

The Workouts screen displays available workout plans.

Expected behavior:

- Shows sample workout plans if database is empty.
- Workout cards show title, difficulty, duration, category, and premium lock if applicable.
- Search/filter should not crash if implemented.

Recommended sample plans:

- Beginner Full Body
- Beginner Fat Loss
- Core Strength
- Cardio Blast
- Upper Body Strength
- Lower Body / Leg Day
- Intermediate Muscle Builder
- Advanced HIIT

### 2.6 Workout Detail

The Workout Detail screen shows exercises inside a selected workout.

Expected content:

- Workout title
- Description
- Difficulty
- Duration
- Exercise list
- Exercise images or fallback placeholder
- Sets/reps/duration/rest time
- Start Workout button

Stability requirements:

- Missing exercise image must use fallback.
- Empty exercise list must show empty state rather than crash.

### 2.7 Workout Player

The Workout Player is the guided workout session screen.

Expected controls:

- Start
- Pause
- Resume
- Next
- Skip if available
- Finish

Expected behavior:

- Shows current exercise image or fallback.
- Shows video placeholder if real video is missing.
- Timer does not crash on first, last, or empty exercise list.
- Finish saves a `CompletedWorkout` record locally.
- Progress screen updates after workout completion.

### 2.8 Progress Screen

The Progress screen shows user activity and history.

Expected content:

- Total completed workouts
- Calories burned
- Current streak
- Weekly progress
- Completed workout history
- Empty state when there is no data

Stability requirements:

- Must not crash with an empty database.
- Date parsing and chart rendering must handle missing/empty data safely.

### 2.9 Profile Screen

The Profile screen shows and manages user information.

Expected content:

- User name
- Age
- Height
- Weight
- Fitness goal
- Fitness level
- Unit system
- Premium status
- Edit profile option or safe placeholder
- Settings navigation
- Reset/delete data option with confirmation

---

## 3. Premium Features

Premium functionality should be gated for free users. Free users should see locked cards that navigate to the Premium screen.

### 3.1 Premium Screen

Expected content:

- Monthly plan: `$2.99/month`
- Yearly plan: `$19.99/year`
- Lifetime plan: `$29.99`
- 7-day free trial banner
- Best Value label for yearly plan
- Free vs Premium comparison
- FAQ section
- Restore purchase placeholder
- Terms and privacy links

Current MVP behavior:

- Premium unlock is mocked locally.
- `isPremiumUser` should persist locally.
- Real Google Play Billing is not implemented yet.

Future integration:

- Google Play Billing Library
- Regional pricing from Play Console
- Subscription status validation

### 3.2 Premium Locked Content

Premium locked areas may include:

- Intermediate and advanced workout plans
- 30-day challenges
- Custom workout plan builder
- Advanced analytics
- Diet guidance
- Exercise video guides
- AI coach placeholders
- PDF progress report
- Trainer mode
- AI form check placeholder
- Unlimited progress photos

Expected behavior:

- Free user taps locked content → Premium screen opens.
- Mock premium unlock → user can access locked content.
- Premium state persists after restart.

---

## 4. Exercise Media

### 4.1 Exercise Images

Every exercise should display either a specific illustration or a fallback image.

Recommended drawable names:

- `exercise_push_ups.xml`
- `exercise_squats.xml`
- `exercise_lunges.xml`
- `exercise_plank.xml`
- `exercise_jumping_jacks.xml`
- `exercise_burpees.xml`
- `exercise_mountain_climbers.xml`
- `exercise_crunches.xml`
- `exercise_bicycle_crunches.xml`
- `exercise_high_knees.xml`
- `exercise_glute_bridge.xml`
- `exercise_wall_sit.xml`
- `exercise_shoulder_taps.xml`
- `exercise_tricep_dips.xml`
- `exercise_superman.xml`
- `exercise_side_plank.xml`
- `exercise_russian_twists.xml`
- `exercise_step_ups.xml`
- `exercise_calf_raises.xml`
- `exercise_arm_circles.xml`
- `exercise_placeholder.xml`

Rules:

- Do not use copyrighted images without permission.
- Use owned, licensed, royalty-free, or AI-generated media with valid usage rights.
- Missing image resources must never crash the app.

### 4.2 Video Placeholders

Real videos are not required for MVP.

Expected placeholder behavior:

- Show exercise image thumbnail.
- Show play icon.
- Show text: `Video guide coming soon`.
- Do not require `.mp4` files.
- Do not crash if video path is missing.

Future video options:

- Add local MP4 files under `app/src/main/res/raw` for small sets only.
- Prefer CDN/Firebase Storage for production video streaming.
- Add offline download for premium users later.

---

## 5. Calculators

The app may include these calculators:

- BMI calculator
- BMR calculator
- Daily calorie needs calculator
- Water intake calculator
- Ideal weight range estimate

Validation rules:

- Empty input shows error.
- Invalid number shows error.
- Negative values show error.
- Zero height/weight should be rejected where invalid.
- No `NumberFormatException` should reach the user.

Recommended implementation:

- Keep calculation logic in utility classes.
- Add local unit tests for calculation utilities.
- UI should only collect input and display results.

---

## 6. Tracking Features

### 6.1 Water Tracker

Expected features:

- Daily water goal
- Add 250 ml / equivalent button
- Add custom amount
- Progress indicator
- Daily reset logic if implemented
- Local persistence

### 6.2 Body Measurement Tracker

Expected fields:

- Weight
- Waist
- Chest
- Arms
- Thighs
- Hips
- Body fat percentage

If incomplete, route to `ComingSoonScreen`.

### 6.3 Progress Photos

Expected MVP behavior:

- If image picker is not fully implemented, show `ComingSoonScreen`.
- Do not request broken permissions.
- Free users may be limited to 3 photos.
- Premium users may have unlimited photos in future.

### 6.4 Fitness Score

Expected behavior:

- Calculate safely even with missing data.
- If not enough data exists, show an explanation and empty state.

Possible score factors:

- Workout consistency
- Current streak
- Completed workouts
- Water intake
- Fitness test results
- Body measurement progress

---

## 7. Challenges

Expected challenge examples:

- 30-Day Fat Loss Challenge
- 30-Day Muscle Gain Challenge
- 30-Day Abs Challenge
- 30-Day Beginner Fitness Challenge

Expected behavior:

- Challenge list opens safely.
- Premium challenges are locked for free users.
- Challenge detail either works or shows `ComingSoonScreen`.
- No broken navigation routes.

---

## 8. Diet and Meal Guidance

Diet guidance is premium-oriented and should include a health disclaimer.

Expected categories:

- Weight Loss
- Muscle Gain
- Balanced
- Vegetarian
- Indian diet option
- Global high protein
- Mediterranean
- Vegan
- Keto
- Low carb
- Halal-friendly
- Gluten-free
- Dairy-free
- Budget meals

Disclaimer:

> This app provides general fitness and nutrition information only and is not medical advice.

---

## 9. Global User Support

### 9.1 Language Support

Supported language codes:

- English: `en`
- Hindi: `hi`
- Spanish: `es`
- French: `fr`
- Arabic: `ar`

Resource folders:

- `app/src/main/res/values/strings.xml`
- `app/src/main/res/values-hi/strings.xml`
- `app/src/main/res/values-es/strings.xml`
- `app/src/main/res/values-fr/strings.xml`
- `app/src/main/res/values-ar/strings.xml`

Expected behavior:

- Selected language applies to app UI.
- Selected language persists after restart.
- English fallback works.
- Missing keys must not crash the app.
- Arabic must not crash.

Implementation notes:

- Store selected language code using DataStore or existing settings storage.
- Use `stringResource(R.string.some_key)` for user-facing text.
- Avoid hardcoded UI text for important labels.
- Use `AppCompatDelegate.setApplicationLocales(...)` if AppCompat is configured.
- Otherwise use a safe locale manager/context wrapper.

### 9.2 RTL Support

For Arabic support:

- `AndroidManifest.xml` should include `android:supportsRtl="true"`.
- Prefer `start` and `end` over `left` and `right`.

### 9.3 Unit System

Supported units:

Metric:

- kg
- cm
- km
- ml

Imperial:

- lbs
- ft/in
- miles
- oz

Expected behavior:

- Unit switching should not crash.
- Profile, calculators, water tracker, and measurements should show clear labels.
- If full conversion is incomplete, show clear labels and avoid incorrect crashes.

---

## 10. Privacy and Legal Screens

The following screens should open safely:

- Privacy Policy
- Terms and Conditions
- Medical Disclaimer
- Data Consent
- Delete My Data
- Export My Data placeholder

Required medical disclaimer:

> This app provides general fitness and wellness information only and is not medical advice. Consult a healthcare professional before starting a new fitness program.

Delete data:

- Must ask confirmation before deleting local data.

Export data:

- Can be placeholder for MVP.

---

## 11. Advanced Placeholder Features

The following features may exist as placeholders or premium-locked screens until fully implemented:

- AI Workout Coach
- AI Meal Suggestion
- AI Progress Analysis
- AI Motivation Chat
- PDF Progress Report
- Community
- Trainer Mode
- AI Form Check
- Smart Calendar
- Recovery Score
- Desk Worker Fitness
- Indian Fitness
- Reminder Settings
- Affiliate Store
- Referral Code
- Promo Code
- Rate App
- Share App
- Feedback
- Bug Report

Rules:

- If fully implemented, they must be stable.
- If incomplete, route to `ComingSoonScreen`.
- Do not call real external APIs in MVP.
- Do not request broken camera, notification, or storage permissions.

---

## 12. Recommended Architecture

Suggested package structure:

```text
app/src/main/java/.../
  data/
    local/
    model/
    repository/
  ui/
    components/
    navigation/
    screens/
    theme/
  viewmodel/
  utils/
```

Recommended architecture principles:

- MVVM
- Repository pattern
- Room for local persistence
- DataStore for settings/premium/language if appropriate
- Kotlin Coroutines and Flow
- Material 3 UI
- Offline-first MVP

---

## 13. Important Data Models

Expected or planned models/entities include:

- `UserProfile`
- `Exercise`
- `WorkoutPlan`
- `CompletedWorkout`
- `Challenge`
- `ChallengeDay`
- `WaterLog`
- `HealthMetric`
- `PremiumStatus`
- `ReminderSettings`
- `CustomWorkoutPlan`
- `BodyMeasurement`
- `ProgressPhoto`
- `Achievement`
- `FitnessTestResult`
- `ShareableWorkoutSummary`
- `CommunityPost`
- `TrainerClient`
- `ClientWorkoutAssignment`
- `RecoveryLog`
- `CalendarWorkoutDay`
- `WorkoutFilter`
- `ReferralCode`
- `FeedbackMessage`
- `AffiliateProduct`
- `AppSettings`

Not all models need to be fully active in MVP. Incomplete features should be safely hidden behind placeholders.

---

## 14. Room Database Guidelines

Requirements:

- App must not crash on database startup.
- DAOs and entities must match.
- Queries must compile.
- Sample data should load only once.
- Completed workouts must save and be visible in Progress.

Development stability:

- `fallbackToDestructiveMigration()` may be used during MVP development.
- Proper migrations should be added before production release.

---

## 15. Testing Expectations

### 15.1 Unit Tests

Add or maintain tests for pure logic:

- BMI calculation
- BMR calculation
- Water intake calculation
- Unit conversion
- Streak calculation
- Premium lock logic
- Workout recommendation logic if present
- Input validation logic if practical

Run:

```bash
./gradlew testDebugUnitTest
```

### 15.2 Manual QA Checklist

Maintain:

```text
docs/MANUAL_QA_CHECKLIST.md
```

Minimum manual test flows:

- Fresh install → onboarding → home
- Returning user → home
- Bottom navigation
- Workout list → detail → player → finish
- Progress update after completed workout
- Premium locked feature → Premium screen → mock unlock
- Language change and persistence
- Metric/imperial unit setting
- Calculators with valid and invalid input
- Exercise image fallback
- Video placeholder
- Privacy/legal screens
- Reset/delete data confirmation

---

## 16. Verification Script

Maintain a verification script at:

```text
scripts/verify_app.sh
```

Expected behavior:

- Print Java version
- Clean project
- Run unit tests
- Build debug APK
- Confirm APK exists

Expected APK path:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Run:

```bash
./scripts/verify_app.sh
```

---

## 17. APK Build

Build debug APK:

```bash
./gradlew clean assembleDebug
```

Output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Do not commit generated APK files.

`.gitignore` should include:

```text
local.properties
.gradle/
build/
app/build/
*.apk
```

---

## 18. GitHub Actions

Expected workflow:

```text
.github/workflows/android-build.yml
```

Workflow should:

- Run on push to `main` or `master`
- Run on pull requests to `main` or `master`
- Support `workflow_dispatch`
- Use Java 17
- Set up Android SDK
- Run tests if stable
- Build debug APK
- Upload artifact named `fitness-workout-debug-apk`

---

## 19. Monetization Roadmap

Planned monetization methods:

### Free App + Ads

- Banner ads
- Interstitial ads after workout completion
- Rewarded ads to unlock one premium workout
- Native ads in workout lists

MVP status:

- Ads should remain placeholders until AdMob is integrated.

### Premium Subscription

Possible plans:

- Monthly
- Yearly
- Lifetime

Future implementation:

- Google Play Billing Library
- Regional pricing
- Restore purchase
- Subscription validation

### Paid Challenges

Potential paid products:

- 30-Day Fat Loss Challenge
- 30-Day Muscle Gain Challenge
- Indian Diet + Home Workout Plan
- Desk Worker Fitness Plan

### Affiliate Store

Possible products:

- Yoga mats
- Resistance bands
- Dumbbells
- Protein shakers
- Fitness trackers
- Running shoes

Use only compliant affiliate links and disclose affiliate relationships where required.

---

## 20. Future Integrations

Planned future integrations:

- Google Play Billing
- AdMob
- Firebase Authentication
- Google Sign-In
- Firestore cloud backup
- Firebase Storage for videos
- Firebase Analytics
- Firebase Crashlytics
- Firebase Remote Config
- Push notifications
- Health Connect
- Google Fit if applicable
- Wear OS
- ML Kit / MediaPipe pose detection
- Real AI coach API
- Real PDF export

These should remain TODO/placeholders until intentionally implemented and tested.

---

## 21. Safety and Medical Disclaimer

Because this is a health and fitness app, safety messaging is important.

Recommended warnings:

- Stop if you feel pain, dizziness, chest pain, or severe discomfort.
- Consult a healthcare professional before starting a new workout program.
- The app provides general wellness information only and is not medical advice.
- Pregnancy, injury, and medical condition content should include stronger disclaimers.

---

## 22. Developer Stabilization Rule

When in doubt:

1. Keep the core MVP stable.
2. Fix crashes before adding features.
3. Hide incomplete advanced features behind `ComingSoonScreen`.
4. Do not leave broken buttons.
5. Do not break APK generation.
6. Update this documentation when feature behavior changes.

Core MVP screens that must always work:

- Onboarding
- Home
- Workouts
- Workout Detail
- Workout Player
- Progress
- Profile
- Premium
- Settings

---

## 23. Current Priority

The immediate development priority should be:

1. Stabilize app launch and onboarding.
2. Stabilize workout flow.
3. Stabilize progress tracking.
4. Stabilize premium mock unlock.
5. Fix language switching.
6. Fix calculator crashes.
7. Ensure APK generation.
8. Keep advanced features as safe placeholders until ready.

A small stable app is better than a large app with broken features.

---

## 24. Localization And Regional Formatting

Supported UI locales:

- English (`en`)
- Hindi (`hi`)
- Spanish (`es`)
- French (`fr`)
- Arabic (`ar`)

The language selector persists its choice and refreshes the visible Compose UI. Country, currency, and unit selectors are separate settings. Country selection seeds a sensible currency and unit default, while users may override currency or units afterward.

Premium preview prices are numeric mock values formatted at runtime for `USD`, `INR`, `GBP`, `CAD`, `AUD`, `EUR`, `BRL`, `AED`, and `SAR`. Real production prices must come from Google Play Billing.
