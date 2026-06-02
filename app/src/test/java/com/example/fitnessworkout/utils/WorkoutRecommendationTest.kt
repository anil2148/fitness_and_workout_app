package com.example.fitnessworkout.utils

import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutRecommendationTest {
    private val beginner = UserProfile(
        name = "Test",
        age = 30,
        weightKg = 70f,
        heightCm = 175f,
        fitnessGoal = "Stay Fit",
        fitnessLevel = "Beginner",
        availableMinutes = 20,
    )

    @Test fun beginnerUserGetsBeginnerWorkout() {
        val plans = listOf(plan(1, "Beginner Full Body", "Beginner"), plan(2, "Advanced Full Body", "Advanced"))

        assertEquals(1, LocalFitnessEngine.recommendation(beginner, plans, emptyList(), emptyList(), "Home", false, false)?.planId)
    }

    @Test fun premiumOnlyRecommendationIsHiddenForFreeUser() {
        val plans = listOf(plan(1, "Free Cardio", "Advanced"), plan(2, "Premium Beginner", "Beginner", premiumOnly = true))

        assertEquals(1, LocalFitnessEngine.recommendation(beginner, plans, emptyList(), emptyList(), "Home", false, false)?.planId)
    }

    private fun plan(id: Int, title: String, level: String, premiumOnly: Boolean = false) =
        WorkoutPlan(id = id, title = title, level = level, category = "Full Body", description = "", estimatedCalories = 100, durationMinutes = 20, premiumOnly = premiumOnly)
}
