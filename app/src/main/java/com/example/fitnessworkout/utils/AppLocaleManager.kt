package com.example.fitnessworkout.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.widget.Toast
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.local.AppPreferences
import java.util.Locale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

data class AppLanguage(val code: String, val label: String)

object AppLocaleManager {
    val supportedLanguages = listOf(
        AppLanguage("en", "English"),
        AppLanguage("hi", "Hindi"),
        AppLanguage("es", "Spanish"),
        AppLanguage("fr", "French"),
        AppLanguage("ar", "Arabic"),
    )

    fun wrap(context: Context): Context = wrap(context, persistedLanguageCode(context))

    fun wrap(context: Context, languageCode: String): Context {
        val locale = Locale.forLanguageTag(safeLanguageCode(languageCode))
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    fun persistedLanguageCode(context: Context): String =
        runCatching {
            runBlocking { AppPreferences(context.applicationContext).languageCode.first() }
        }.getOrDefault("en").let(::safeLanguageCode)

    suspend fun persistLanguageCode(context: Context, languageCode: String) {
        AppPreferences(context.applicationContext).setLanguageCode(safeLanguageCode(languageCode))
    }

    fun languageName(languageCode: String): String =
        supportedLanguages.firstOrNull { it.code == safeLanguageCode(languageCode) }?.label ?: "English"

    fun languageCode(languageName: String): String =
        supportedLanguages.firstOrNull { it.label.equals(languageName, ignoreCase = true) }?.code ?: "en"

    fun restartUi(context: Context) {
        Toast.makeText(context, context.getString(R.string.language_updated_restarting), Toast.LENGTH_SHORT).show()
        context.findActivity()?.recreate()
    }

    fun safeLanguageCode(languageCode: String): String =
        languageCode.takeIf { code -> supportedLanguages.any { it.code == code } } ?: "en"

    private tailrec fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
