package com.example.fitnessworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnessworkout.data.SampleData
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.CustomWorkoutPlan
import com.example.fitnessworkout.data.model.HealthMetric
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
import com.example.fitnessworkout.data.model.ProgressReport
import com.example.fitnessworkout.data.model.DailyHabit
import com.example.fitnessworkout.data.model.FavoriteWorkout
import com.example.fitnessworkout.data.model.RecentlyViewedWorkout
import com.example.fitnessworkout.repository.FitnessRepository
import com.example.fitnessworkout.utils.LocalFitnessEngine
import com.example.fitnessworkout.utils.FitnessCalculations
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FitnessUiState(
    val isLoading: Boolean = true,
    val user: UserProfile? = null,
    val plans: List<WorkoutPlan> = emptyList(),
    val history: List<CompletedWorkout> = emptyList(),
    val totalCalories: Int = 0,
    val weeklyCount: Int = 0,
    val streak: Int = 0,
    val quote: String = SampleData.quotes.first()
    ,
    val isPremiumUser: Boolean = false,
    val water: WaterLog = WaterLog(LocalDate.now().toString()),
    val healthMetric: HealthMetric? = null,
    val reminders: ReminderSettings = ReminderSettings(),
    val customPlans: List<CustomWorkoutPlan> = emptyList()
    ,
    val measurements: List<BodyMeasurement> = emptyList(),
    val photos: List<ProgressPhoto> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val fitnessTests: List<FitnessTestResult> = emptyList(),
    val shareSummaries: List<ShareableWorkoutSummary> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val communityPosts: List<CommunityPost> = emptyList(),
    val recovery: List<RecoveryLog> = emptyList(),
    val allExercises: List<Exercise> = emptyList(),
    val safetyAcknowledgement: SafetyAcknowledgement = SafetyAcknowledgement(),
    val announcements: List<AppAnnouncement> = emptyList(),
    val supportMessages: List<SupportMessage> = emptyList(),
    val skippedWorkouts: List<SkippedWorkout> = emptyList(),
    val dailyHabit: DailyHabit = DailyHabit(LocalDate.now().toString()),
    val favoriteWorkouts: List<FavoriteWorkout> = emptyList(),
    val recentlyViewedWorkouts: List<RecentlyViewedWorkout> = emptyList()
) {
    val standardPlans get() = plans.filter { it.challengeDay == null }
    val challengePlans get() = plans.filter { it.challengeDay != null }.sortedBy { it.challengeDay }
    val todayWorkout get() = standardPlans.firstOrNull()
    val completedPlanIds get() = history.map { it.planId }.toSet()
    val recommendation get() = LocalFitnessEngine.recommendation(user, standardPlans, history, skippedWorkouts, settings.workoutLocation, settings.injurySafeMode, isPremiumUser)
    val fitnessScore get() = LocalFitnessEngine.fitnessScore(history, streak, weeklyCount, fitnessTests, water, measurements)
    val favoritePlanIds get() = favoriteWorkouts.map { it.planId }.toSet()
    val recentPlans get() = recentlyViewedWorkouts.mapNotNull { recent -> plans.firstOrNull { it.id == recent.planId } }
    val weeklyReport get() = LocalFitnessEngine.weeklyReport(history, skippedWorkouts)
    val monthlyReport get() = LocalFitnessEngine.monthlyReport(history)
}

