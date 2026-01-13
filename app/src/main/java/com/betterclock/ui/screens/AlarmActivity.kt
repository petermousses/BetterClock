package com.betterclock.ui.screens

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.betterclock.alarm.AlarmScheduler
import com.betterclock.alarm.AlarmService
import com.betterclock.ui.theme.BetterClockTheme
import com.betterclock.viewmodel.ChallengeViewModel

/**
 * Activity displayed when an alarm triggers.
 * Shows the challenge screen in full-screen mode that cannot be dismissed.
 */
class AlarmActivity : ComponentActivity() {

    private val viewModel: ChallengeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupFullScreenMode()

        val alarmId = intent.getLongExtra(AlarmScheduler.EXTRA_ALARM_ID, -1)
        if (alarmId != -1L) {
            viewModel.loadAlarm(alarmId)
        }

        setContent {
            BetterClockTheme {
                val uiState by viewModel.uiState.collectAsState()

                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = viewModel::updateUserAnswer,
                    onSubmitAnswer = viewModel::submitAnswer,
                    onSnooze = { handleSnooze(alarmId) },
                    onStop = { handleStop(alarmId) }
                )
            }
        }
    }

    private fun setupFullScreenMode() {
        // Show on lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun handleSnooze(alarmId: Long) {
        val serviceIntent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_SNOOZE
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
        }
        startService(serviceIntent)
        finish()
    }

    private fun handleStop(alarmId: Long) {
        val serviceIntent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
        }
        startService(serviceIntent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Prevent back button from dismissing the alarm
        // User must complete challenge or use snooze/stop buttons
    }
}
