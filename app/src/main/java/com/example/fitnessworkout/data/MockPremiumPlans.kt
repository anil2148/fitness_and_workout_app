package com.example.fitnessworkout.data

import com.example.fitnessworkout.data.model.PricingDisplay
import com.example.fitnessworkout.data.model.SubscriptionPlanUi
import com.example.fitnessworkout.utils.RegionSettings

/**
 * Developer-only mock pricing configuration.
 *
 * Change local preview pricing and promotional copy here, not in Compose screens. Google Play
 * ProductDetails will replace this object when the production billing client is connected.
 */
object MockPremiumPlans {
    private data class RegionalAmounts(
        val monthly: Double,
        val yearly: Double,
        val lifetime: Double,
        val monthlyIntro: Double,
        val yearlyHalfPrice: Double,
        val yearlyFestival: Double,
    )

    private val regionalAmounts = mapOf(
        "USD" to RegionalAmounts(2.99, 19.99, 29.99, 0.99, 9.99, 14.99),
        "INR" to RegionalAmounts(99.0, 999.0, 1499.0, 29.0, 499.0, 749.0),
        "EUR" to RegionalAmounts(2.99, 19.99, 29.99, 0.99, 9.99, 14.99),
        "GBP" to RegionalAmounts(2.49, 17.99, 24.99, 0.79, 8.99, 13.49),
        "CAD" to RegionalAmounts(3.99, 26.99, 39.99, 1.29, 13.49, 19.99),
        "AUD" to RegionalAmounts(4.49, 29.99, 44.99, 1.49, 14.99, 22.49),
        "BRL" to RegionalAmounts(9.99, 79.99, 119.99, 3.49, 39.99, 59.99),
        "AED" to RegionalAmounts(10.99, 74.99, 109.99, 3.69, 37.49, 54.99),
        "SAR" to RegionalAmounts(10.99, 74.99, 109.99, 3.69, 37.49, 54.99),
    )

    fun subscriptionPlans(currencyCode: String = "USD"): List<SubscriptionPlanUi> {
        val currency = RegionSettings.safeCurrencyCode(currencyCode)
        val amounts = amounts(currency)
        return listOf(
            SubscriptionPlanUi(
                productId = "premium_monthly",
                basePlanId = "monthly-base",
                title = "Monthly Premium",
                description = "Flexible monthly access to Premium tracking and local previews.",
                priceAmount = amounts.monthly,
                currencyCode = currency,
                billingPeriodText = "/ month",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                title = "Yearly Premium",
                description = "A full year of Premium access with the strongest standard savings.",
                priceAmount = amounts.yearly,
                currencyCode = currency,
                billingPeriodText = "/ year",
                discountText = "Save 44% vs monthly",
                offerBadge = "Best Value",
                isBestValue = true,
                isSelected = true,
            ),
            SubscriptionPlanUi(
                productId = "premium_lifetime",
                basePlanId = "lifetime-base",
                title = "Lifetime Premium",
                description = "One-time mock purchase option for lifetime Premium access.",
                priceAmount = amounts.lifetime,
                currencyCode = currency,
                billingPeriodText = " one-time",
                offerBadge = "Lifetime",
                isLifetime = true,
            ),
        )
    }

    fun promotionalPlans(currencyCode: String = "USD"): List<SubscriptionPlanUi> {
        val currency = RegionSettings.safeCurrencyCode(currencyCode)
        val amounts = amounts(currency)
        return listOf(
            SubscriptionPlanUi(
                productId = "premium_monthly",
                basePlanId = "monthly-base",
                offerId = "monthly-intro-099",
                title = "Monthly Intro Offer",
                description = "Introductory pricing for the first month, then the standard monthly price.",
                priceAmount = amounts.monthlyIntro,
                currencyCode = currency,
                billingPeriodText = " first month",
                originalPriceAmount = amounts.monthly,
                discountText = "First month offer",
                offerBadge = "Intro Offer",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                offerId = "yearly-50-off",
                title = "Yearly Limited-Time Offer",
                description = "Promotional yearly pricing configured for a future Play offer.",
                priceAmount = amounts.yearlyHalfPrice,
                currencyCode = currency,
                billingPeriodText = "/ year",
                originalPriceAmount = amounts.yearly,
                discountText = "50% off yearly plan",
                offerBadge = "Limited-Time Discount",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                offerId = "yearly-free-trial-7-days",
                title = "Yearly Free Trial",
                description = "Try Premium locally before a future paid yearly subscription begins.",
                priceAmount = amounts.yearly,
                currencyCode = currency,
                billingPeriodText = "/ year after trial",
                trialText = "7 days free",
                offerBadge = "Free Trial",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                offerId = "new-year-offer",
                title = "Yearly Festival Offer",
                description = "Seasonal promotional placeholder controlled from the mock configuration.",
                priceAmount = amounts.yearlyFestival,
                currencyCode = currency,
                billingPeriodText = "/ year",
                originalPriceAmount = amounts.yearly,
                discountText = "Festival savings",
                offerBadge = "New Year Offer",
            ),
        )
    }

    const val referralDiscountText = "Referral discount placeholder"
    const val promoCodeText = "Promo code placeholder"

    val regionalPricing: List<PricingDisplay> = RegionSettings.supportedCurrencyCodes.map { currency ->
        val amounts = amounts(currency)
        PricingDisplay(currency, amounts.monthly, amounts.yearly, amounts.lifetime)
    }

    private fun amounts(currencyCode: String): RegionalAmounts =
        regionalAmounts[currencyCode] ?: regionalAmounts.getValue("USD")
}
