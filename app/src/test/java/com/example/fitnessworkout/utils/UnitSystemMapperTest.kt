package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitSystemMapperTest {
    @Test fun mapsSupportedCountriesAndFallsBackToMetric() {
        assertEquals("Imperial", RegionSettings.unitSystemForCountry("US"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("IN"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("FR"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("invalid"))
    }
}
