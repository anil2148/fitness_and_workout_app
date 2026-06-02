package com.example.fitnessworkout.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("fitness_preferences")

/**
 * DataStore is reserved for lightweight app preferences. Fitness history and
 * health records stay in Room so offline data has one structured source.
 */
class AppPreferences(private val context: Context) {
    private val privacyAccepted = booleanPreferencesKey("privacy_accepted")
    val hasAcceptedPrivacy = context.dataStore.data.map { it[privacyAccepted] ?: false }

    suspend fun setPrivacyAccepted(accepted: Boolean) {
        context.dataStore.edit { it[privacyAccepted] = accepted }
    }
}
