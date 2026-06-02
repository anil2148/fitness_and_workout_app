package com.example.fitnessworkout.utils

import com.example.fitnessworkout.data.model.BodyMeasurement
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.FitnessScore
import com.example.fitnessworkout.data.model.FitnessTestResult
import com.example.fitnessworkout.data.model.QuickWorkout
import com.example.fitnessworkout.data.model.SkippedWorkout
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WaterLog
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.WorkoutRecommendation
import com.example.fitnessworkout.data.model.WeeklyReport
import com.example.fitnessworkout.data.model.MonthlyReport
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object LocalFitnessEngine {
    fun quickWorkout(exercises: List<Exercise>, minutes: Int, filters: Set<String>): QuickWorkout {
        val matches = exercises.distinctBy { it.name }.filter { exercise ->
            ("No equipment" !in filters || exercise.equipment.equals("No equipment", ignoreCase = true)) &&
                ("Low impact" !in filters || exercise.isLowImpact) &&
                ("No jumping" !in filters || exercise.isNoJumping) &&
                ("Office friendly" !in filters || (exercise.isLowImpact && exercise.isNoJumping)) &&
                ("Beginner safe" !in filters || (exercise.difficulty == "Beginner" && exercise.isKneeFriendly && exercise.isBackFriendly))
        }
        val fallback = exercises.distinctBy { it.name }.filter { it.isNoJumping }
        val pool = (matches.ifEmpty { fallback }).ifEmpty { exercises.distinctBy { it.name } }
        val count = (minutes / 3).coerceAtLeast(2)
        val selected = pool.take(count)
        return QuickWorkout(
            durationMinutes = minutes,
            filters = filters,
            exerciseNames = selected.map { it.name },
            estimatedCalories = selected.sumOf { it.caloriesPerMinute } * minutes / selected.size.coerceAtLeast(1)
        )
    }

    fun recommendation(
        user: UserProfile?,
        plans: List<WorkoutPlan>,
        history: List<CompletedWorkout>,
        skipped: List<SkippedWorkout>,
        location: String,
        injurySafeMode: Boolean,
        premium: Boolean
    ): WorkoutRecommendation? {
        val available = plans.filter { it.challengeDay == null && (premium || !it.premiumOnly) }
        val scored = available.map { plan ->
            var score = 0
            if (plan.level == user?.fitnessLevel.orEmpty()) score += 5
            if (plan.durationMinutes <= (user?.availableMinutes ?: 20) + 5) score += 4
            if (plan.id !in history.take(4).map { it.planId }) score += 3
            if (plan.id !in skipped.take(3).map { it.planId }) score += 3
            if (user?.fitnessGoal == "Lose Weight" && plan.category in listOf("Cardio", "Full Body", "Abs")) score += 4
            if (user?.fitnessGoal == "Build Muscle" && plan.category in listOf("Chest", "Legs", "Arms")) score += 4
            if (location.contains("Office") && plan.category in listOf("Full Body", "Abs")) score += 2
            if (injurySafeMode && plan.level == "Beginner") score += 3
            plan to score
        }
        val best = scored.maxByOrNull { it.second }?.first ?: return null
        return WorkoutRecommendation(
            planId = best.id,
            title = best.title,
            reason = "Recommended because you prefer ${user?.availableMinutes ?: 20}-minute $location workouts" +
                if (injurySafeMode) " with injury-safe pacing." else "."
        )
    }

    fun fitnessScore(
        history: List<CompletedWorkout>,
        streak: Int,
        weeklyCount: Int,
        fitnessTests: List<FitnessTestResult>,
        water: WaterLog,
        measurements: List<BodyMeasurement>
    ): FitnessScore {
        val consistency = (weeklyCount * 5).coerceAtMost(25)
        val streakPoints = (streak * 3).coerceAtMost(20)
        val completion = history.size.coerceAtMost(15)
        val test = (fitnessTests.firstOrNull()?.score ?: 0).coerceIn(0, 20)
        val hydration = ((water.amountMl / water.goalMl.coerceAtLeast(1).toFloat()) * 10).toInt().coerceIn(0, 10)
        val measurement = if (measurements.size >= 2) 10 else measurements.size * 4
        val value = (consistency + streakPoints + completion + test + hydration + measurement).coerceIn(0, 100)
        val tips = buildList {
            if (weeklyCount < 3) add("Complete three workouts this week.")
            if (hydration < 8) add("Log more water to support recovery.")
            if (fitnessTests.isEmpty()) add("Take the fitness level test for a stronger baseline.")
            if (measurements.size < 2) add("Add another body measurement to track progress.")
            if (isEmpty()) add("Keep your current routine going.")
        }
        val level = when {
            value >= 80 -> "Excellent"
            value >= 60 -> "Strong"
            value >= 40 -> "Building"
            else -> "Getting started"
        }
        val comparison = if (weeklyCount >= 3) "You are on track this week." else "Add ${3 - weeklyCount} workout(s) to reach this week's consistency target."
        return FitnessScore(value, "Your score combines consistency, streak, completed sessions, fitness tests, hydration, and measurement progress.", tips, level, comparison)
    }

    fun weeklyReport(history: List<CompletedWorkout>, skipped: List<SkippedWorkout>): WeeklyReport {
        val weekStart = LocalDate.now().minusDays(6)
        val workouts = history.filter { it.completedAt.toDate() >= weekStart }
        val missed = skipped.count { it.skippedAt.toDate() >= weekStart }
        return WeeklyReport(
            workoutsCompleted = workouts.size,
            missedWorkouts = missed,
            caloriesBurned = workouts.sumOf { it.caloriesBurned },
            bestWorkoutWeek = if (workouts.isEmpty()) "Complete a workout to start your timeline." else "${workouts.size} workout(s) completed in the last 7 days.",
            improvementPlan = if (missed > 0) "Try a 5-minute quick workout on your busiest day." else "Keep one short recovery workout on your calendar."
        )
    }

    fun monthlyReport(history: List<CompletedWorkout>): MonthlyReport {
        val monthStart = LocalDate.now().withDayOfMonth(1)
        val workouts = history.filter { it.completedAt.toDate() >= monthStart }
        return MonthlyReport(
            workoutsCompleted = workouts.size,
            caloriesBurned = workouts.sumOf { it.caloriesBurned },
            transformationSummary = if (workouts.isEmpty()) "Your monthly transformation summary will grow as you train." else "You completed ${workouts.size} workout(s) this month.",
            improvementPlan = "Balance strength, mobility, hydration, and rest next month."
        )
    }

    private fun Long.toDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}
