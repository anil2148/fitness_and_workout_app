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
import com.example.fitnessworkout.data.model.Achievement
import com.example.fitnessworkout.data.model.BodyMeasurement
import com.example.fitnessworkout.data.model.FitnessTestResult
import com.example.fitnessworkout.data.model.ProgressPhoto
import com.example.fitnessworkout.data.model.ShareableWorkoutSummary
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.data.model.CommunityPost
import com.example.fitnessworkout.data.model.RecoveryLog
import com.example.fitnessworkout.data.model.SafetyAcknowledgement
import com.example.fitnessworkout.data.model.AppAnnouncement
import com.example.fitnessworkout.data.model.SupportMessage
import com.example.fitnessworkout.data.model.SkippedWorkout
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

    @Query("SELECT * FROM exercises ORDER BY id")
    fun observeAllExercises(): Flow<List<Exercise>>

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

    @Query("SELECT * FROM body_measurements ORDER BY recordedAt DESC")
    fun observeMeasurements(): Flow<List<BodyMeasurement>>

    @Insert suspend fun insertMeasurement(item: BodyMeasurement)

    @Query("SELECT * FROM progress_photos ORDER BY createdAt DESC")
    fun observePhotos(): Flow<List<ProgressPhoto>>

    @Insert suspend fun insertPhoto(item: ProgressPhoto)
    @Query("DELETE FROM progress_photos WHERE id = :id") suspend fun deletePhoto(id: Int)

    @Query("SELECT * FROM achievements ORDER BY unlockedAt DESC")
    fun observeAchievements(): Flow<List<Achievement>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAchievement(item: Achievement)

    @Query("SELECT * FROM fitness_test_results ORDER BY recordedAt DESC")
    fun observeFitnessTests(): Flow<List<FitnessTestResult>>

    @Insert suspend fun insertFitnessTest(item: FitnessTestResult)

    @Query("SELECT * FROM shareable_workout_summaries ORDER BY completedAt DESC")
    fun observeShareSummaries(): Flow<List<ShareableWorkoutSummary>>

    @Insert suspend fun insertShareSummary(item: ShareableWorkoutSummary)

    @Query("DELETE FROM user_profile") suspend fun deleteUsers()
    @Query("DELETE FROM completed_workouts") suspend fun deleteWorkouts()
    @Query("DELETE FROM body_measurements") suspend fun deleteMeasurements()
    @Query("DELETE FROM progress_photos") suspend fun deletePhotos()
    @Query("DELETE FROM achievements") suspend fun deleteAchievements()
    @Query("DELETE FROM fitness_test_results") suspend fun deleteFitnessTests()
    @Query("DELETE FROM shareable_workout_summaries") suspend fun deleteShareSummaries()
    @Query("DELETE FROM water_logs") suspend fun deleteWaterLogs()
    @Query("DELETE FROM health_metrics") suspend fun deleteHealthMetrics()
    @Query("DELETE FROM reminder_settings") suspend fun deleteReminderSettings()
    @Query("DELETE FROM custom_workout_plans") suspend fun deleteCustomPlans()
    @Query("DELETE FROM premium_status") suspend fun deletePremiumStatus()

    @Query("SELECT * FROM app_settings WHERE id = 1") fun observeSettings(): Flow<AppSettings?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSettings(item: AppSettings)
    @Query("SELECT * FROM community_posts ORDER BY id DESC") fun observeCommunityPosts(): Flow<List<CommunityPost>>
    @Insert suspend fun insertCommunityPosts(items: List<CommunityPost>)
    @Query("SELECT COUNT(*) FROM community_posts") suspend fun communityPostCount(): Int
    @Query("SELECT * FROM recovery_logs ORDER BY id DESC") fun observeRecovery(): Flow<List<RecoveryLog>>
    @Insert suspend fun insertRecovery(item: RecoveryLog)
    @Query("DELETE FROM app_settings") suspend fun deleteSettings()
    @Query("DELETE FROM recovery_logs") suspend fun deleteRecovery()

    @Query("SELECT * FROM safety_acknowledgements WHERE id = 1") fun observeSafetyAcknowledgement(): Flow<SafetyAcknowledgement?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSafetyAcknowledgement(item: SafetyAcknowledgement)
    @Query("SELECT * FROM app_announcements WHERE active = 1 ORDER BY id DESC") fun observeAnnouncements(): Flow<List<AppAnnouncement>>
    @Insert suspend fun insertAnnouncements(items: List<AppAnnouncement>)
    @Query("SELECT COUNT(*) FROM app_announcements") suspend fun announcementCount(): Int
    @Query("SELECT * FROM support_messages ORDER BY createdAt DESC") fun observeSupportMessages(): Flow<List<SupportMessage>>
    @Insert suspend fun insertSupportMessage(item: SupportMessage)
    @Query("SELECT * FROM skipped_workouts ORDER BY skippedAt DESC") fun observeSkippedWorkouts(): Flow<List<SkippedWorkout>>
    @Insert suspend fun insertSkippedWorkout(item: SkippedWorkout)
    @Query("DELETE FROM safety_acknowledgements") suspend fun deleteSafetyAcknowledgements()
    @Query("DELETE FROM support_messages") suspend fun deleteSupportMessages()
    @Query("DELETE FROM skipped_workouts") suspend fun deleteSkippedWorkouts()

    @Query("SELECT COUNT(*) FROM workout_plans")
    suspend fun planCount(): Int

    @Transaction
    suspend fun insertPlanWithExercises(plan: WorkoutPlan, exercises: List<Exercise>) {
        val planId = insertPlan(plan).toInt()
        insertExercises(exercises.map { it.copy(planId = planId) })
    }
}
