package com.example.fitnessworkout.repository

import com.example.fitnessworkout.data.MockPremiumPlans
import com.example.fitnessworkout.data.model.SubscriptionPlanUi

/**
 * Mock Premium catalog boundary. Compose consumes this repository instead of embedding prices.
 *
 * TODO: Query Google Play Billing ProductDetails for product IDs and regional prices.
 * TODO: Map base plan IDs, offer IDs, and eligible offer tokens from ProductDetails.
 * TODO: Launch the billing flow with the selected ProductDetails and offer token.
 * TODO: Verify purchase success before granting Premium entitlement.
 * TODO: Restore purchases and expose subscription renewal status.
 */
class PremiumPricingRepository {
    private var selectedKey = planKey(MockPremiumPlans.subscriptionPlans.first { it.isSelected })

    fun getSubscriptionPlans(): List<SubscriptionPlanUi> =
        MockPremiumPlans.subscriptionPlans.map(::withSelection)

    fun getPromotionalPlans(): List<SubscriptionPlanUi> =
        MockPremiumPlans.promotionalPlans.map(::withSelection)

    fun getBestValuePlan(): SubscriptionPlanUi? =
        getSubscriptionPlans().firstOrNull { it.isBestValue }

    fun getSelectedPlan(): SubscriptionPlanUi? =
        (getSubscriptionPlans() + getPromotionalPlans()).firstOrNull { it.isSelected }

    fun selectPlan(productId: String, offerId: String? = null) {
        selectedKey = planKey(productId, offerId)
    }

    private fun withSelection(plan: SubscriptionPlanUi): SubscriptionPlanUi =
        plan.copy(isSelected = planKey(plan) == selectedKey)

    private fun planKey(plan: SubscriptionPlanUi): String =
        planKey(plan.productId, plan.offerId)

    private fun planKey(productId: String, offerId: String?): String =
        "$productId:${offerId.orEmpty()}"
}
