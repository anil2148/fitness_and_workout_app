package com.example.fitnessworkout.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CurrencyFormatterTest {
    @Test fun formatsSupportedCurrenciesWithoutCrashing() {
        assertTrue(CurrencyFormatter.format(2.99, "USD", "en").contains("$"))
        assertTrue(CurrencyFormatter.format(99.0, "INR", "en").contains("₹"))
        assertTrue(CurrencyFormatter.format(2.99, "EUR", "fr").contains("€"))
    }

    @Test fun invalidCurrencyFallsBackToUsd() {
        assertTrue(CurrencyFormatter.format(2.99, "invalid", "en").contains("$"))
    }

    @Test fun languageAndCurrencyRemainIndependent() {
        val hindiWithUsd = CurrencyFormatter.format(2.99, "USD", "hi")
        assertTrue(hindiWithUsd.contains("$"))
        assertFalse(hindiWithUsd.contains("₹"))

        assertTrue(CurrencyFormatter.format(99.0, RegionSettings.currencyForCountry("IN"), "en").contains("₹"))
    }
}
