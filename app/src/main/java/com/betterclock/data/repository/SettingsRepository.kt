package com.betterclock.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.betterclock.data.model.ChallengeDifficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Repository for app settings using DataStore
 */
class SettingsRepository(private val context: Context) {

    private object Keys {
        val SNOOZE_DURATION = intPreferencesKey("snooze_duration")
        val CHALLENGES_ENABLED_GLOBALLY = booleanPreferencesKey("challenges_enabled_globally")
        val DEFAULT_CHALLENGE_DIFFICULTY = stringPreferencesKey("default_challenge_difficulty")
        val DEFAULT_SOUND_URI = stringPreferencesKey("default_sound_uri")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val USE_24_HOUR_FORMAT = booleanPreferencesKey("use_24_hour_format")
        val CONFIRM_STOP_ENABLED = booleanPreferencesKey("confirm_stop_enabled")
    }

    // Default values
    companion object {
        const val DEFAULT_SNOOZE_DURATION = 10
        const val MIN_SNOOZE_DURATION = 5
        const val MAX_SNOOZE_DURATION = 60
    }

    val snoozeDuration: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.SNOOZE_DURATION] ?: DEFAULT_SNOOZE_DURATION
    }

    val challengesEnabledGlobally: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.CHALLENGES_ENABLED_GLOBALLY] ?: true
    }

    val defaultChallengeDifficulty: Flow<ChallengeDifficulty> = context.dataStore.data.map { preferences ->
        val difficultyName = preferences[Keys.DEFAULT_CHALLENGE_DIFFICULTY]
        if (difficultyName != null) {
            try {
                ChallengeDifficulty.valueOf(difficultyName)
            } catch (e: IllegalArgumentException) {
                ChallengeDifficulty.MEDIUM
            }
        } else {
            ChallengeDifficulty.MEDIUM
        }
    }

    val defaultSoundUri: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[Keys.DEFAULT_SOUND_URI]
    }

    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.VIBRATION_ENABLED] ?: true
    }

    val use24HourFormat: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.USE_24_HOUR_FORMAT] ?: false
    }

    val confirmStopEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.CONFIRM_STOP_ENABLED] ?: false
    }

    suspend fun setSnoozeDuration(duration: Int) {
        val clampedDuration = duration.coerceIn(MIN_SNOOZE_DURATION, MAX_SNOOZE_DURATION)
        context.dataStore.edit { preferences ->
            preferences[Keys.SNOOZE_DURATION] = clampedDuration
        }
    }

    suspend fun setChallengesEnabledGlobally(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CHALLENGES_ENABLED_GLOBALLY] = enabled
        }
    }

    suspend fun setDefaultChallengeDifficulty(difficulty: ChallengeDifficulty) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DEFAULT_CHALLENGE_DIFFICULTY] = difficulty.name
        }
    }

    suspend fun setDefaultSoundUri(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[Keys.DEFAULT_SOUND_URI] = uri
            } else {
                preferences.remove(Keys.DEFAULT_SOUND_URI)
            }
        }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.VIBRATION_ENABLED] = enabled
        }
    }

    suspend fun setUse24HourFormat(use24Hour: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USE_24_HOUR_FORMAT] = use24Hour
        }
    }

    suspend fun setConfirmStopEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CONFIRM_STOP_ENABLED] = enabled
        }
    }
}
