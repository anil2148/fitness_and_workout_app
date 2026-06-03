package com.example.fitnessworkout.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidationTest {
    @Test fun validatesRequiredFieldsAndPositiveNumbers() {
        assertFalse(InputValidation.isRequired("  "))
        assertTrue(InputValidation.isRequired("Alex"))
        assertFalse(InputValidation.isPositiveNumber("invalid"))
        assertFalse(InputValidation.isPositiveNumber("-1"))
        assertTrue(InputValidation.isPositiveNumber("70.5"))
        assertFalse(InputValidation.isPositiveInt("0"))
        assertTrue(InputValidation.isPositiveInt("30"))
        assertFalse(InputValidation.isNonNegativeNumber("-0.1"))
        assertTrue(InputValidation.isNonNegativeNumber("0"))
        assertFalse(InputValidation.isValidEmail("alex"))
        assertTrue(InputValidation.isValidEmail("alex@example.com"))
    }
}
