package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegionSettingsTest {
    @Test fun mapsCountriesToExpectedCurrencyAndUnits() {
        assertEquals("USD", RegionSettings.currencyForCountry("US"))
        assertEquals("INR", RegionSettings.currencyForCountry("India"))
        assertEquals("GBP", RegionSettings.currencyForCountry("GB"))
        assertEquals("EUR", RegionSettings.currencyForCountry("France"))
        assertEquals("AED", RegionSettings.currencyForCountry("AE"))
        assertEquals("Imperial", RegionSettings.unitSystemForCountry("United States"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("India"))
    }

    @Test fun fallsBackToUnitedStatesForUnknownCountry() {
        assertEquals("US", RegionSettings.safeCountryCode("unknown"))
        assertEquals("USD", RegionSettings.currencyForCountry("unknown"))
    }

    @Test fun formatsLocalizedMoneyAndFallsBackToUsd() {
        assertTrue(CurrencyFormatter.format(99.0, "INR", "en").contains("₹"))
        assertTrue(CurrencyFormatter.format(2.99, "USD", "en").contains("$"))
        assertTrue(CurrencyFormatter.format(2.99, "UNKNOWN", "en").contains("$"))
    }
}
