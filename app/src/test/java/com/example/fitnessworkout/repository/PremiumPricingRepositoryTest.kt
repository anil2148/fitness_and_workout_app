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

        assertTrue(offers.any { it.offerId == "monthly-intro-099" && it.originalPriceText != null })
        assertTrue(offers.any { it.offerId == "yearly-50-off" && it.discountText == "50% off yearly plan" })
        assertTrue(offers.any { it.offerId == "yearly-free-trial-7-days" && it.trialText == "7 days free" })
        assertTrue(offers.any { it.offerId == "new-year-offer" && it.offerBadge == "New Year Offer" })
    }

    @Test fun selectsBasePlanOrPromotionalOffer() {
        val repository = PremiumPricingRepository()
        assertNotNull(repository.getBestValuePlan())

        repository.selectPlan("premium_monthly", "monthly-intro-099")

        assertEquals("monthly-intro-099", repository.getSelectedPlan()?.offerId)
    }
}
