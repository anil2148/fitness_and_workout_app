package com.example.fitnessworkout.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object FitnessCalculations {
    fun bmi(weightKg: Float, heightCm: Float): Float =
        if (weightKg > 0f && heightCm > 0f) {
            val meters = heightCm / 100f
            weightKg / (meters * meters)
        } else 0f

    fun bmr(weightKg: Float, heightCm: Float, age: Int): Float =
        if (weightKg > 0f && heightCm > 0f && age > 0) 10f * weightKg + 6.25f * heightCm - 5f * age + 5f else 0f

    fun calorieNeeds(bmr: Float, activityMultiplier: Float): Float =
        if (bmr > 0f && activityMultiplier > 0f) bmr * activityMultiplier else 0f

    fun streak(timestamps: List<Long>, today: LocalDate = LocalDate.now()): Int {
        val dates = timestamps.map { it.toDate() }.distinct().sortedDescending()
        if (dates.isEmpty()) return 0
        var expected = today
        if (dates.first() != expected) expected = expected.minusDays(1)
        var streak = 0
        for (date in dates) {
            if (date == expected) {
                streak++
                expected = expected.minusDays(1)
            } else if (date < expected) break
        }
        return streak
    }

    fun isPremiumLocked(premiumOnly: Boolean, isPremiumUser: Boolean): Boolean =
        premiumOnly && !isPremiumUser

    private fun Long.toDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}
