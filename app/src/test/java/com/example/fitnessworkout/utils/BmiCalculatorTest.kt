package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class BmiCalculatorTest {
    @Test fun calculatesMetricAndConvertedImperialBmi() {
        assertEquals(22.86f, FitnessCalculations.bmi(70f, 175f), 0.02f)
        assertEquals(22.86f, FitnessCalculations.bmi(Units.lbsToKg(154.324f), Units.inchesToCm(68.8976f)), 0.02f)
    }

    @Test fun rejectsZeroHeightAndNegativeWeight() {
        assertEquals(0f, FitnessCalculations.bmi(70f, 0f), 0f)
        assertEquals(0f, FitnessCalculations.bmi(-1f, 175f), 0f)
    }
}
