package com.example.fitnessworkout.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: Int,
    val weightKg: Float,
    val heightCm: Float,
    val fitnessGoal: String
) {
    val bmi: Float get() = if (heightCm > 0) weightKg / ((heightCm / 100) * (heightCm / 100)) else 0f
}

@Entity(tableName = "workout_plans")
data class WorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val level: String,
    val category: String,
    val description: String,
    val estimatedCalories: Int,
    val durationMinutes: Int,
    val challengeDay: Int? = null
)

@Entity(
    tableName = "exercises",
    foreignKeys = [ForeignKey(
        entity = WorkoutPlan::class,
        parentColumns = ["id"],
        childColumns = ["planId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("planId")]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planId: Int,
    val name: String,
    val repsOrDuration: String,
    val sets: Int,
    val restSeconds: Int,
    val instruction: String,
    val durationSeconds: Int? = null
)

@Entity(
    tableName = "completed_workouts",
    indices = [Index("planId")]
)
data class CompletedWorkout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planId: Int,
    val planTitle: String,
    val completedAt: Long,
    val caloriesBurned: Int,
    val durationMinutes: Int
)

data class WorkoutPlanWithExercises(
    val plan: WorkoutPlan,
    val exercises: List<Exercise>
)
