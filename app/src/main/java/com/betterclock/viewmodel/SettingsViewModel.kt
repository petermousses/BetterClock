package com.betterclock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing app settings
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    val snoozeDuration: StateFlow<Int> = repository.snoozeDuration
        .stateIn(viewModelScope, SharingStarted.Lazily, SettingsRepository.DEFAULT_SNOOZE_DURATION)

    val challengesEnabledGlobally: StateFlow<Boolean> = repository.challengesEnabledGlobally
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val defaultChallengeDifficulty: StateFlow<ChallengeDifficulty> = repository.defaultChallengeDifficulty
        .stateIn(viewModelScope, SharingStarted.Lazily, ChallengeDifficulty.MEDIUM)

    val vibrationEnabled: StateFlow<Boolean> = repository.vibrationEnabled
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val use24HourFormat: StateFlow<Boolean> = repository.use24HourFormat
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val confirmStopEnabled: StateFlow<Boolean> = repository.confirmStopEnabled
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun setSnoozeDuration(duration: Int) {
        viewModelScope.launch {
            repository.setSnoozeDuration(duration)
        }
    }

    fun setChallengesEnabledGlobally(enabled: Boolean) {
        viewModelScope.launch {
            repository.setChallengesEnabledGlobally(enabled)
        }
    }

    fun setDefaultChallengeDifficulty(difficulty: ChallengeDifficulty) {
        viewModelScope.launch {
            repository.setDefaultChallengeDifficulty(difficulty)
        }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setVibrationEnabled(enabled)
        }
    }

    fun setUse24HourFormat(use24Hour: Boolean) {
        viewModelScope.launch {
            repository.setUse24HourFormat(use24Hour)
        }
    }

    fun setConfirmStopEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setConfirmStopEnabled(enabled)
        }
    }
}
