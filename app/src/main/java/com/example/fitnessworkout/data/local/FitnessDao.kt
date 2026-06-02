package com.example.fitnessworkout.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeUser(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserProfile)

    @Query("SELECT * FROM workout_plans ORDER BY challengeDay IS NOT NULL, level, category")
    fun observePlans(): Flow<List<WorkoutPlan>>

    @Query("SELECT * FROM exercises WHERE planId = :planId ORDER BY id")
    fun observeExercises(planId: Int): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE planId = :planId ORDER BY id")
    suspend fun getExercises(planId: Int): List<Exercise>

    @Insert
    suspend fun insertPlan(plan: WorkoutPlan): Long

    @Insert
    suspend fun insertExercises(exercises: List<Exercise>)

    @Insert
    suspend fun insertCompletedWorkout(workout: CompletedWorkout)

    @Query("SELECT * FROM completed_workouts ORDER BY completedAt DESC")
    fun observeCompletedWorkouts(): Flow<List<CompletedWorkout>>

    @Query("DELETE FROM completed_workouts")
    suspend fun resetProgress()

    @Query("SELECT COUNT(*) FROM workout_plans")
    suspend fun planCount(): Int

    @Transaction
    suspend fun insertPlanWithExercises(plan: WorkoutPlan, exercises: List<Exercise>) {
        val planId = insertPlan(plan).toInt()
        insertExercises(exercises.map { it.copy(planId = planId) })
    }
}
