package com.example.fitnessworkout.utils

import android.content.Context
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.WorkoutPlan

object ExerciseMedia {
    private const val PLACEHOLDER = "exercise_placeholder"

    private val exerciseImages = mapOf(
        "push ups" to "exercise_push_ups",
        "incline push ups" to "exercise_push_ups",
        "diamond push ups" to "exercise_push_ups",
        "chest press" to "exercise_push_ups",
        "squats" to "exercise_squats",
        "lunges" to "exercise_lunges",
        "reverse lunges" to "exercise_lunges",
        "plank" to "exercise_plank",
        "jumping jacks" to "exercise_jumping_jacks",
        "burpees" to "exercise_burpees",
        "mountain climbers" to "exercise_mountain_climbers",
        "crunches" to "exercise_crunches",
        "leg raises" to "exercise_crunches",
        "bicycle crunches" to "exercise_bicycle_crunches",
        "high knees" to "exercise_high_knees",
        "glute bridge" to "exercise_glute_bridge",
        "glute bridges" to "exercise_glute_bridge",
        "wall sit" to "exercise_wall_sit",
        "shoulder taps" to "exercise_shoulder_taps",
        "plank shoulder taps" to "exercise_shoulder_taps",
        "tricep dips" to "exercise_tricep_dips",
        "bicep curls" to "exercise_arm_circles",
        "superman" to "exercise_superman",
        "side plank" to "exercise_side_plank",
        "russian twists" to "exercise_russian_twists",
        "step ups" to "exercise_step_ups",
        "calf raises" to "exercise_calf_raises",
        "arm circles" to "exercise_arm_circles",
    )

    fun imageResourceName(exerciseName: String, storedName: String = ""): String =
        exerciseImages[exerciseName.normalized()]
            ?: storedName.sanitizedResourceName().takeIf { it.isNotBlank() }
            ?: PLACEHOLDER

    fun videoResourceName(exerciseName: String): String =
        "video_${imageResourceName(exerciseName).removePrefix("exercise_")}"

    fun drawableId(context: Context, exercise: Exercise): Int =
        drawableId(context, imageResourceName(exercise.name, exercise.imageResName))

    fun drawableId(context: Context, resourceName: String): Int =
        context.resources.getIdentifier(resourceName.sanitizedResourceName(), "drawable", context.packageName)
            .takeIf { it != 0 }
            ?: R.drawable.exercise_placeholder

    fun rawVideoId(context: Context, exercise: Exercise): Int? =
        exercise.localVideoName
            .sanitizedResourceName()
            .takeIf { it.isNotBlank() }
            ?.let { context.resources.getIdentifier(it, "raw", context.packageName) }
            ?.takeIf { it != 0 }

    fun planImageResourceName(plan: WorkoutPlan): String = when (plan.category) {
        "Chest" -> "exercise_push_ups"
        "Legs" -> "exercise_squats"
        "Arms" -> "exercise_arm_circles"
        "Abs" -> "exercise_plank"
        "Cardio" -> "exercise_jumping_jacks"
        else -> "exercise_mountain_climbers"
    }

    fun illustrationDescription(exerciseName: String): String =
        "$exerciseName exercise illustration"

    fun videoPlaceholderDescription(exerciseName: String): String =
        "Video guide placeholder for $exerciseName"

    private fun String.normalized(): String =
        lowercase().replace(Regex("[^a-z0-9]+"), " ").trim()

    private fun String.sanitizedResourceName(): String =
        lowercase().replace(Regex("[^a-z0-9_]+"), "_").trim('_')
}
