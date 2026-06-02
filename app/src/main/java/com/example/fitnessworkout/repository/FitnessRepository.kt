package com.example.fitnessworkout.repository

import com.example.fitnessworkout.data.SampleData
import com.example.fitnessworkout.data.local.FitnessDao
import com.example.fitnessworkout.data.local.AppPreferences
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.CustomWorkoutPlan
import com.example.fitnessworkout.data.model.HealthMetric
import com.example.fitnessworkout.data.model.PremiumStatus
import com.example.fitnessworkout.data.model.ReminderSettings
import com.example.fitnessworkout.data.model.WaterLog
import com.example.fitnessworkout.data.model.Achievement
import com.example.fitnessworkout.data.model.BodyMeasurement
import com.example.fitnessworkout.data.model.FitnessTestResult
import com.example.fitnessworkout.data.model.ProgressPhoto
import com.example.fitnessworkout.data.model.ShareableWorkoutSummary
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.data.model.CommunityPost
import com.example.fitnessworkout.data.model.RecoveryLog
import com.example.fitnessworkout.data.model.SafetyAcknowledgement
import com.example.fitnessworkout.data.model.AppAnnouncement
import com.example.fitnessworkout.data.model.SupportMessage
import com.example.fitnessworkout.data.model.SkippedWorkout
import com.example.fitnessworkout.data.model.QuickWorkout
import com.example.fitnessworkout.data.model.DailyHabit
import com.example.fitnessworkout.data.model.FavoriteWorkout
import com.example.fitnessworkout.data.model.RecentlyViewedWorkout
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import com.example.fitnessworkout.utils.AppLocaleManager
import com.example.fitnessworkout.utils.RegionSettings

class FitnessRepository(private val dao: FitnessDao, private val appPreferences: AppPreferences) {
    val user = dao.observeUser()
    val plans = dao.observePlans()
    val completedWorkouts = dao.observeCompletedWorkouts()
    val premium = dao.observePremium()
    val water = dao.observeWater(LocalDate.now().toString())
    val healthMetric = dao.observeHealthMetric()
    val reminders = dao.observeReminders()
    val customPlans = dao.observeCustomPlans()
    val measurements = dao.observeMeasurements()
    val photos = dao.observePhotos()
    val achievements = dao.observeAchievements()
    val fitnessTests = dao.observeFitnessTests()
    val shareSummaries = dao.observeShareSummaries()
    val settings = dao.observeSettings()
    val communityPosts = dao.observeCommunityPosts()
    val recovery = dao.observeRecovery()
    val allExercises = dao.observeAllExercises()
    val safetyAcknowledgement = dao.observeSafetyAcknowledgement()
    val announcements = dao.observeAnnouncements()
    val supportMessages = dao.observeSupportMessages()
    val skippedWorkouts = dao.observeSkippedWorkouts()
    val dailyHabit = dao.observeDailyHabit(LocalDate.now().toString())
    val favoriteWorkouts = dao.observeFavoriteWorkouts()
    val recentlyViewedWorkouts = dao.observeRecentlyViewedWorkouts()

    fun exercises(planId: Int) = dao.observeExercises(planId)

    suspend fun initialize() {
        val languageCode = AppLocaleManager.safeLanguageCode(appPreferences.languageCode.first())
        val settings = dao.getSettings()
        if (settings == null || settings.selectedLanguageCode != languageCode) {
            dao.upsertSettings((settings ?: AppSettings()).copy(
                language = AppLocaleManager.languageName(languageCode),
                selectedLanguageCode = languageCode,
            ))
        }
        if (dao.planCount() == 0) {
            SampleData.plans().forEach { dao.insertPlanWithExercises(it.plan, it.exercises) }
        }
        if (dao.communityPostCount() == 0) dao.insertCommunityPosts(listOf(
            CommunityPost(author = "Maya", message = "Finished the beginner challenge today.", likes = 12),
            CommunityPost(author = "Alex", message = "Desk stretch break complete. Small habits add up.", likes = 8)
        ))
        if (dao.announcementCount() == 0) dao.insertAnnouncements(listOf(
            AppAnnouncement(title = "New challenge", message = "Try the 30-day beginner consistency challenge.", type = "challenge"),
            AppAnnouncement(title = "Featured workout", message = "Quick office-friendly movement breaks are now available.", type = "featured"),
            AppAnnouncement(title = "Premium preview", message = "Explore local AI-ready coaching tools and progress reports.", type = "promo"),
            AppAnnouncement(title = "Maintenance notice", message = "Local tracking remains available during future service maintenance.", type = "maintenance")
        ))
    }

    suspend fun saveUser(user: UserProfile) = dao.upsertUser(user)

    suspend fun completeWorkout(plan: WorkoutPlan, streak: Int) {
        dao.insertCompletedWorkout(CompletedWorkout(
            planId = plan.id,
            planTitle = plan.title,
            completedAt = System.currentTimeMillis(),
            caloriesBurned = plan.estimatedCalories,
            durationMinutes = plan.durationMinutes
        ))
        dao.insertShareSummary(ShareableWorkoutSummary(workoutTitle = plan.title, caloriesBurned = plan.estimatedCalories, durationMinutes = plan.durationMinutes, streak = streak + 1))
        markWorkoutHabitComplete()
        dao.insertAchievement(Achievement("first_workout", "First Step", "Completed your first workout"))
        if (streak + 1 >= 7) dao.insertAchievement(Achievement("seven_day_streak", "Week Warrior", "Maintained a 7-day workout streak"))
    }

