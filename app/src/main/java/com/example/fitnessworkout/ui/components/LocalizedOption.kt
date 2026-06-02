package com.example.fitnessworkout.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R

@Composable
fun localizedOption(value: String): String {
    val resource = when (value) {
        "Metric" -> R.string.metric
        "Imperial" -> R.string.imperial
        "Beginner" -> R.string.beginner
        "Intermediate" -> R.string.intermediate
        "Advanced" -> R.string.advanced
        "Lose Weight" -> R.string.lose_weight
        "Build Muscle" -> R.string.build_muscle
        "Stay Fit" -> R.string.stay_fit
        "Improve Stamina" -> R.string.improve_stamina
        "No Equipment" -> R.string.no_equipment
        "Dumbbells" -> R.string.dumbbells
        "Resistance Band" -> R.string.resistance_band
        "Gym" -> R.string.gym
        "Balanced" -> R.string.balanced
        "Strength" -> R.string.strength
        "Cardio" -> R.string.cardio
        "Mobility" -> R.string.mobility
        "HIIT" -> R.string.hiit
        "Vegetarian" -> R.string.vegetarian
        "Vegan" -> R.string.vegan
        "Halal-friendly" -> R.string.halal_friendly
        "Gluten-free" -> R.string.gluten_free
        "Dairy-free" -> R.string.dairy_free
        "Keto" -> R.string.keto
        "Home" -> R.string.home
        "Office" -> R.string.office
        "Outdoor" -> R.string.outdoor
        "Apartment / no jumping" -> R.string.apartment_no_jumping
        "Full Body" -> R.string.full_body
        "Chest" -> R.string.chest
        "Back" -> R.string.back_body
        "Legs" -> R.string.legs
        "Shoulders" -> R.string.shoulders
        "Arms" -> R.string.arms
        "Abs" -> R.string.abs
        "Monday" -> R.string.monday
        "Tuesday" -> R.string.tuesday
        "Wednesday" -> R.string.wednesday
        "Thursday" -> R.string.thursday
        "Friday" -> R.string.friday
        "Saturday" -> R.string.saturday
        "Sunday" -> R.string.sunday
        "English" -> R.string.english
        "Hindi" -> R.string.hindi
        "Spanish" -> R.string.spanish
        "French" -> R.string.french
        "Arabic" -> R.string.arabic
        "United States" -> R.string.country_us
        "India" -> R.string.country_india
        "United Kingdom" -> R.string.country_uk
        "Canada" -> R.string.country_canada
        "Australia" -> R.string.country_australia
        "Germany" -> R.string.country_germany
        "France" -> R.string.country_france
        "Spain" -> R.string.country_spain
        "Brazil" -> R.string.country_brazil
        "United Arab Emirates" -> R.string.country_uae
        "Saudi Arabia" -> R.string.country_saudi
        else -> null
    }
    return resource?.let { stringResource(it) } ?: value
}
