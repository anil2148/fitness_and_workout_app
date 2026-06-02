package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {
    @Test fun convertsWeightLengthAndWaterBothWays() {
        assertEquals(2.20462f, Units.kgToLbs(1f), 0.0001f)
        assertEquals(1f, Units.lbsToKg(2.20462f), 0.0001f)
        assertEquals(1f, Units.cmToInches(2.54f), 0.0001f)
        assertEquals(2.54f, Units.inchesToCm(1f), 0.0001f)
        assertEquals(33.814f, Units.mlToOz(1000), 0.01f)
        assertEquals(1000, Units.ozToMl(33.814f))
    }
}
