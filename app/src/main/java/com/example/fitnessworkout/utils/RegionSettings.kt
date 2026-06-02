package com.example.fitnessworkout.utils

data class CountryOption(
    val code: String,
    val label: String,
    val currencyCode: String,
    val unitSystem: String,
)

object RegionSettings {
    val supportedCountries = listOf(
        CountryOption("US", "United States", "USD", "Imperial"),
        CountryOption("IN", "India", "INR", "Metric"),
        CountryOption("GB", "United Kingdom", "GBP", "Imperial"),
        CountryOption("CA", "Canada", "CAD", "Metric"),
        CountryOption("AU", "Australia", "AUD", "Metric"),
        CountryOption("DE", "Germany", "EUR", "Metric"),
        CountryOption("FR", "France", "EUR", "Metric"),
        CountryOption("ES", "Spain", "EUR", "Metric"),
        CountryOption("BR", "Brazil", "BRL", "Metric"),
        CountryOption("AE", "United Arab Emirates", "AED", "Metric"),
        CountryOption("SA", "Saudi Arabia", "SAR", "Metric"),
    )

    val supportedCurrencyCodes = supportedCountries.map { it.currencyCode }.distinct()

    fun country(codeOrLabel: String): CountryOption =
        supportedCountries.firstOrNull {
            it.code.equals(codeOrLabel, ignoreCase = true) ||
                it.label.equals(codeOrLabel, ignoreCase = true)
        } ?: supportedCountries.first()

    fun safeCountryCode(code: String): String = country(code).code

    fun safeCurrencyCode(code: String): String =
        supportedCurrencyCodes.firstOrNull { it.equals(code, ignoreCase = true) } ?: "USD"

    fun currencyForCountry(codeOrLabel: String): String = country(codeOrLabel).currencyCode

    fun unitSystemForCountry(codeOrLabel: String): String = country(codeOrLabel).unitSystem
}
