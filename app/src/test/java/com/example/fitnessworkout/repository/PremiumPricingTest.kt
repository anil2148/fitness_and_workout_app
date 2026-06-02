package com.example.fitnessworkout.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class PremiumPricingTest {
    @Test fun returnsExpectedRegionalStandardPrices() {
        assertStandardPrices("USD", 2.99, 19.99, 29.99)
        assertStandardPrices("INR", 99.0, 999.0, 1499.0)
        assertStandardPrices("EUR", 2.99, 19.99, 29.99)
        assertStandardPrices("GBP", 2.49, 17.99, 24.99)
        assertStandardPrices("BRL", 9.99, 79.99, 119.99)
        assertStandardPrices("AED", 10.99, 74.99, 109.99)
        assertStandardPrices("SAR", 10.99, 74.99, 109.99)
    }

    @Test fun invalidCurrencyFallsBackToUsdPricing() {
        assertStandardPrices("invalid", 2.99, 19.99, 29.99, expectedCurrency = "USD")
    }

    private fun assertStandardPrices(
        currency: String,
        monthly: Double,
        yearly: Double,
        lifetime: Double,
        expectedCurrency: String = currency,
    ) {
        val plans = PremiumPricingRepository(currency).getSubscriptionPlans()
        assertEquals(expectedCurrency, plans.first().currencyCode)
        assertEquals(monthly, plans.first { it.productId == "premium_monthly" }.priceAmount, 0.0)
        assertEquals(yearly, plans.first { it.productId == "premium_yearly" }.priceAmount, 0.0)
        assertEquals(lifetime, plans.first { it.productId == "premium_lifetime" }.priceAmount, 0.0)
    }
}
