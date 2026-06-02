package com.example.fitnessworkout.utils

object Units {
    fun defaultSystem(country: String) = RegionSettings.unitSystemForCountry(country)
    fun weight(kg: Float, system: String) = if (system == "Imperial") "${"%.1f".format(kg * 2.20462f)} lbs" else "${"%.1f".format(kg)} kg"
    fun length(cm: Float, system: String) = if (system == "Imperial") "${"%.1f".format(cm / 2.54f)} in" else "${"%.1f".format(cm)} cm"
    fun water(ml: Int, system: String) = if (system == "Imperial") "${"%.1f".format(ml / 29.5735f)} oz" else "$ml ml"
    fun inputWeight(value: Float, system: String) = if (system == "Imperial") value / 2.20462f else value
    fun inputLength(value: Float, system: String) = if (system == "Imperial") value * 2.54f else value
}
