# Localization and Regional Settings Audit

## Supported Locales

The app supports English (`en`), Hindi (`hi`), Spanish (`es`), French (`fr`), and Arabic (`ar`). `AppLocaleManager` persists the selected language code in DataStore, wraps the base activity context before Compose loads, recreates the activity after a change, and applies RTL layout direction for Arabic. Unknown language codes safely fall back to English.

## Supported Regions

| Country | Currency | Default Units |
|---|---|---|
| United States | USD | Imperial |
| India | INR | Metric |
| United Kingdom | GBP | Imperial |
| Canada | CAD | Metric |
| Australia | AUD | Metric |
| Germany, France, Spain | EUR | Metric |
| Brazil | BRL | Metric |
| United Arab Emirates | AED | Metric |
| Saudi Arabia | SAR | Metric |

Language, country, currency, and unit system are separate settings. Selecting a country supplies sensible defaults; currency and units can then be overridden independently. Unknown countries fall back to USD for currency mapping and Metric for direct unit mapping.

## Automated Results

Run:

```bash
python3 scripts/verify_strings.py
python3 scripts/scan_hardcoded_strings.py
```

Audit result:

- Default pack: 589 translatable keys.
- Hindi, Spanish, French, and Arabic: 589 matching keys each.
- Missing keys: none.
- Extra keys: none.
- Duplicate keys: none.
- Direct Compose hardcoded-string scan: 0 findings.
- Premium pricing cards render localized titles, descriptions, billing periods, discounts, trial labels, and badges from string resources. `MockPremiumPlans` keeps product IDs, offer IDs, numeric amounts, and non-visible display tokens only.

The scan is intentionally informational because seeded workout descriptions, mock announcement copy, internal rule-engine explanations, and localized-option fallback values are structured local content rather than direct Compose labels. They remain safe English fallback content until product translations are supplied.

## Adding Content

To add a string:

1. Add the key to `app/src/main/res/values/strings.xml`.
2. Add the same key to `values-hi`, `values-es`, `values-fr`, and `values-ar`.
3. Run `python3 scripts/verify_strings.py`.

To add a language:

1. Add a `values-<locale>/strings.xml` resource pack.
2. Add the code and label to `AppLocaleManager.supportedLanguages`.
3. Extend `scripts/verify_strings.py`.
4. Test selection, restart persistence, and layout direction on a device.

To add a currency:

1. Add the country and ISO currency code to `RegionSettings`.
2. Add numeric mock Premium values to `MockPremiumPlans`.
3. Add JVM mapping, formatter, and pricing tests.
4. Replace mock prices with Google Play Billing `ProductDetails` before release.

## Manual Checks

- Select Hindi and confirm Home, Workouts, Premium, Profile, and Settings labels refresh.
- Restart and confirm Hindi remains selected.
- Switch to English and confirm visible labels refresh.
- Select Arabic and confirm the app remains stable with RTL direction.
- Select India with English UI and confirm INR Premium prices.
- Select United States with Hindi UI and confirm USD Premium prices.
- Override currency manually and confirm it changes Premium display without changing language.
