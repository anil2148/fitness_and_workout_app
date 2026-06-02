package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseMediaTest {
    @Test fun mapsRequiredExerciseIllustrations() {
        val expected = mapOf(
            "Push Ups" to "exercise_push_ups",
            "Squats" to "exercise_squats",
            "Lunges" to "exercise_lunges",
            "Plank" to "exercise_plank",
            "Jumping Jacks" to "exercise_jumping_jacks",
            "Burpees" to "exercise_burpees",
            "Mountain Climbers" to "exercise_mountain_climbers",
            "Crunches" to "exercise_crunches",
            "Bicycle Crunches" to "exercise_bicycle_crunches",
            "High Knees" to "exercise_high_knees",
            "Glute Bridge" to "exercise_glute_bridge",
            "Wall Sit" to "exercise_wall_sit",
            "Shoulder Taps" to "exercise_shoulder_taps",
            "Tricep Dips" to "exercise_tricep_dips",
            "Superman" to "exercise_superman",
            "Side Plank" to "exercise_side_plank",
            "Russian Twists" to "exercise_russian_twists",
            "Step Ups" to "exercise_step_ups",
            "Calf Raises" to "exercise_calf_raises",
            "Arm Circles" to "exercise_arm_circles",
        )

        expected.forEach { (exercise, drawable) ->
            assertEquals(drawable, ExerciseMedia.imageResourceName(exercise))
        }
    }

    @Test fun safelyFallsBackWhenNoImageNameExists() {
        assertEquals("exercise_placeholder", ExerciseMedia.imageResourceName("Unknown exercise"))
        assertEquals("custom_owned_asset", ExerciseMedia.imageResourceName("Unknown exercise", "Custom Owned Asset"))
    }

    @Test fun derivesStableOptionalVideoNames() {
        assertEquals("video_push_ups", ExerciseMedia.videoResourceName("Push Ups"))
        assertEquals("video_plank", ExerciseMedia.videoResourceName("Plank"))
    }
}
