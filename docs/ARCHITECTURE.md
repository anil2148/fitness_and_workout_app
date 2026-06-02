# Fitness and Workout App — Architecture Documentation

This document explains the expected technical architecture of the `fitness_and_workout_app` Android project for future developers.

Use this document together with:

- `docs/FEATURES.md`
- `docs/FEATURE_AUDIT.md`
- `docs/MANUAL_QA_CHECKLIST.md`
- `README.md`

---

## 1. Architecture Goals

The app should be designed as a stable, offline-first Android fitness MVP that can later grow into a monetized global fitness product.

Primary architecture goals:

- Keep core workout flow stable.
- Keep app buildable at all times.
- Use local persistence first.
- Avoid broken advanced features in production UI.
- Prefer safe placeholders for incomplete integrations.
- Keep business logic out of Compose UI where practical.
- Make calculations and rules testable.
- Support future integrations without requiring them in MVP.

---

## 2. Recommended Tech Stack

Expected stack:

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Room
- DataStore
- Kotlin Coroutines
- Flow / StateFlow
- Navigation Compose
- Gradle Kotlin DSL
- Java 17 for builds

Optional future integrations:

- Google Play Billing
- AdMob
- Firebase Auth
- Firestore
- Firebase Storage
- Firebase Analytics
- Crashlytics
- Remote Config
- Health Connect
- WorkManager
- Media3 / ExoPlayer
- ML Kit / MediaPipe

---

## 3. Recommended Package Structure

Recommended source structure:

```text
app/src/main/java/<package>/
  data/
    local/
      AppDatabase.kt
      dao/
      datastore/
      converters/
    model/
    repository/
  domain/
    calculator/
    rules/
    validation/
  ui/
    components/
    navigation/
    screens/
    theme/
  viewmodel/
  utils/
```

If the current project uses a slightly different structure, avoid unnecessary large refactors. Stabilize first, then clean up gradually.

---

## 4. Layer Responsibilities

### 4.1 UI Layer

Contains Jetpack Compose screens and reusable components.

Responsibilities:

- Display state from ViewModels.
- Send user events to ViewModels.
- Show loading, empty, and error states.
- Navigate using Navigation Compose.
- Avoid direct database calls.
- Avoid heavy business logic.

Examples:

- `HomeScreen`
- `WorkoutsScreen`
- `WorkoutDetailScreen`
- `WorkoutPlayerScreen`
- `ProgressScreen`
- `PremiumScreen`
- `SettingsScreen`
- `ComingSoonScreen`

### 4.2 ViewModel Layer

Coordinates UI state and business actions.

Responsibilities:

- Expose immutable UI state.
- Collect Flow from repositories.
- Validate user actions.
- Call repositories for data operations.
- Handle loading and error states.
- Keep UI stable with safe default values.

ViewModels should avoid:

- Direct resource lookup where possible.
- Direct navigation object dependencies.
- Unhandled coroutine exceptions.
- Unsafe list indexing.

### 4.3 Repository Layer

Abstracts local data sources.

Responsibilities:

- Coordinate DAOs and DataStore.
- Provide Flow or suspend APIs.
- Hide persistence implementation details.
- Seed sample data when needed.
- Keep sample data loading idempotent.

### 4.4 Data Layer

Contains Room database, DAOs, entities, DataStore, and converters.

Responsibilities:

- Persist user profile.
- Persist workouts, exercises, completed workouts, settings, water logs, and other local data.
- Provide reliable queries.
- Avoid crashes on database startup.

### 4.5 Domain / Utility Layer

Contains pure logic.

Examples:

- BMI calculation
- BMR calculation
- Water goal calculation
- Unit conversion
- Streak calculation
- Premium lock logic
- Workout recommendation rules
- Input validation

These should be easy to unit test.

---

## 5. Core App Flow

### 5.1 Launch Flow

Expected flow:

```text
MainActivity
  -> Load app settings/profile state
  -> If no profile exists: Onboarding
  -> If profile exists: Home
```

Rules:

- Never show infinite loading.
- Never crash if profile is missing.
- Use safe default UI state while loading.

### 5.2 Onboarding Flow

```text
OnboardingScreen
  -> User enters required profile details
  -> Validate input
  -> Save UserProfile
  -> Save selected settings if available
  -> Navigate to Home
```

Required validation:

- Name required
- Age must be valid positive number
- Height must be valid positive number
- Weight must be valid positive number
- Required goal/level/unit selections must be present

### 5.3 Workout Flow

```text
WorkoutsScreen
  -> WorkoutDetailScreen(workoutId)
  -> WorkoutPlayerScreen(workoutId)
  -> WorkoutSummary / Finish
  -> Save CompletedWorkout
  -> ProgressScreen reflects updated data
```

Rules:

- Empty workout list should trigger sample preload or show empty state.
- Missing workout ID should show friendly error.
- Empty exercise list should not crash.
- Timer should handle first, next, last, and finish safely.

