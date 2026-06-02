package com.example.fitnessworkout.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitnessworkout.data.model.CompletedWorkout
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.CustomWorkoutPlan
import com.example.fitnessworkout.data.model.HealthMetric
import com.example.fitnessworkout.data.model.PremiumStatus
import com.example.fitnessworkout.data.model.ReminderSettings
import com.example.fitnessworkout.data.model.WaterLog

@Database(
    entities = [UserProfile::class, WorkoutPlan::class, Exercise::class, CompletedWorkout::class,
        PremiumStatus::class, WaterLog::class, HealthMetric::class, ReminderSettings::class, CustomWorkoutPlan::class],
    version = 2,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile private var instance: FitnessDatabase? = null

        fun getInstance(context: Context): FitnessDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_workout.db"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
    }
}
