package com.example.fitnessworkout.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: Int,
    val weightKg: Float,
    val heightCm: Float,
    val fitnessGoal: String,
    val darkMode: Boolean = false,
    val fitnessLevel: String = "Beginner",
    val availableMinutes: Int = 20,
    val equipment: String = "No Equipment",
    val workoutStyle: String = "Balanced"
) {
    val bmi: Float get() = if (heightCm > 0) weightKg / ((heightCm / 100) * (heightCm / 100)) else 0f
}

@Entity(tableName = "workout_plans")
data class WorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val level: String,
    val category: String,
    val description: String,
    val estimatedCalories: Int,
    val durationMinutes: Int,
    val challengeDay: Int? = null,
    val premiumOnly: Boolean = level != "Beginner" || challengeDay != null,
    val challengeName: String? = null
)

@Entity(
    tableName = "exercises",
    foreignKeys = [ForeignKey(
        entity = WorkoutPlan::class,
        parentColumns = ["id"],
        childColumns = ["planId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("planId")]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planId: Int,
    val name: String,
    val repsOrDuration: String,
    val sets: Int,
    val restSeconds: Int,
    val instruction: String,
    val durationSeconds: Int? = null,
    val description: String = "A guided bodyweight movement.",
    val muscleGroup: String = "Full Body",
    val difficulty: String = "Beginner",
    val equipment: String = "No equipment",
    val imageResName: String = "exercise_placeholder",
    val localVideoName: String = "",
    val caloriesPerMinute: Int = 6,
    val safetyTips: String = "Stop if you feel pain. Keep your movement controlled.",
    val commonMistakes: String = "Avoid rushing repetitions or losing alignment.",
    val isPremium: Boolean = false,
    val isLowImpact: Boolean = false,
    val isNoJumping: Boolean = false,
    val isKneeFriendly: Boolean = true,
    val isBackFriendly: Boolean = true
)

@Entity(
    tableName = "completed_workouts",
    indices = [Index("planId")]
)
data class CompletedWorkout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planId: Int,
    val planTitle: String,
    val completedAt: Long,
    val caloriesBurned: Int,
    val durationMinutes: Int
)

@Entity(tableName = "premium_status")
data class PremiumStatus(@PrimaryKey val id: Int = 1, val isPremiumUser: Boolean = false)

@Entity(tableName = "water_logs")
data class WaterLog(@PrimaryKey val date: String, val amountMl: Int = 0, val goalMl: Int = 2500)

@Entity(tableName = "health_metrics")
data class HealthMetric(
    @PrimaryKey val id: Int = 1,
    val bmi: Float = 0f,
    val bmr: Float = 0f,
    val calorieNeeds: Float = 0f,
    val waterMl: Int = 0,
    val idealWeightMin: Float = 0f,
    val idealWeightMax: Float = 0f
)

@Entity(tableName = "reminder_settings")
data class ReminderSettings(
    @PrimaryKey val id: Int = 1,
    val workoutTime: String = "07:00",
    val waterReminder: Boolean = true,
    val mealReminder: Boolean = false,
    val weightCheckIn: Boolean = true,
    val progressPhotoDay: String = "Sunday"
)

@Entity(tableName = "custom_workout_plans")
data class CustomWorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goal: String,
    val level: String,
    val availableMinutes: Int,
    val equipment: String,
    val generatedTitle: String,
    val bodyFocus: String = "Full Body"
)

@Entity(tableName = "body_measurements")
data class BodyMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recordedAt: Long = System.currentTimeMillis(),
    val weightKg: Float,
    val waistCm: Float = 0f,
    val chestCm: Float = 0f,
    val armsCm: Float = 0f,
    val thighsCm: Float = 0f,
    val hipsCm: Float = 0f,
    val bodyFatPercent: Float = 0f
)

@Entity(tableName = "progress_photos")
data class ProgressPhoto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUri: String,
    val label: String = "Progress photo"
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val code: String,
    val title: String,
    val description: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "fitness_test_results")
data class FitnessTestResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recordedAt: Long = System.currentTimeMillis(),
    val pushUps: Int,
    val plankSeconds: Int,
    val squats: Int,
    val restingHeartRate: Int,
    val score: Int
)