### 5.4 Premium Flow

```text
Free user taps locked card
  -> PremiumScreen
  -> Mock Unlock Premium
  -> Save isPremiumUser=true
  -> User can access locked content
```

MVP rules:

- Premium is local/mock only.
- Do not implement real billing until the core app is stable.
- Add TODO comments for Google Play Billing.

### 5.5 Settings and Language Flow

```text
SettingsScreen
  -> Select language
  -> Save selectedLanguageCode
  -> Apply locale
  -> Recompose or recreate activity safely
  -> Persist after restart
```

Rules:

- Language change must not be only visual dropdown state.
- It must apply to app UI.
- English fallback must work.
- Arabic must not crash.

---

## 6. Navigation Architecture

Use Navigation Compose with strongly defined route constants.

Recommended route object pattern:

```kotlin
object Routes {
    const val HOME = "home"
    const val ONBOARDING = "onboarding"
    const val WORKOUTS = "workouts"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"
    const val WORKOUT_PLAYER = "workout_player/{workoutId}"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val PREMIUM = "premium"
    const val SETTINGS = "settings"
    const val COMING_SOON = "coming_soon/{title}"
}
```

Rules:

- Every referenced route must exist in the NavHost.
- Every visible button must navigate safely.
- Incomplete advanced features should navigate to `ComingSoonScreen`.
- Avoid hardcoded route strings scattered across UI files.
- Validate nav arguments.

---

## 7. Coming Soon Strategy

The app includes many advanced planned features. Incomplete features must not crash.

Use a reusable `ComingSoonScreen` for incomplete features.

Expected parameters:

- Title
- Description
- Premium locked flag if needed
- Back action

Use Coming Soon for incomplete:

- Real AI coach
- Real PDF export
- Real AdMob
- Real Play Billing
- Real exercise videos
- Real cloud backup
- Health Connect
- Wear OS
- ML pose detection
- Community backend
- Trainer backend

---

## 8. Data Persistence Architecture

### 8.1 Room Database

Room should handle structured local data.

Likely entities:

- `UserProfile`
- `Exercise`
- `WorkoutPlan`
- `CompletedWorkout`
- `Challenge`
- `ChallengeDay`
- `WaterLog`
- `BodyMeasurement`
- `HealthMetric`
- `FavoriteWorkout`
- `RecentlyViewedWorkout`

Development rule:

- `fallbackToDestructiveMigration()` may be used during MVP stabilization.
- Production releases should use proper migrations.

### 8.2 DataStore

DataStore is recommended for lightweight settings.

Likely settings:

- Premium mock status
- Selected language code
- Unit system
- Theme preference
- Reminder settings
- Disclaimer acknowledgement

### 8.3 Sample Data Seeding

Sample data should load only once.

Recommended approach:

- Store a flag in DataStore such as `sampleDataLoaded=true`.
- On first launch, insert default exercises and workouts.
- Avoid duplicate sample data on every launch.

---

## 9. Important Models

### 9.1 UserProfile

Recommended fields:

- id
- name
- age
- height
- weight
- fitnessGoal
- fitnessLevel
- unitSystem
- country
- languageCode
- workoutLocation
- createdAt
- updatedAt

### 9.2 Exercise

Recommended fields:

- id
- name
- description
- muscleGroup
- difficulty
- equipment
- imageResName or imageKey
- videoUrl or localVideoName
- sets
- reps
- durationSeconds
- restSeconds
- caloriesPerMinute
- instructions
- safetyTips
- commonMistakes
- isPremium
- isLowImpact
- isNoJumping
- isKneeFriendly
- isBackFriendly

### 9.3 WorkoutPlan

Recommended fields:

- id
- title
- description
- difficulty
- category
- durationMinutes
- caloriesEstimate
- isPremium
- exerciseIds or relationship table

### 9.4 CompletedWorkout

Recommended fields:

- id
- workoutPlanId
- workoutTitle
- completedAt
- durationMinutes
- caloriesBurned
- exercisesCompleted

### 9.5 AppSettings

Recommended fields / DataStore keys:

- selectedLanguageCode
- unitSystem
- themeMode
- isPremiumUser
- hasAcceptedMedicalDisclaimer
- sampleDataLoaded

---

## 10. UI State Guidelines

Each screen should have a UI state object where practical.

Recommended pattern:

```kotlin
data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val todayWorkout: WorkoutPlan? = null,
    val streak: Int = 0,
    val caloriesBurned: Int = 0,
    val errorMessage: String? = null
)
```

Rules:

- Default state must be safe.
- Empty lists must be supported.
- Errors should be shown in UI.
- Avoid `!!` in UI and ViewModels.
- Avoid direct list access without bounds checks.

---

## 11. Calculation Utilities

Keep calculations outside Compose screens.

Recommended utility classes:

