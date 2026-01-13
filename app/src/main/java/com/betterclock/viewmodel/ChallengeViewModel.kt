package com.betterclock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterclock.challenge.Challenge
import com.betterclock.challenge.ChallengeGenerator
import com.betterclock.data.AppDatabase
import com.betterclock.data.model.Alarm
import com.betterclock.data.model.ChallengeType
import com.betterclock.data.repository.AlarmRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * State for the challenge screen
 */
data class ChallengeUiState(
    val alarm: Alarm? = null,
    val challenge: Challenge? = null,
    val userAnswer: String = "",
    val attemptCount: Int = 0,
    val isAnswerCorrect: Boolean? = null,
    val showError: Boolean = false,
    val errorMessage: String = "",
    val remainingTimeSeconds: Int = 0,
    val isTimeoutEnabled: Boolean = true,
    val canSnooze: Boolean = false,
    val canStop: Boolean = false
)

/**
 * ViewModel for challenge screen during alarm trigger
 */
class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = AlarmRepository(database.alarmDao())

    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()

    private var timeoutJob: Job? = null

    fun loadAlarm(alarmId: Long) {
        viewModelScope.launch {
            val alarm = repository.getAlarmById(alarmId)
            if (alarm != null) {
                val challenge = if (alarm.challengeEnabled && alarm.challengeType != ChallengeType.NONE) {
                    ChallengeGenerator.generateChallenge(alarm.challengeType, alarm.challengeDifficulty)
                } else {
                    null
                }

                _uiState.value = ChallengeUiState(
                    alarm = alarm,
                    challenge = challenge,
                    remainingTimeSeconds = alarm.challengeTimeoutMinutes * 60,
                    isTimeoutEnabled = alarm.challengeEnabled,
                    canSnooze = !alarm.challengeEnabled,
                    canStop = !alarm.challengeEnabled
                )

                if (alarm.challengeEnabled && alarm.challengeTimeoutMinutes > 0) {
                    startTimeoutTimer()
                }
            }
        }
    }

    private fun startTimeoutTimer() {
        timeoutJob?.cancel()
        timeoutJob = viewModelScope.launch {
            while (_uiState.value.remainingTimeSeconds > 0) {
                delay(1000)
                _uiState.update { it.copy(remainingTimeSeconds = it.remainingTimeSeconds - 1) }
            }
            // Timeout reached - alarm continues, no auto-dismiss
        }
    }

    fun updateUserAnswer(answer: String) {
        _uiState.update {
            it.copy(userAnswer = answer, showError = false)
        }
    }

    fun submitAnswer() {
        val state = _uiState.value
        val challenge = state.challenge ?: return

        val isCorrect = challenge.checkAnswer(state.userAnswer)

        if (isCorrect) {
            timeoutJob?.cancel()
            _uiState.update {
                it.copy(
                    isAnswerCorrect = true,
                    canSnooze = true,
                    canStop = true,
                    showError = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    attemptCount = it.attemptCount + 1,
                    isAnswerCorrect = false,
                    showError = true,
                    errorMessage = "Incorrect answer. Try again!",
                    userAnswer = ""
                )
            }
        }
    }

    fun generateNewChallenge() {
        val alarm = _uiState.value.alarm ?: return
        if (alarm.challengeType == ChallengeType.NONE) return

        val newChallenge = ChallengeGenerator.generateChallenge(
            alarm.challengeType,
            alarm.challengeDifficulty
        )

        _uiState.update {
            it.copy(
                challenge = newChallenge,
                userAnswer = "",
                isAnswerCorrect = null,
                showError = false,
                canSnooze = false,
                canStop = false,
                remainingTimeSeconds = alarm.challengeTimeoutMinutes * 60
            )
        }

        if (alarm.challengeTimeoutMinutes > 0) {
            startTimeoutTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timeoutJob?.cancel()
    }
}
