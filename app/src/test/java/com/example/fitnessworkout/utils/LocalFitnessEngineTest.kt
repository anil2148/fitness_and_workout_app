package com.example.fitnessworkout.utils

import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalFitnessEngineTest {
    @Test fun recommendationPrefersMatchingGoalAndLevel() {
        val user = UserProfile(
            name = "Test",
            age = 30,
            weightKg = 70f,
            heightCm = 175f,
            fitnessGoal = "Build Muscle",
            fitnessLevel = "Beginner",
            availableMinutes = 20
        )
        val plans = listOf(
            WorkoutPlan(id = 1, title = "Beginner Cardio", level = "Beginner", category = "Cardio", description = "", estimatedCalories = 100, durationMinutes = 20, premiumOnly = false),
            WorkoutPlan(id = 2, title = "Beginner Arms", level = "Beginner", category = "Arms", description = "", estimatedCalories = 100, durationMinutes = 20, premiumOnly = false)
        )

        val recommendation = LocalFitnessEngine.recommendation(user, plans, emptyList(), emptyList(), "Home", false, false)

        assertEquals(2, recommendation?.planId)
        assertTrue(recommendation?.reason.orEmpty().contains("20-minute Home workouts"))
    }
}
