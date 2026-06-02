package com.example.fitnessworkout.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {
    fun format(amount: Double, currencyCode: String, languageCode: String = Locale.getDefault().language): String {
        val safeCurrencyCode = RegionSettings.safeCurrencyCode(currencyCode)
        val currency = runCatching { Currency.getInstance(safeCurrencyCode) }
            .getOrElse { Currency.getInstance("USD") }
        val locale = Locale.forLanguageTag(languageCode.ifBlank { "en" })
        return runCatching {
            NumberFormat.getCurrencyInstance(locale).apply { this.currency = currency }.format(amount)
        }.getOrElse {
            NumberFormat.getCurrencyInstance(Locale.ENGLISH).apply {
                this.currency = Currency.getInstance("USD")
            }.format(amount)
        }
    }
}