@OptIn(ExperimentalCoroutinesApi::class)
class FitnessViewModel(private val repository: FitnessRepository) : ViewModel() {
    private val selectedPlanId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<FitnessUiState> = combine(
        repository.user,
        repository.plans,
        repository.completedWorkouts,
        repository.premium,
        repository.water,
        repository.healthMetric,
        repository.reminders,
        repository.customPlans
        , repository.measurements, repository.photos, repository.achievements, repository.fitnessTests, repository.shareSummaries,
        repository.settings, repository.communityPosts, repository.recovery, repository.allExercises,
        repository.safetyAcknowledgement, repository.announcements, repository.supportMessages, repository.skippedWorkouts,
        repository.dailyHabit, repository.favoriteWorkouts, repository.recentlyViewedWorkouts
    ) { values ->
        val user = values[0] as UserProfile?
        @Suppress("UNCHECKED_CAST") val plans = values[1] as List<WorkoutPlan>
        @Suppress("UNCHECKED_CAST") val history = values[2] as List<CompletedWorkout>
        val premium = values[3] as com.example.fitnessworkout.data.model.PremiumStatus?
        val water = values[4] as WaterLog?
        val healthMetric = values[5] as HealthMetric?
        val reminders = values[6] as ReminderSettings?
        @Suppress("UNCHECKED_CAST") val customPlans = values[7] as List<CustomWorkoutPlan>
        @Suppress("UNCHECKED_CAST") val measurements = values[8] as List<BodyMeasurement>
        @Suppress("UNCHECKED_CAST") val photos = values[9] as List<ProgressPhoto>
        @Suppress("UNCHECKED_CAST") val achievements = values[10] as List<Achievement>
        @Suppress("UNCHECKED_CAST") val fitnessTests = values[11] as List<FitnessTestResult>
        @Suppress("UNCHECKED_CAST") val shareSummaries = values[12] as List<ShareableWorkoutSummary>
        val settings = values[13] as AppSettings?
        @Suppress("UNCHECKED_CAST") val communityPosts = values[14] as List<CommunityPost>
        @Suppress("UNCHECKED_CAST") val recovery = values[15] as List<RecoveryLog>
        @Suppress("UNCHECKED_CAST") val allExercises = values[16] as List<Exercise>
        val safetyAcknowledgement = values[17] as SafetyAcknowledgement?
        @Suppress("UNCHECKED_CAST") val announcements = values[18] as List<AppAnnouncement>
        @Suppress("UNCHECKED_CAST") val supportMessages = values[19] as List<SupportMessage>
        @Suppress("UNCHECKED_CAST") val skippedWorkouts = values[20] as List<SkippedWorkout>
        val dailyHabit = values[21] as DailyHabit?
        @Suppress("UNCHECKED_CAST") val favoriteWorkouts = values[22] as List<FavoriteWorkout>
        @Suppress("UNCHECKED_CAST") val recentlyViewedWorkouts = values[23] as List<RecentlyViewedWorkout>
        FitnessUiState(
            isLoading = false,
            user = user,
            plans = plans,
            history = history,
            totalCalories = history.sumOf { it.caloriesBurned },
            weeklyCount = history.count { isCurrentWeek(it.completedAt) },
            streak = FitnessCalculations.streak(history.map { it.completedAt }),
            quote = SampleData.quotes[LocalDate.now().dayOfYear % SampleData.quotes.size],
            isPremiumUser = premium?.isPremiumUser ?: false,
            water = water ?: WaterLog(LocalDate.now().toString()),
            healthMetric = healthMetric,
            reminders = reminders ?: ReminderSettings(),
            customPlans = customPlans, measurements = measurements, photos = photos, achievements = achievements,
            fitnessTests = fitnessTests, shareSummaries = shareSummaries, settings = settings ?: AppSettings(),
            communityPosts = communityPosts, recovery = recovery, allExercises = allExercises,
            safetyAcknowledgement = safetyAcknowledgement ?: SafetyAcknowledgement(), announcements = announcements,
            supportMessages = supportMessages, skippedWorkouts = skippedWorkouts,
            dailyHabit = dailyHabit ?: DailyHabit(LocalDate.now().toString()), favoriteWorkouts = favoriteWorkouts,
            recentlyViewedWorkouts = recentlyViewedWorkouts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FitnessUiState())

    val selectedPlan: StateFlow<WorkoutPlan?> = combine(selectedPlanId, repository.plans) { id, plans ->
        plans.firstOrNull { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val selectedExercises: StateFlow<List<Exercise>> = selectedPlanId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repository.exercises(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { repository.initialize() }
    }

    fun selectPlan(planId: Int) {
        selectedPlanId.value = planId
        viewModelScope.launch { repository.recordRecentlyViewed(planId) }
    }

    fun saveUser(name: String, age: Int, weight: Float, height: Float, goal: String, darkMode: Boolean = uiState.value.user?.darkMode ?: false,
        level: String = uiState.value.user?.fitnessLevel ?: "Beginner", minutes: Int = uiState.value.user?.availableMinutes ?: 20,
        equipment: String = uiState.value.user?.equipment ?: "No Equipment", style: String = uiState.value.user?.workoutStyle ?: "Balanced") {
        viewModelScope.launch {
            repository.saveUser(UserProfile(name = name, age = age, weightKg = weight, heightCm = height, fitnessGoal = goal, darkMode = darkMode,
                fitnessLevel = level, availableMinutes = minutes, equipment = equipment, workoutStyle = style))
        }
    }

    fun setDarkMode(enabled: Boolean) {
        uiState.value.user?.let { user -> viewModelScope.launch { repository.saveUser(user.copy(darkMode = enabled)) } }
    }

    fun completeSelectedWorkout() {
        selectedPlan.value?.let { plan ->
            viewModelScope.launch { repository.completeWorkout(plan, uiState.value.streak) }
        }
    }

    fun resetProgress() {
        viewModelScope.launch { repository.resetProgress() }
    }

    fun setPremium(enabled: Boolean) = viewModelScope.launch { repository.setPremium(enabled) }
    fun addWater(amountMl: Int) = viewModelScope.launch { if (amountMl > 0) repository.addWater(amountMl, uiState.value.water) }
    fun resetWater() = viewModelScope.launch { repository.resetWater(uiState.value.water) }
    fun saveReminders(settings: ReminderSettings) = viewModelScope.launch { repository.saveReminders(settings) }
    fun calculateHealth(weightKg: Float, heightCm: Float, age: Int, activityMultiplier: Float = 1.375f) {
        if (weightKg <= 0 || heightCm <= 0 || age <= 0) return
        val meters = heightCm / 100f
        val bmi = FitnessCalculations.bmi(weightKg, heightCm)
        val bmr = FitnessCalculations.bmr(weightKg, heightCm, age)
        viewModelScope.launch {
            repository.saveHealthMetric(HealthMetric(bmi = bmi, bmr = bmr, calorieNeeds = FitnessCalculations.calorieNeeds(bmr, activityMultiplier),
                waterMl = FitnessCalculations.waterIntakeMl(weightKg), idealWeightMin = 18.5f * meters * meters, idealWeightMax = 24.9f * meters * meters))
        }
    }
    fun generateCustomPlan(goal: String, level: String, minutes: Int, equipment: String, bodyFocus: String = "Full Body") =
        viewModelScope.launch { repository.saveCustomPlan(CustomWorkoutPlan(goal = goal, level = level, availableMinutes = minutes, equipment = equipment, generatedTitle = "$minutes-min $bodyFocus $goal plan", bodyFocus = bodyFocus)) }
    fun saveMeasurement(item: BodyMeasurement) = viewModelScope.launch {
        val values = listOf(item.weightKg, item.waistCm, item.chestCm, item.armsCm, item.thighsCm, item.hipsCm, item.bodyFatPercent)
        if (item.weightKg > 0f && values.all { it >= 0f } && item.bodyFatPercent <= 100f &&
            (uiState.value.isPremiumUser || uiState.value.measurements.size < 3)) repository.saveMeasurement(item)
    }
    fun savePhoto(uri: String) = viewModelScope.launch {
        if (uiState.value.isPremiumUser || uiState.value.photos.size < 2) repository.savePhoto(ProgressPhoto(imageUri = uri))
    }
    fun deletePhoto(id: Int) = viewModelScope.launch { repository.deletePhoto(id) }
    fun saveFitnessTest(pushUps: Int, plank: Int, squats: Int, heartRate: Int) = viewModelScope.launch {
        if (pushUps >= 0 && plank >= 0 && squats >= 0 && heartRate > 0) {
            val score = pushUps + plank / 10 + squats - (heartRate - 60).coerceAtLeast(0) / 2
            repository.saveFitnessTest(FitnessTestResult(pushUps = pushUps, plankSeconds = plank, squats = squats, restingHeartRate = heartRate, score = score))
        }
    }
    fun deleteAllData() = viewModelScope.launch { repository.deleteAllData() }
    fun saveSettings(item: AppSettings, onSaved: (() -> Unit)? = null) = viewModelScope.launch {
        repository.saveSettings(item)
        onSaved?.invoke()
    }
    fun completeOnboarding(
        name: String,
        age: Int,
        weight: Float,
        height: Float,
        goal: String,
        level: String,
        minutes: Int,
        equipment: String,
        style: String,
        settings: AppSettings,
        onSaved: (() -> Unit)? = null,
    ) = viewModelScope.launch {
        repository.saveUser(UserProfile(name = name, age = age, weightKg = weight, heightCm = height, fitnessGoal = goal,
            fitnessLevel = level, availableMinutes = minutes, equipment = equipment, workoutStyle = style))
        repository.saveSettings(settings)
        repository.acknowledgeSafety()
        onSaved?.invoke()
    }
    fun saveRecovery(sleep: Float, soreness: Int, energy: Int, stress: Int) = viewModelScope.launch {
        if (sleep in 0f..24f && soreness in 1..10 && energy in 1..10 && stress in 1..10) {
            val recommendation = when { sleep < 6 || stress > 7 -> "Rest day"; soreness > 6 -> "Light stretching"; energy > 7 -> "Strength workout"; else -> "Moderate workout" }
            repository.saveRecovery(RecoveryLog(sleepHours = sleep, soreness = soreness, energy = energy, stress = stress, recommendation = recommendation))
        }
    }
    fun quickWorkout(minutes: Int, filters: Set<String>) = LocalFitnessEngine.quickWorkout(uiState.value.allExercises, minutes, filters)
    fun completeQuickWorkout(workout: QuickWorkout) = viewModelScope.launch { repository.completeQuickWorkout(workout, uiState.value.streak) }
    fun acknowledgeSafety() = viewModelScope.launch { repository.acknowledgeSafety() }
    fun skipSelectedWorkout() = selectedPlan.value?.let { plan -> viewModelScope.launch { repository.skipWorkout(plan) } }
    fun sendSupportMessage(email: String, message: String) = viewModelScope.launch {
        if (email.isNotBlank() && message.isNotBlank()) repository.sendSupportMessage(email.trim(), message.trim())
    }
    fun progressReport(): ProgressReport {
        val state = uiState.value
        return ProgressReport(
            userName = state.user?.name ?: "Fitness member",
            dateRange = "All locally tracked activity",
            workoutsCompleted = state.history.size,
            caloriesBurned = state.totalCalories,
            streak = state.streak,
            measurementSummary = state.measurements.firstOrNull()?.let { "${it.weightKg} kg, waist ${it.waistCm} cm" } ?: "No measurements recorded",
            waterSummary = "${state.water.amountMl} / ${state.water.goalMl} ml today",
            fitnessScore = state.fitnessScore.value,
            challengeProgress = "${state.challengePlans.count { it.id in state.completedPlanIds }} / 30 days",
            progressPhotoPlaceholder = if (state.photos.isEmpty()) "No progress photo selected" else "${state.photos.size} local progress photo(s)"
        )
    }
    fun saveDailyHabit(item: DailyHabit) = viewModelScope.launch { repository.saveDailyHabit(item) }
    fun toggleFavorite(planId: Int) = viewModelScope.launch { repository.toggleFavorite(planId, planId in uiState.value.favoritePlanIds) }

    private fun isCurrentWeek(timestamp: Long): Boolean {
        val date = timestamp.toDate()
        val weekFields = WeekFields.of(Locale.getDefault())
        val today = LocalDate.now()
        return date.get(weekFields.weekOfWeekBasedYear()) == today.get(weekFields.weekOfWeekBasedYear()) &&
            date.year == today.year
    }

    private fun Long.toDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

class FitnessViewModelFactory(private val repository: FitnessRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        FitnessViewModel(repository) as T
}
