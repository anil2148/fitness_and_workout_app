package com.example.fitnessworkout.utils

import java.time.LocalDate
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FitnessCalculationsTest {
    @Test fun bmiUsesMetricFormula() {
        assertEquals(22.86f, FitnessCalculations.bmi(70f, 175f), 0.02f)
    }

    @Test fun bmiRejectsInvalidInput() {
        assertEquals(0f, FitnessCalculations.bmi(-1f, 175f), 0f)
        assertEquals(0f, FitnessCalculations.bmi(70f, 0f), 0f)
    }

    @Test fun unitsConvertImperialInputToMetric() {
        assertEquals(1f, Units.inputWeight(2.20462f, "Imperial"), 0.001f)
        assertEquals(2.54f, Units.inputLength(1f, "Imperial"), 0.001f)
    }

    @Test fun streakCountsConsecutiveDaysIncludingYesterday() {
        val today = LocalDate.of(2026, 6, 2)
        val timestamps = listOf(today.minusDays(1), today.minusDays(2), today.minusDays(3)).map {
            it.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
        assertEquals(3, FitnessCalculations.streak(timestamps, today))
    }

    @Test fun premiumLocksOnlyPremiumContentForFreeUsers() {
        assertTrue(FitnessCalculations.isPremiumLocked(premiumOnly = true, isPremiumUser = false))
        assertFalse(FitnessCalculations.isPremiumLocked(premiumOnly = true, isPremiumUser = true))
        assertFalse(FitnessCalculations.isPremiumLocked(premiumOnly = false, isPremiumUser = false))
    }
}
