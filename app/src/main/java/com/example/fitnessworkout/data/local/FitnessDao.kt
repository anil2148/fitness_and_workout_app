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
import com.example.fitnessworkout.data.model.CustomWorkoutPlan
import com.example.fitnessworkout.data.model.HealthMetric
import com.example.fitnessworkout.data.model.PremiumStatus
import com.example.fitnessworkout.data.model.ReminderSettings
import com.example.fitnessworkout.data.model.WaterLog
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

    @Query("SELECT * FROM premium_status WHERE id = 1")
    fun observePremium(): Flow<PremiumStatus?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPremium(status: PremiumStatus)

    @Query("SELECT * FROM water_logs WHERE date = :date")
    fun observeWater(date: String): Flow<WaterLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWater(log: WaterLog)

    @Query("SELECT * FROM health_metrics WHERE id = 1")
    fun observeHealthMetric(): Flow<HealthMetric?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHealthMetric(metric: HealthMetric)

    @Query("SELECT * FROM reminder_settings WHERE id = 1")
    fun observeReminders(): Flow<ReminderSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReminders(settings: ReminderSettings)

    @Query("SELECT * FROM custom_workout_plans ORDER BY id DESC")
    fun observeCustomPlans(): Flow<List<CustomWorkoutPlan>>

    @Insert
    suspend fun insertCustomPlan(plan: CustomWorkoutPlan)

    @Query("SELECT COUNT(*) FROM workout_plans")
    suspend fun planCount(): Int

    @Transaction
    suspend fun insertPlanWithExercises(plan: WorkoutPlan, exercises: List<Exercise>) {
        val planId = insertPlan(plan).toInt()
        insertExercises(exercises.map { it.copy(planId = planId) })
    }
}
