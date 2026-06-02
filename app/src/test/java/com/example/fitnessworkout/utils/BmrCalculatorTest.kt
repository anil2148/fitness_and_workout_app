package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class BmrCalculatorTest {
    @Test fun calculatesSupportedBmrFormula() {
        assertEquals(1648.75f, FitnessCalculations.bmr(70f, 175f, 30), 0.01f)
    }

    @Test fun rejectsInvalidAgeHeightAndWeight() {
        assertEquals(0f, FitnessCalculations.bmr(70f, 175f, 0), 0f)
        assertEquals(0f, FitnessCalculations.bmr(70f, 0f, 30), 0f)
        assertEquals(0f, FitnessCalculations.bmr(-1f, 175f, 30), 0f)
    }
}
