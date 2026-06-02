package com.example.fitnessworkout.repository

import com.example.fitnessworkout.data.SampleData
import com.example.fitnessworkout.data.local.FitnessDao
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.CustomWorkoutPlan
import com.example.fitnessworkout.data.model.HealthMetric
import com.example.fitnessworkout.data.model.PremiumStatus
import com.example.fitnessworkout.data.model.ReminderSettings
import com.example.fitnessworkout.data.model.WaterLog
import java.time.LocalDate

class FitnessRepository(private val dao: FitnessDao) {
    val user = dao.observeUser()
    val plans = dao.observePlans()
    val completedWorkouts = dao.observeCompletedWorkouts()
    val premium = dao.observePremium()
    val water = dao.observeWater(LocalDate.now().toString())
    val healthMetric = dao.observeHealthMetric()
    val reminders = dao.observeReminders()
    val customPlans = dao.observeCustomPlans()

    fun exercises(planId: Int) = dao.observeExercises(planId)

    suspend fun initialize() {
        if (dao.planCount() == 0) {
            SampleData.plans().forEach { dao.insertPlanWithExercises(it.plan, it.exercises) }
        }
    }

    suspend fun saveUser(user: UserProfile) = dao.upsertUser(user)

    suspend fun completeWorkout(plan: WorkoutPlan) = dao.insertCompletedWorkout(
        CompletedWorkout(
            planId = plan.id,
            planTitle = plan.title,
            completedAt = System.currentTimeMillis(),
            caloriesBurned = plan.estimatedCalories,
            durationMinutes = plan.durationMinutes
        )
    )

    suspend fun resetProgress() = dao.resetProgress()

    suspend fun setPremium(enabled: Boolean) = dao.upsertPremium(PremiumStatus(isPremiumUser = enabled))
    suspend fun addWater(amountMl: Int, current: WaterLog?) =
        dao.upsertWater((current ?: WaterLog(LocalDate.now().toString())).copy(amountMl = (current?.amountMl ?: 0) + amountMl))
    suspend fun resetWater(current: WaterLog?) =
        dao.upsertWater((current ?: WaterLog(LocalDate.now().toString())).copy(amountMl = 0))
    suspend fun saveHealthMetric(metric: HealthMetric) = dao.upsertHealthMetric(metric)
    suspend fun saveReminders(settings: ReminderSettings) = dao.upsertReminders(settings)
    suspend fun saveCustomPlan(plan: CustomWorkoutPlan) = dao.insertCustomPlan(plan)
}
