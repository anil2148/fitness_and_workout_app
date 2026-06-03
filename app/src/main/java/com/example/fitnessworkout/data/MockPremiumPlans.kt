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
                title = "premium_monthly",
                description = "monthly_premium_description",
                priceAmount = amounts.monthly,
                currencyCode = currency,
                billingPeriodText = "period_month",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                title = "premium_yearly",
                description = "yearly_premium_description",
                priceAmount = amounts.yearly,
                currencyCode = currency,
                billingPeriodText = "period_year",
                discountText = "save_44",
                offerBadge = "best_value",
                isBestValue = true,
                isSelected = true,
            ),
            SubscriptionPlanUi(
                productId = "premium_lifetime",
                basePlanId = "lifetime-base",
                title = "premium_lifetime",
                description = "lifetime_premium_description",
                priceAmount = amounts.lifetime,
                currencyCode = currency,
                billingPeriodText = "period_one_time",
                offerBadge = "lifetime",
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
                title = "monthly_intro_offer",
                description = "monthly_intro_description",
                priceAmount = amounts.monthlyIntro,
                currencyCode = currency,
                billingPeriodText = "period_first_month",
                originalPriceAmount = amounts.monthly,
                discountText = "first_month_offer",
                offerBadge = "intro_offer",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                offerId = "yearly-50-off",
                title = "yearly_limited_offer",
                description = "yearly_limited_description",
                priceAmount = amounts.yearlyHalfPrice,
                currencyCode = currency,
                billingPeriodText = "period_year",
                originalPriceAmount = amounts.yearly,
                discountText = "yearly_50_off",
                offerBadge = "limited_time_discount",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                offerId = "yearly-free-trial-7-days",
                title = "yearly_free_trial",
                description = "yearly_trial_description",
                priceAmount = amounts.yearly,
                currencyCode = currency,
                billingPeriodText = "period_year_after_trial",
                trialText = "seven_days_free",
                offerBadge = "free_trial",
            ),
            SubscriptionPlanUi(
                productId = "premium_yearly",
                basePlanId = "yearly-base",
                offerId = "new-year-offer",
                title = "yearly_festival_offer",
                description = "yearly_festival_description",
                priceAmount = amounts.yearlyFestival,
                currencyCode = currency,
                billingPeriodText = "period_year",
                originalPriceAmount = amounts.yearly,
                discountText = "festival_savings",
                offerBadge = "new_year_offer",
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
