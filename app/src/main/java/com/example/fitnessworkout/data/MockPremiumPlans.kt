package com.example.fitnessworkout.data

import com.example.fitnessworkout.data.model.PricingDisplay
import com.example.fitnessworkout.data.model.SubscriptionPlanUi

/**
 * Developer-only mock pricing configuration.
 *
 * Change local preview pricing and promotional copy here, not in Compose screens. Google Play
 * ProductDetails will replace this object when the production billing client is connected.
 */
object MockPremiumPlans {
    val subscriptionPlans = listOf(
        SubscriptionPlanUi(
            productId = "premium_monthly",
            basePlanId = "monthly-base",
            title = "Monthly Premium",
            description = "Flexible monthly access to Premium tracking and local previews.",
            priceText = "$2.99",
            billingPeriodText = "/ month",
        ),
        SubscriptionPlanUi(
            productId = "premium_yearly",
            basePlanId = "yearly-base",
            title = "Yearly Premium",
            description = "A full year of Premium access with the strongest standard savings.",
            priceText = "$19.99",
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
            priceText = "$29.99",
            billingPeriodText = " one-time",
            offerBadge = "Lifetime",
            isLifetime = true,
        ),
    )

    val promotionalPlans = listOf(
        SubscriptionPlanUi(
            productId = "premium_monthly",
            basePlanId = "monthly-base",
            offerId = "monthly-intro-099",
            title = "Monthly Intro Offer",
            description = "Introductory pricing for the first month, then the standard monthly price.",
            priceText = "$0.99",
            billingPeriodText = " first month",
            originalPriceText = "$2.99",
            discountText = "First month $0.99",
            offerBadge = "Intro Offer",
        ),
        SubscriptionPlanUi(
            productId = "premium_yearly",
            basePlanId = "yearly-base",
            offerId = "yearly-50-off",
            title = "Yearly Limited-Time Offer",
            description = "Promotional yearly pricing configured for a future Play offer.",
            priceText = "$9.99",
            billingPeriodText = "/ year",
            originalPriceText = "$19.99",
            discountText = "50% off yearly plan",
            offerBadge = "Limited-Time Discount",
        ),
        SubscriptionPlanUi(
            productId = "premium_yearly",
            basePlanId = "yearly-base",
            offerId = "yearly-free-trial-7-days",
            title = "Yearly Free Trial",
            description = "Try Premium locally before a future paid yearly subscription begins.",
            priceText = "$19.99",
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
            priceText = "$14.99",
            billingPeriodText = "/ year",
            originalPriceText = "$19.99",
            discountText = "Festival savings",
            offerBadge = "New Year Offer",
        ),
    )

    const val referralDiscountText = "Referral discount placeholder"
    const val promoCodeText = "Promo code placeholder"

    val regionalPricing = listOf(
        PricingDisplay("USD", "$2.99", "$19.99", "$29.99"),
        PricingDisplay("INR", "₹249", "₹1,699", "₹2,499"),
        PricingDisplay("EUR", "€2.99", "€19.99", "€29.99"),
        PricingDisplay("GBP", "£2.49", "£17.99", "£26.99"),
        PricingDisplay("BRL", "R$14.90", "R$99.90", "R$149.90"),
    )
}