    suspend fun completeQuickWorkout(workout: QuickWorkout, streak: Int) {
        val title = "${workout.durationMinutes}-minute Quick Workout"
        dao.insertCompletedWorkout(CompletedWorkout(
            planId = 0,
            planTitle = title,
            completedAt = System.currentTimeMillis(),
            caloriesBurned = workout.estimatedCalories,
            durationMinutes = workout.durationMinutes
        ))
        dao.insertShareSummary(ShareableWorkoutSummary(workoutTitle = title, caloriesBurned = workout.estimatedCalories, durationMinutes = workout.durationMinutes, streak = streak + 1))
        markWorkoutHabitComplete()
        dao.insertAchievement(Achievement("quick_start", "Quick Win", "Completed a quick workout"))
    }

    suspend fun resetProgress() = dao.resetProgress()

    suspend fun setPremium(enabled: Boolean) = dao.upsertPremium(PremiumStatus(isPremiumUser = enabled))
    suspend fun addWater(amountMl: Int, current: WaterLog?) {
        val log = (current ?: WaterLog(LocalDate.now().toString())).copy(amountMl = (current?.amountMl ?: 0) + amountMl)
        dao.upsertWater(log)
        if (log.amountMl >= log.goalMl) {
            val habit = dao.getDailyHabit(log.date) ?: DailyHabit(log.date)
            dao.upsertDailyHabit(habit.copy(waterGoalCompleted = true))
        }
    }
    suspend fun resetWater(current: WaterLog?) {
        val log = (current ?: WaterLog(LocalDate.now().toString())).copy(amountMl = 0)
        dao.upsertWater(log)
        val habit = dao.getDailyHabit(log.date) ?: DailyHabit(log.date)
        dao.upsertDailyHabit(habit.copy(waterGoalCompleted = false))
    }
    suspend fun saveHealthMetric(metric: HealthMetric) = dao.upsertHealthMetric(metric)
    suspend fun saveReminders(settings: ReminderSettings) = dao.upsertReminders(settings)
    suspend fun saveCustomPlan(plan: CustomWorkoutPlan) = dao.insertCustomPlan(plan)
    suspend fun saveMeasurement(item: BodyMeasurement) = dao.insertMeasurement(item)
    suspend fun savePhoto(item: ProgressPhoto) = dao.insertPhoto(item)
    suspend fun deletePhoto(id: Int) = dao.deletePhoto(id)
    suspend fun saveFitnessTest(item: FitnessTestResult) {
        dao.insertFitnessTest(item)
        dao.insertAchievement(Achievement("fitness_test", "Benchmark Set", "Completed a fitness level test"))
    }
    suspend fun deleteAllData() {
        dao.deleteUsers(); dao.deleteWorkouts(); dao.deleteMeasurements(); dao.deletePhotos()
        dao.deleteAchievements(); dao.deleteFitnessTests(); dao.deleteShareSummaries()
        dao.deleteWaterLogs(); dao.deleteHealthMetrics(); dao.deleteReminderSettings(); dao.deleteCustomPlans()
        dao.deletePremiumStatus()
        dao.deleteSettings(); dao.deleteRecovery(); dao.deleteSafetyAcknowledgements()
        dao.deleteSupportMessages(); dao.deleteSkippedWorkouts(); dao.deleteDailyHabits()
        dao.deleteFavoriteWorkouts(); dao.deleteRecentlyViewedWorkouts()
    }
    suspend fun saveSettings(item: AppSettings) {
        val country = RegionSettings.country(item.selectedCountryCode.ifBlank { item.country })
        val languageCode = AppLocaleManager.safeLanguageCode(item.selectedLanguageCode)
        val unitSystem = RegionSettings.safeUnitSystem(item.selectedUnitSystem, country.code)
        val normalized = item.copy(
            country = country.label,
            language = AppLocaleManager.languageName(languageCode),
            selectedLanguageCode = languageCode,
            selectedCountryCode = country.code,
            selectedCurrencyCode = RegionSettings.safeCurrencyCode(item.selectedCurrencyCode),
            selectedUnitSystem = unitSystem,
            unitSystem = unitSystem,
        )
        dao.upsertSettings(normalized)
        appPreferences.setLanguageCode(normalized.selectedLanguageCode)
    }
    suspend fun saveRecovery(item: RecoveryLog) = dao.insertRecovery(item)
    suspend fun acknowledgeSafety() = dao.upsertSafetyAcknowledgement(SafetyAcknowledgement(medicalDisclaimerAccepted = true, acceptedAt = System.currentTimeMillis()))
    suspend fun sendSupportMessage(email: String, message: String) = dao.insertSupportMessage(SupportMessage(email = email, message = message))
    suspend fun skipWorkout(plan: WorkoutPlan) = dao.insertSkippedWorkout(SkippedWorkout(planId = plan.id, planTitle = plan.title))
    suspend fun saveDailyHabit(item: DailyHabit) = dao.upsertDailyHabit(item)
    suspend fun toggleFavorite(planId: Int, favorite: Boolean) =
        if (favorite) dao.deleteFavoriteWorkout(planId) else dao.upsertFavoriteWorkout(FavoriteWorkout(planId))
    suspend fun recordRecentlyViewed(planId: Int) = dao.upsertRecentlyViewedWorkout(RecentlyViewedWorkout(planId))

    private suspend fun markWorkoutHabitComplete() {
        val today = LocalDate.now().toString()
        val habit = dao.getDailyHabit(today) ?: DailyHabit(today)
        dao.upsertDailyHabit(habit.copy(workoutCompleted = true))
    }
}
