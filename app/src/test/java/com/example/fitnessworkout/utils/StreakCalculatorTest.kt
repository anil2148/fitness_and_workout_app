package com.example.fitnessworkout.utils

import java.time.LocalDate
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Test

class StreakCalculatorTest {
    private val today = LocalDate.of(2026, 6, 2)

    @Test fun emptyHistoryHasNoStreak() {
        assertEquals(0, FitnessCalculations.streak(emptyList(), today))
    }

    @Test fun todayOnlyHasOneDayStreak() {
        assertEquals(1, FitnessCalculations.streak(listOf(timestamp(today)), today))
    }

    @Test fun consecutiveDaysAreCounted() {
        assertEquals(3, FitnessCalculations.streak(listOf(timestamp(today), timestamp(today.minusDays(1)), timestamp(today.minusDays(2))), today))
    }

    @Test fun missedDayStopsTheStreak() {
        assertEquals(1, FitnessCalculations.streak(listOf(timestamp(today), timestamp(today.minusDays(2))), today))
    }

    private fun timestamp(date: LocalDate): Long =
        date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
