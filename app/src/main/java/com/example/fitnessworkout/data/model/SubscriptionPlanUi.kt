package com.example.fitnessworkout.data.model

/**
 * Display-ready Premium pricing. Production values should come from Google Play ProductDetails,
 * never from Compose code.
 */
data class SubscriptionPlanUi(
    val productId: String,
    val basePlanId: String,
    val offerId: String? = null,
    val title: String,
    val description: String,
    val priceText: String,
    val billingPeriodText: String,
    val originalPriceText: String? = null,
    val discountText: String? = null,
    val trialText: String? = null,
    val offerBadge: String? = null,
    val isBestValue: Boolean = false,
    val isLifetime: Boolean = false,
    val isSelected: Boolean = false,
)
