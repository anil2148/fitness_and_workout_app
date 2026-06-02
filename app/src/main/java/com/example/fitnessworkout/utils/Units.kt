package com.example.fitnessworkout.utils

import kotlin.math.roundToInt

object Units {
    fun defaultSystem(country: String) = RegionSettings.unitSystemForCountry(country)
    fun kgToLbs(kg: Float) = kg * 2.20462f
    fun lbsToKg(lbs: Float) = lbs / 2.20462f
    fun cmToInches(cm: Float) = cm / 2.54f
    fun inchesToCm(inches: Float) = inches * 2.54f
    fun mlToOz(ml: Int) = ml / 29.5735f
    fun ozToMl(oz: Float) = (oz * 29.5735f).roundToInt()
    fun weight(kg: Float, system: String) = if (system == "Imperial") "${"%.1f".format(kgToLbs(kg))} lbs" else "${"%.1f".format(kg)} kg"
    fun length(cm: Float, system: String) = if (system == "Imperial") "${"%.1f".format(cmToInches(cm))} in" else "${"%.1f".format(cm)} cm"
    fun water(ml: Int, system: String) = if (system == "Imperial") "${"%.1f".format(mlToOz(ml))} oz" else "$ml ml"
    fun inputWeight(value: Float, system: String) = if (system == "Imperial") lbsToKg(value) else value
    fun inputLength(value: Float, system: String) = if (system == "Imperial") inchesToCm(value) else value
    fun inputWater(value: Float, system: String) = if (system == "Imperial") ozToMl(value) else value.toInt()
}