- `BmiCalculator`
- `BmrCalculator`
- `WaterIntakeCalculator`
- `UnitConverter`
- `StreakCalculator`
- `PremiumAccessEvaluator`
- `WorkoutRecommendationEngine`

These should have unit tests.

---

## 12. Localization Architecture

### 12.1 String Resources

User-facing text should be in string resources.

Resource folders:

```text
res/values/strings.xml
res/values-hi/strings.xml
res/values-es/strings.xml
res/values-fr/strings.xml
res/values-ar/strings.xml
```

Rules:

- Every key in English should exist in each translation file.
- If translation is incomplete, use English fallback text.
- Major screens should use `stringResource(...)`.

### 12.2 Runtime Language Switching

Recommended:

- Store selected language code in DataStore.
- Apply selected language before Compose UI loads.
- Recreate activity or trigger recomposition after change.
- Support `en`, `hi`, `es`, `fr`, `ar`.

If using AppCompat:

```kotlin
AppCompatDelegate.setApplicationLocales(
    LocaleListCompat.forLanguageTags(languageCode)
)
```

If AppCompat is not configured, use a safe locale helper/context wrapper.

---

## 13. Unit System Architecture

Supported unit systems:

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

Rules:

- Store selected unit system.
- Use utility conversion functions.
- Show clear labels in UI.
- Calculators must respect the selected unit system or clearly show which unit is required.
- Avoid crashes from bad ft/in parsing.

---

## 14. Exercise Media Architecture

### 14.1 Images

Use drawable assets or vector placeholders.

Rules:

- Every exercise should map to an image key.
- Missing image uses `exercise_placeholder`.
- Never crash due to missing drawable.
- Do not download copyrighted images.

### 14.2 Videos

MVP uses placeholder UI.

Rules:

- Do not require real MP4 files.
- Show `ExerciseVideoPlaceholder`.
- Future real videos should be streamed from CDN/Firebase Storage or stored carefully in `res/raw` for small assets.

---

## 15. Premium Architecture

Current MVP:

- Local premium status only.
- Mock unlock button.
- Premium lock UI.

Future production:

- Google Play Billing Library
- Subscription plans
- Purchase restore
- Receipt validation
- Regional pricing

Recommended abstraction:

```kotlin
interface PremiumRepository {
    val isPremium: Flow<Boolean>
    suspend fun mockUnlockPremium()
    suspend fun resetPremium()
}
```

Future billing integration can replace mock implementation behind a similar interface.

---

## 16. Build Architecture

### 16.1 Local/Codespaces Build

Build command:

```bash
./gradlew clean assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### 16.2 Java Requirement

Android Gradle Plugin requires Java 17.

Codespaces setup may require:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

### 16.3 Android SDK

Codespaces may require Android SDK setup and `local.properties`:

```text
sdk.dir=/home/codespace/android-sdk
```

Do not commit `local.properties`.

---

## 17. GitHub Actions Architecture

Workflow file:

```text
.github/workflows/android-build.yml
```

Expected behavior:

- Checkout code
- Set up JDK 17
- Set up Android SDK
- Run unit tests if stable
- Build debug APK
- Upload APK artifact

Artifact name:

```text
fitness-workout-debug-apk
```

---

## 18. Verification Script

Script path:

```text
scripts/verify_app.sh
```

Expected steps:

- Print Java version
- Clean project
- Run unit tests
- Build debug APK
- Check APK exists

Run:

```bash
./scripts/verify_app.sh
```

---

## 19. Error Handling Rules

All screens should handle:

- Loading state
- Empty state
- Error state
- Missing profile
- Missing workout
- Missing exercise
- Missing media
- Invalid input
- Database errors

Avoid:

- Force unwraps
- Unsafe index access
- Assumptions that database always has data
- Assumptions that resources always exist
- Navigation to undefined routes

---

## 20. Release Readiness Rules

Before Play Store release:

- Core workout flow works.
- Premium mock is replaced or clearly remains non-production.
- Privacy policy URL is available.
- Medical disclaimer is visible.
- Data safety behavior is understood.
- App icon and screenshots are ready.
- Real AdMob/Billing integrations are tested if enabled.
- Crash rate is monitored.
- APK/AAB builds successfully.

---

## 21. Stabilization Priority

If the app becomes unstable, prioritize fixes in this order:

1. Build errors
2. App launch crash
3. Onboarding/profile flow
4. Navigation routes
5. Workout list/detail/player
6. Completed workout persistence
7. Progress screen
8. Premium mock lock/unlock
9. Calculators
10. Language switching
11. Settings/profile
12. Advanced placeholders

---

## 22. Developer Rule of Thumb

A small stable MVP is better than a large unstable app.

When adding or fixing features:

- Keep core flows working.
- Add tests for pure logic.
- Use Coming Soon for risky advanced features.
- Update documentation.
- Run verification script.
- Confirm APK generation.
