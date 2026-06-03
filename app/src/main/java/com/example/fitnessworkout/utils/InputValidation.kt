package com.example.fitnessworkout.utils

object InputValidation {
    fun isRequired(value: String): Boolean = value.isNotBlank()

    fun isPositiveNumber(value: String): Boolean =
        value.toFloatOrNull()?.let { it > 0f } ?: false

    fun isPositiveInt(value: String): Boolean =
        value.toIntOrNull()?.let { it > 0 } ?: false

    fun isNonNegativeNumber(value: String): Boolean =
        value.toFloatOrNull()?.let { it >= 0f } ?: false

    fun isValidEmail(value: String): Boolean =
        value.contains("@") && value.substringAfter("@").contains(".")
}
