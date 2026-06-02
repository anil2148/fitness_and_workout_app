package com.example.fitnessworkout.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PremiumAccessTest {
    @Test fun premiumContentIsLockedOnlyForFreeUsers() {
        assertTrue(FitnessCalculations.isPremiumLocked(premiumOnly = true, isPremiumUser = false))
        assertFalse(FitnessCalculations.isPremiumLocked(premiumOnly = true, isPremiumUser = true))
    }

    @Test fun beginnerFreeWorkoutRemainsAvailable() {
        assertFalse(FitnessCalculations.isPremiumLocked(premiumOnly = false, isPremiumUser = false))
    }
}
