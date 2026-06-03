package com.example.fitnessworkout.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PremiumAccessTest {
    private val dayMillis = 24L * 60L * 60L * 1000L

    @Test fun premiumContentIsLockedOnlyForFreeUsers() {
        assertTrue(FitnessCalculations.isPremiumLocked(premiumOnly = true, isPremiumUser = false))
        assertFalse(FitnessCalculations.isPremiumLocked(premiumOnly = true, isPremiumUser = true))
    }

    @Test fun beginnerFreeWorkoutRemainsAvailable() {
        assertFalse(FitnessCalculations.isPremiumLocked(premiumOnly = false, isPremiumUser = false))
    }

    @Test fun firstMonthTrialKeepsPremiumFeaturesAvailableForThirtyDays() {
        val start = 1_000_000L

        assertTrue(PremiumTrial.isActive(start, start + 29L * dayMillis))
        assertFalse(PremiumTrial.isActive(start, start + 30L * dayMillis))
    }

    @Test fun firstMonthTrialRoundsRemainingDaysSafely() {
        val start = 1_000_000L

        assertTrue(PremiumTrial.daysRemaining(start, start + 29L * dayMillis) == 1)
        assertTrue(PremiumTrial.daysRemaining(start, start + 30L * dayMillis) == 0)
    }
}
