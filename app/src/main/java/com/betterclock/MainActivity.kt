package com.betterclock

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.betterclock.ui.screens.AlarmEditorSheet
import com.betterclock.ui.screens.AlarmListScreen
import com.betterclock.ui.screens.SettingsScreen
import com.betterclock.ui.theme.BetterClockTheme
import com.betterclock.viewmodel.AlarmViewModel
import com.betterclock.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

    private val alarmViewModel: AlarmViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()

        setContent {
            BetterClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BetterClockApp(
                        alarmViewModel = alarmViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetterClockApp(
    alarmViewModel: AlarmViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()

    val alarms by alarmViewModel.alarms.collectAsState()
    val use24HourFormat by alarmViewModel.use24HourFormat.collectAsState()
    val showAddEditSheet by alarmViewModel.showAddEditSheet.collectAsState()
    val selectedAlarm by alarmViewModel.selectedAlarm.collectAsState()

    // Settings state
    val snoozeDuration by settingsViewModel.snoozeDuration.collectAsState()
    val challengesEnabledGlobally by settingsViewModel.challengesEnabledGlobally.collectAsState()
    val defaultChallengeDifficulty by settingsViewModel.defaultChallengeDifficulty.collectAsState()
    val vibrationEnabled by settingsViewModel.vibrationEnabled.collectAsState()
    val settingsUse24HourFormat by settingsViewModel.use24HourFormat.collectAsState()
    val confirmStopEnabled by settingsViewModel.confirmStopEnabled.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    NavHost(navController = navController, startDestination = "alarms") {
        composable("alarms") {
            AlarmListScreen(
                alarms = alarms,
                use24HourFormat = use24HourFormat,
                onAddAlarm = { alarmViewModel.showAddAlarmSheet() },
                onEditAlarm = { alarm -> alarmViewModel.selectAlarm(alarm) },
                onToggleAlarm = { alarm -> alarmViewModel.toggleAlarmEnabled(alarm) },
                onDeleteAlarm = { alarm -> alarmViewModel.deleteAlarm(alarm) },
                onOpenSettings = { navController.navigate("settings") }
            )
        }

        composable("settings") {
            SettingsScreen(
                snoozeDuration = snoozeDuration,
                challengesEnabledGlobally = challengesEnabledGlobally,
                defaultChallengeDifficulty = defaultChallengeDifficulty,
                vibrationEnabled = vibrationEnabled,
                use24HourFormat = settingsUse24HourFormat,
                confirmStopEnabled = confirmStopEnabled,
                onSnoozeDurationChange = settingsViewModel::setSnoozeDuration,
                onChallengesEnabledChange = settingsViewModel::setChallengesEnabledGlobally,
                onDifficultyChange = settingsViewModel::setDefaultChallengeDifficulty,
                onVibrationChange = settingsViewModel::setVibrationEnabled,
                onTimeFormatChange = settingsViewModel::setUse24HourFormat,
                onConfirmStopChange = settingsViewModel::setConfirmStopEnabled,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }

    // Bottom Sheet for Add/Edit Alarm
    if (showAddEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { alarmViewModel.hideAddEditSheet() },
            sheetState = sheetState
        ) {
            AlarmEditorSheet(
                existingAlarm = selectedAlarm,
                onSave = { alarm ->
                    if (selectedAlarm != null) {
                        alarmViewModel.updateAlarm(alarm)
                    } else {
                        alarmViewModel.createAlarm(
                            hour = alarm.hour,
                            minute = alarm.minute,
                            label = alarm.label,
                            challengeEnabled = alarm.challengeEnabled,
                            challengeType = alarm.challengeType,
                            challengeDifficulty = alarm.challengeDifficulty,
                            repeatDays = alarm.repeatDays
                        )
                    }
                },
                onDismiss = { alarmViewModel.hideAddEditSheet() }
            )
        }
    }
}
