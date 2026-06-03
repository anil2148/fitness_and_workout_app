package com.example.fitnessworkout.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PremiumPricingRepositoryTest {
    @Test fun exposesMonthlyYearlyAndLifetimePlans() {
        val plans = PremiumPricingRepository().getSubscriptionPlans()

        assertTrue(plans.any { it.productId == "premium_monthly" })
        assertTrue(plans.any { it.productId == "premium_yearly" })
        assertTrue(plans.any { it.productId == "premium_lifetime" && it.isLifetime })
    }

    @Test fun exposesFlexiblePromotionalOffers() {
        val offers = PremiumPricingRepository().getPromotionalPlans()

        assertTrue(offers.any { it.offerId == "monthly-intro-099" && it.originalPriceAmount != null })
        assertTrue(offers.any { it.offerId == "yearly-50-off" && it.discountText == "yearly_50_off" })
        assertTrue(offers.any { it.offerId == "yearly-free-trial-7-days" && it.trialText == "seven_days_free" })
        assertTrue(offers.any { it.offerId == "new-year-offer" && it.offerBadge == "new_year_offer" })
    }

    @Test fun usesRegionalNumericAmountsAndCurrencyCode() {
        val indiaPlans = PremiumPricingRepository("INR").getSubscriptionPlans()
        val monthly = indiaPlans.first { it.productId == "premium_monthly" }

        assertEquals("INR", monthly.currencyCode)
        assertEquals(99.0, monthly.priceAmount, 0.0)
    }

    @Test fun unsupportedCurrencyFallsBackToUsd() {
        val monthly = PremiumPricingRepository("NOT-A-CURRENCY").getSubscriptionPlans()
            .first { it.productId == "premium_monthly" }

        assertEquals("USD", monthly.currencyCode)
        assertEquals(2.99, monthly.priceAmount, 0.0)
    }

    @Test fun selectsBasePlanOrPromotionalOffer() {
        val repository = PremiumPricingRepository()
        assertNotNull(repository.getBestValuePlan())

        repository.selectPlan("premium_monthly", "monthly-intro-099")

        assertEquals("monthly-intro-099", repository.getSelectedPlan()?.offerId)
    }
}
