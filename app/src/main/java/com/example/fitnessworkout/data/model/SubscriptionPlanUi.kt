package com.example.fitnessworkout.data.model

/**
 * Mock Premium pricing. Production values and formatted prices should come from Google Play
 * ProductDetails, never from Compose code.
 */
data class SubscriptionPlanUi(
    val productId: String,
    val basePlanId: String,
    val offerId: String? = null,
    val title: String,
    val description: String,
    val priceAmount: Double,
    val currencyCode: String,
    val billingPeriodText: String,
    val originalPriceAmount: Double? = null,
    val discountText: String? = null,
    val trialText: String? = null,
    val offerBadge: String? = null,
    val isBestValue: Boolean = false,
    val isLifetime: Boolean = false,
    val isSelected: Boolean = false,
)
