package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class WaterIntakeCalculatorTest {
    @Test fun calculatesMetricAndConvertedImperialWaterGoal() {
        assertEquals(2450, FitnessCalculations.waterIntakeMl(70f))
        assertEquals(2450, FitnessCalculations.waterIntakeMl(Units.lbsToKg(154.3234f)))
    }

    @Test fun rejectsInvalidWeight() {
        assertEquals(0, FitnessCalculations.waterIntakeMl(0f))
        assertEquals(0, FitnessCalculations.waterIntakeMl(-1f))
    }
}
