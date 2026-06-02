package com.example.fitnessworkout.data

import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.WorkoutPlanWithExercises

object SampleData {
    val quotes = listOf(
        "Small steps build strong habits.",
        "Your future self is cheering for you.",
        "Consistency beats intensity when intensity is inconsistent.",
        "One workout is always better than no workout."
    )

    fun plans(): List<WorkoutPlanWithExercises> {
        val categories = listOf("Full Body", "Chest", "Legs", "Arms", "Abs", "Cardio")
        val levels = listOf("Beginner", "Intermediate", "Advanced")
        return levels.flatMapIndexed { levelIndex, level ->
            categories.mapIndexed { categoryIndex, category ->
                val intensity = levelIndex + 1
                val title = "$level $category"
                val plan = WorkoutPlan(
                    title = title,
                    level = level,
                    category = category,
                    description = "A focused $category session designed for ${level.lowercase()} training.",
                    estimatedCalories = 120 + (levelIndex * 70) + (categoryIndex * 10),
                    durationMinutes = 18 + (levelIndex * 8) + categoryIndex
                )
                WorkoutPlanWithExercises(plan, exercisesFor(category, intensity))
            }
        } + challengePlans()
    }

    private fun exercisesFor(category: String, intensity: Int): List<Exercise> {
        val reps = "${10 + intensity * 4} reps"
        val exercises = when (category) {
            "Chest" -> listOf("Push-Ups", "Incline Push-Ups", "Chest Press", "Plank Shoulder Taps")
            "Legs" -> listOf("Squats", "Reverse Lunges", "Glute Bridges", "Wall Sit")
            "Arms" -> listOf("Tricep Dips", "Bicep Curls", "Arm Circles", "Diamond Push-Ups")
            "Abs" -> listOf("Crunches", "Bicycle Crunches", "Leg Raises", "Plank")
            "Cardio" -> listOf("Jumping Jacks", "High Knees", "Mountain Climbers", "Burpees")
            else -> listOf("Squats", "Push-Ups", "Mountain Climbers", "Plank")
        }
        return exercises.mapIndexed { index, name ->
            val timed = name in listOf("Plank", "Wall Sit", "Jumping Jacks", "High Knees", "Mountain Climbers", "Arm Circles")
            Exercise(
                planId = 0,
                name = name,
                repsOrDuration = if (timed) "${30 + intensity * 10} sec" else reps,
                sets = 2 + intensity,
                restSeconds = 40 - intensity * 5,
                instruction = instructionFor(name),
                durationSeconds = if (timed) 30 + intensity * 10 else null,
                description = "$name targets $category with a controlled ${if (timed) "timed" else "repetition"} interval.",
                muscleGroup = category,
                difficulty = if (intensity == 1) "Beginner" else if (intensity == 2) "Intermediate" else "Advanced",
                equipment = if (name in listOf("Chest Press", "Bicep Curls", "Step Ups")) "Optional dumbbells" else "No equipment",
                imageResName = imageName(name),
                localVideoName = "video_${imageName(name).removePrefix("exercise_")}",
                caloriesPerMinute = 5 + intensity,
                safetyTips = "Warm up first. Keep your form steady and stop if you feel pain.",
                commonMistakes = "Avoid rushing, holding your breath, or losing alignment."
            )
        }
    }

    private fun challengePlans(): List<WorkoutPlanWithExercises> = (1..30).map { day ->
        val category = listOf("Full Body", "Cardio", "Abs", "Legs")[day % 4]
        val challenge = listOf("30-Day Beginner Fitness", "30-Day Fat Loss", "30-Day Muscle Gain", "30-Day Abs")[day % 4]
        WorkoutPlanWithExercises(
            WorkoutPlan(
                title = "$challenge • Day $day",
                level = if (day < 11) "Beginner" else if (day < 21) "Intermediate" else "Advanced",
                category = category,
                description = "Day $day of your 30-day consistency challenge.",
                estimatedCalories = 130 + day * 5,
                durationMinutes = 15 + day / 2,
                challengeDay = day,
                premiumOnly = challenge != "30-Day Beginner Fitness",
                challengeName = challenge
            ),
            exercisesFor(category, if (day < 11) 1 else if (day < 21) 2 else 3)
        )
    }

    private fun instructionFor(name: String) = when (name) {
        "Squats" -> "Keep your chest lifted and sit your hips back."
        "Push-Ups", "Incline Push-Ups", "Diamond Push-Ups" -> "Keep a straight line from shoulders to heels."
        "Plank" -> "Brace your core and keep your hips level."
        "Reverse Lunges" -> "Step back softly and keep the front knee aligned."
        else -> "Move with control, breathe steadily, and maintain good form."
    }

    private fun imageName(name: String) = "exercise_" + name.lowercase()
        .replace("-", "").replace(" ", "_")
}
