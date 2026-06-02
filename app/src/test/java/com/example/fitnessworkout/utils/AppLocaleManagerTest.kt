package com.example.fitnessworkout.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLocaleManagerTest {
    @Test fun defaultsUnknownLanguageToEnglish() {
        assertEquals("en", AppLocaleManager.safeLanguageCode(""))
        assertEquals("en", AppLocaleManager.safeLanguageCode("unknown"))
        assertEquals("English", AppLocaleManager.languageName("unknown"))
    }

    @Test fun mapsSupportedLanguageNamesAndCodes() {
        assertEquals("hi", AppLocaleManager.languageCode("Hindi"))
        assertEquals("es", AppLocaleManager.languageCode("Spanish"))
        assertEquals("fr", AppLocaleManager.languageCode("French"))
        assertEquals("ar", AppLocaleManager.languageCode("Arabic"))
        assertEquals("Arabic", AppLocaleManager.languageName("ar"))
    }
}
