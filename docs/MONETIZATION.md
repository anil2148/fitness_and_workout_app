# Premium Monetization Architecture

## Current State

Premium access remains a local mock for QA. The app does not connect to Google Play Billing, collect payment, create a purchase, or verify an entitlement. The mock unlock only stores a local Room flag so locked-screen behavior can be tested offline.

All Premium-gated features are also available locally for the first 30 days after onboarding. This first-month free entitlement is calculated from the persisted onboarding safety-acknowledgement timestamp and does not create a purchase, payment, subscription, or verified Play Billing entitlement.

Compose screens do not own price strings. `MockPremiumPlans.kt` is the developer-only preview catalog, `SubscriptionPlanUi` is the display model, and `PremiumPricingRepository` is the replacement boundary for Google Play Billing.

## Mock Catalog

Standard local preview products:

| Product ID | Base Plan ID | Purpose |
|---|---|---|
| `premium_monthly` | `monthly-base` | Normal monthly subscription |
| `premium_yearly` | `yearly-base` | Normal yearly subscription and savings display |
| `premium_lifetime` | `lifetime-base` | One-time lifetime placeholder |

Mock offer IDs:

| Offer ID | Purpose |
|---|---|
| `monthly-intro-099` | First-month introductory price |
| `yearly-50-off` | Limited-time yearly discount |
| `yearly-free-trial-7-days` | Seven-day trial strategy |
| `new-year-offer` | Seasonal or festival campaign |

Referral discounts and promo codes remain labeled placeholders. They do not alter billing or grant entitlement.

## Flexible Display Model

`SubscriptionPlanUi` supports:

- Numeric standard and discounted preview amounts
- ISO currency code
- Billing-period copy
- Optional original price with strike-through display
- Discount text
- Trial text
- Promotional badge
- Best-value state
- Lifetime-purchase state
- Selected-card state

Edit mock promotional content in `app/src/main/java/com/example/fitnessworkout/data/MockPremiumPlans.kt`. Do not edit Compose UI to change preview pricing.

## Google Play Billing Integration

When production billing is added:

1. Configure products, base plans, offers, and regional prices in Google Play Console.
2. Query `ProductDetails` for `premium_monthly`, `premium_yearly`, and the lifetime one-time product.
3. Map Play base plans, offer IDs, eligibility, and offer tokens into `SubscriptionPlanUi`.
4. Launch the billing flow with the selected `ProductDetails` and eligible offer token.
5. Verify purchase success before saving a Premium entitlement.
6. Restore purchases through Play Billing instead of the disabled placeholder.
7. Surface renewal state, cancellation state, and entitlement expiry from verified purchase data.

Google Play Console should remain the source of truth for final prices. Do not hardcode release pricing in UI code, documentation-driven logic, or local entitlement checks.

## Offer Strategy

- Free trial: use `yearly-free-trial-7-days` for eligible new subscribers.
- First month free: current app builds grant local access for the first 30 days after onboarding. When production billing is connected, replace or reconcile this local entitlement with Play Billing eligibility rules.
- Intro offer: use `monthly-intro-099` for a first-month conversion test.
- Seasonal discounts: manage `yearly-50-off` and `new-year-offer` in Play Console and show only eligible offers returned by Play.
- Promo codes and referrals: connect them to Play-supported promotions or a verified backend campaign before granting entitlement.
- Regional pricing: use localized Play `ProductDetails` formatting. The app's numeric regional preview values are mock models only. `CurrencyFormatter` applies the selected ISO currency and app language for offline previews.

## Production Checklist

- [ ] Add the Play Billing dependency and billing-client lifecycle.
- [ ] Query `ProductDetails` and eligible subscription offers.
- [ ] Pass offer tokens into billing-flow parameters.
- [ ] Verify purchases before Premium access.
- [ ] Implement restore purchases.
- [ ] Handle renewal, expiry, grace period, and cancellation state.
- [ ] Replace local mock pricing and local unlock controls for release builds.
