package com.example.fitnessworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnessworkout.data.SampleData
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.repository.FitnessRepository
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
) {
    val standardPlans get() = plans.filter { it.challengeDay == null }
    val challengePlans get() = plans.filter { it.challengeDay != null }.sortedBy { it.challengeDay }
    val todayWorkout get() = standardPlans.firstOrNull()
    val completedPlanIds get() = history.map { it.planId }.toSet()
}

@OptIn(ExperimentalCoroutinesApi::class)
class FitnessViewModel(private val repository: FitnessRepository) : ViewModel() {
    private val selectedPlanId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<FitnessUiState> = combine(
        repository.user,
        repository.plans,
        repository.completedWorkouts
    ) { user, plans, history ->
        FitnessUiState(
            isLoading = false,
            user = user,
            plans = plans,
            history = history,
            totalCalories = history.sumOf { it.caloriesBurned },
            weeklyCount = history.count { isCurrentWeek(it.completedAt) },
            streak = calculateStreak(history),
            quote = SampleData.quotes[LocalDate.now().dayOfYear % SampleData.quotes.size]
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
    }

    fun saveUser(name: String, age: Int, weight: Float, height: Float, goal: String) {
        viewModelScope.launch {
            repository.saveUser(UserProfile(name = name, age = age, weightKg = weight, heightCm = height, fitnessGoal = goal))
        }
    }

    fun completeSelectedWorkout() {
        selectedPlan.value?.let { plan ->
            viewModelScope.launch { repository.completeWorkout(plan) }
        }
    }

    fun resetProgress() {
        viewModelScope.launch { repository.resetProgress() }
    }

    private fun isCurrentWeek(timestamp: Long): Boolean {
        val date = timestamp.toDate()
        val weekFields = WeekFields.of(Locale.getDefault())
        val today = LocalDate.now()
        return date.get(weekFields.weekOfWeekBasedYear()) == today.get(weekFields.weekOfWeekBasedYear()) &&
            date.year == today.year
    }

    private fun calculateStreak(history: List<CompletedWorkout>): Int {
        val dates = history.map { it.completedAt.toDate() }.distinct().sortedDescending()
        if (dates.isEmpty()) return 0
        var expected = LocalDate.now()
        if (dates.first() != expected) expected = expected.minusDays(1)
        var streak = 0
        for (date in dates) {
            if (date == expected) {
                streak++
                expected = expected.minusDays(1)
            } else if (date < expected) break
        }
        return streak
    }

    private fun Long.toDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

class FitnessViewModelFactory(private val repository: FitnessRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        FitnessViewModel(repository) as T
}