@Entity(tableName = "shareable_workout_summaries")
data class ShareableWorkoutSummary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val completedAt: Long = System.currentTimeMillis(),
    val workoutTitle: String,
    val caloriesBurned: Int,
    val durationMinutes: Int,
    val streak: Int
)

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val country: String = "United States",
    val language: String = "English",
    val selectedLanguageCode: String = "en",
    val selectedCountryCode: String = "US",
    val selectedCurrencyCode: String = "USD",
    val selectedUnitSystem: String = "Imperial",
    val unitSystem: String = "Imperial",
    val dietPreference: String = "Balanced",
    val workoutLocation: String = "Home",
    val injurySafeMode: Boolean = false,
    val analyticsConsent: Boolean = false,
    val cloudSyncConsent: Boolean = false
)

@Entity(tableName = "community_posts")
data class CommunityPost(@PrimaryKey(autoGenerate = true) val id: Int = 0, val author: String, val message: String, val likes: Int = 0)

@Entity(tableName = "recovery_logs")
data class RecoveryLog(@PrimaryKey(autoGenerate = true) val id: Int = 0, val sleepHours: Float, val soreness: Int, val energy: Int, val stress: Int, val recommendation: String)

@Entity(tableName = "safety_acknowledgements")
data class SafetyAcknowledgement(
    @PrimaryKey val id: Int = 1,
    val medicalDisclaimerAccepted: Boolean = false,
    val acceptedAt: Long = 0L
)

@Entity(tableName = "app_announcements")
data class AppAnnouncement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val type: String,
    val active: Boolean = true
)

@Entity(tableName = "support_messages")
data class SupportMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "skipped_workouts")
data class SkippedWorkout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planId: Int,
    val planTitle: String,
    val skippedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_habits")
data class DailyHabit(
    @PrimaryKey val date: String,
    val workoutCompleted: Boolean = false,
    val waterGoalCompleted: Boolean = false,
    val stepsCompleted: Boolean = false,
    val mealPlanFollowed: Boolean = false,
    val sleepLogged: Boolean = false,
    val stretchingCompleted: Boolean = false
) {
    val completionPercentage: Int get() = listOf(
        workoutCompleted, waterGoalCompleted, stepsCompleted, mealPlanFollowed, sleepLogged, stretchingCompleted
    ).count { it } * 100 / 6
}

@Entity(tableName = "favorite_workouts")
data class FavoriteWorkout(@PrimaryKey val planId: Int, val savedAt: Long = System.currentTimeMillis())

@Entity(tableName = "recently_viewed_workouts")
data class RecentlyViewedWorkout(@PrimaryKey val planId: Int, val viewedAt: Long = System.currentTimeMillis())

data class QuickWorkout(
    val durationMinutes: Int,
    val filters: Set<String>,
    val exerciseNames: List<String>,
    val estimatedCalories: Int,
    val exercises: List<Exercise> = emptyList(),
)

data class WorkoutRecommendation(
    val planId: Int,
    val title: String,
    val reason: String
)

data class FitnessScore(
    val value: Int,
    val explanation: String,
    val tips: List<String>,
    val levelLabel: String,
    val weeklyComparison: String
)

data class AIChatMessage(val role: String, val text: String)

data class ProgressReport(
    val userName: String,
    val dateRange: String,
    val workoutsCompleted: Int,
    val caloriesBurned: Int,
    val streak: Int,
    val measurementSummary: String,
    val waterSummary: String,
    val fitnessScore: Int,
    val challengeProgress: String,
    val progressPhotoPlaceholder: String
)

data class WeeklyReport(
    val workoutsCompleted: Int,
    val missedWorkouts: Int,
    val caloriesBurned: Int,
    val bestWorkoutWeek: String,
    val improvementPlan: String
)

data class MonthlyReport(
    val workoutsCompleted: Int,
    val caloriesBurned: Int,
    val transformationSummary: String,
    val improvementPlan: String
)

data class PremiumComparisonFeature(val title: String, val freeValue: String, val premiumValue: String)

data class WorkoutContentCategory(
    val title: String,
    val description: String,
    val beginnerFriendly: Boolean,
    val premiumOnly: Boolean,
    val relatedPlans: List<String>
)

data class PricingDisplay(val currency: String, val monthly: Double, val yearly: Double, val lifetime: Double)

data class WorkoutPlanWithExercises(
    val plan: WorkoutPlan,
    val exercises: List<Exercise>
)
