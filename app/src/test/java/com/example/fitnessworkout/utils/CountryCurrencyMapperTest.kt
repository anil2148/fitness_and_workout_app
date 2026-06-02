package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class CountryCurrencyMapperTest {
    @Test fun mapsSupportedCountriesAndFallsBackToUsd() {
        assertEquals("USD", RegionSettings.currencyForCountry("US"))
        assertEquals("INR", RegionSettings.currencyForCountry("IN"))
        assertEquals("EUR", RegionSettings.currencyForCountry("FR"))
        assertEquals("GBP", RegionSettings.currencyForCountry("GB"))
        assertEquals("USD", RegionSettings.currencyForCountry("invalid"))
    }
}
