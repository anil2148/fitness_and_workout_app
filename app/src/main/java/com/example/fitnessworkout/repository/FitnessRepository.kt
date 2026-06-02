package com.example.fitnessworkout.repository

import com.example.fitnessworkout.data.SampleData
import com.example.fitnessworkout.data.local.FitnessDao
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan

class FitnessRepository(private val dao: FitnessDao) {
    val user = dao.observeUser()
    val plans = dao.observePlans()
    val completedWorkouts = dao.observeCompletedWorkouts()

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
}
