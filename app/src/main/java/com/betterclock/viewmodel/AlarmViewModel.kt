package com.betterclock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterclock.alarm.AlarmScheduler
import com.betterclock.data.AppDatabase
import com.betterclock.data.model.Alarm
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType
import com.betterclock.data.repository.AlarmRepository
import com.betterclock.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing alarms
 */
class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = AlarmRepository(database.alarmDao())
    private val settingsRepository = SettingsRepository(application)
    private val alarmScheduler = AlarmScheduler(application)

    val alarms: StateFlow<List<Alarm>> = repository.allAlarms
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val use24HourFormat: StateFlow<Boolean> = settingsRepository.use24HourFormat
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _selectedAlarm = MutableStateFlow<Alarm?>(null)
    val selectedAlarm: StateFlow<Alarm?> = _selectedAlarm.asStateFlow()

    private val _showAddEditSheet = MutableStateFlow(false)
    val showAddEditSheet: StateFlow<Boolean> = _showAddEditSheet.asStateFlow()

    fun selectAlarm(alarm: Alarm?) {
        _selectedAlarm.value = alarm
        _showAddEditSheet.value = alarm != null
    }

    fun showAddAlarmSheet() {
        _selectedAlarm.value = null
        _showAddEditSheet.value = true
    }

    fun hideAddEditSheet() {
        _showAddEditSheet.value = false
        _selectedAlarm.value = null
    }

    fun createAlarm(
        hour: Int,
        minute: Int,
        label: String = "",
        challengeEnabled: Boolean = false,
        challengeType: ChallengeType = ChallengeType.MATH,
        challengeDifficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM,
        repeatDays: Int = 0
    ) {
        viewModelScope.launch {
            val alarm = Alarm(
                hour = hour,
                minute = minute,
                label = label,
                isEnabled = true,
                challengeEnabled = challengeEnabled,
                challengeType = challengeType,
                challengeDifficulty = challengeDifficulty,
                repeatDays = repeatDays
            )
            val id = repository.insertAlarm(alarm)
            val savedAlarm = repository.getAlarmById(id)
            savedAlarm?.let {
                alarmScheduler.scheduleAlarm(it)
            }
            hideAddEditSheet()
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            repository.updateAlarm(alarm)
            if (alarm.isEnabled) {
                alarmScheduler.scheduleAlarm(alarm)
            } else {
                alarmScheduler.cancelAlarm(alarm.id)
            }
            hideAddEditSheet()
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmScheduler.cancelAlarm(alarm.id)
            repository.deleteAlarm(alarm)
            hideAddEditSheet()
        }
    }

    fun toggleAlarmEnabled(alarm: Alarm) {
        viewModelScope.launch {
            val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
            repository.updateAlarm(updatedAlarm)
            if (updatedAlarm.isEnabled) {
                alarmScheduler.scheduleAlarm(updatedAlarm)
            } else {
                alarmScheduler.cancelAlarm(updatedAlarm.id)
            }
        }
    }

    fun getAlarmById(id: Long): Flow<Alarm?> {
        return repository.getAlarmByIdFlow(id)
    }
}
