package com.example.fitnessworkout

import com.example.fitnessworkout.data.MockPremiumPlans
import com.example.fitnessworkout.utils.CurrencyFormatter
import com.example.fitnessworkout.utils.RegionSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegionSettingsTest {

    @Test
    fun countryMapsToExpectedCurrency() {
        assertEquals("USD", RegionSettings.currencyForCountry("US"))
        assertEquals("INR", RegionSettings.currencyForCountry("IN"))
        assertEquals("GBP", RegionSettings.currencyForCountry("GB"))
        assertEquals("EUR", RegionSettings.currencyForCountry("FR"))
        assertEquals("AED", RegionSettings.currencyForCountry("AE"))
    }

    @Test
    fun invalidCountryFallsBackToUnitedStates() {
        val country = RegionSettings.country("UNKNOWN")
        assertEquals("US", country.code)
        assertEquals("USD", country.currencyCode)
    }

    @Test
    fun countryMapsToExpectedUnitSystem() {
        assertEquals("Imperial", RegionSettings.unitSystemForCountry("US"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("IN"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("FR"))
        assertEquals("Metric", RegionSettings.unitSystemForCountry("BR"))
    }

    @Test
    fun safeCurrencyFallsBackToUsd() {
        assertEquals("USD", RegionSettings.safeCurrencyCode("NOT_A_CURRENCY"))
        assertEquals("INR", RegionSettings.safeCurrencyCode("inr"))
    }

    @Test
    fun currencyFormatterDoesNotTieCurrencyToLanguage() {
        val hindiWithUsd = CurrencyFormatter.format(2.99, "USD", "hi")
        val englishWithInr = CurrencyFormatter.format(99.0, "INR", "en")

        assertTrue("Hindi language with USD should still show USD-style currency", hindiWithUsd.contains("$") || hindiWithUsd.contains("US"))
        assertTrue("English language with INR should still show INR-style currency", englishWithInr.contains("₹") || englishWithInr.contains("INR") || englishWithInr.contains("Rs"))
    }

    @Test
    fun premiumPricingUsesSelectedCurrency() {
        val indiaPlans = MockPremiumPlans.subscriptionPlans("INR")
        val usPlans = MockPremiumPlans.subscriptionPlans("USD")

        assertEquals("INR", indiaPlans.first().currencyCode)
        assertEquals(99.0, indiaPlans.first().priceAmount, 0.001)
        assertEquals("USD", usPlans.first().currencyCode)
        assertEquals(2.99, usPlans.first().priceAmount, 0.001)
    }
}
