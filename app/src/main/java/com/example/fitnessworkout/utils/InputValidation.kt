package com.example.fitnessworkout.utils

object InputValidation {
    fun isRequired(value: String): Boolean = value.isNotBlank()

    fun isPositiveNumber(value: String): Boolean =
        value.toFloatOrNull()?.let { it > 0f } ?: false
}
