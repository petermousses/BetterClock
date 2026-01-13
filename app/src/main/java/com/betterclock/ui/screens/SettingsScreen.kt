package com.betterclock.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.repository.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    snoozeDuration: Int,
    challengesEnabledGlobally: Boolean,
    defaultChallengeDifficulty: ChallengeDifficulty,
    vibrationEnabled: Boolean,
    use24HourFormat: Boolean,
    confirmStopEnabled: Boolean,
    onSnoozeDurationChange: (Int) -> Unit,
    onChallengesEnabledChange: (Boolean) -> Unit,
    onDifficultyChange: (ChallengeDifficulty) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    onTimeFormatChange: (Boolean) -> Unit,
    onConfirmStopChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics {
                            contentDescription = "Go back"
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Alarm Settings Section
            Text(
                text = "Alarm Settings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Snooze Duration
            SettingsSliderItem(
                title = "Snooze Duration",
                value = snoozeDuration,
                valueRange = SettingsRepository.MIN_SNOOZE_DURATION.toFloat()..SettingsRepository.MAX_SNOOZE_DURATION.toFloat(),
                valueLabel = "$snoozeDuration minutes",
                onValueChange = { onSnoozeDurationChange(it.toInt()) }
            )

            // Vibration
            SettingsSwitchItem(
                title = "Vibration",
                subtitle = "Vibrate when alarm sounds",
                checked = vibrationEnabled,
                onCheckedChange = onVibrationChange
            )

            // Confirm Stop
            SettingsSwitchItem(
                title = "Confirm Stop",
                subtitle = "Show confirmation dialog when stopping alarms",
                checked = confirmStopEnabled,
                onCheckedChange = onConfirmStopChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Challenge Settings Section
            Text(
                text = "Challenge Settings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Global Enable
            SettingsSwitchItem(
                title = "Enable Challenges",
                subtitle = "Require challenges to snooze/stop alarms globally",
                checked = challengesEnabledGlobally,
                onCheckedChange = onChallengesEnabledChange
            )

            // Default Difficulty
            Text(
                text = "Default Difficulty",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ChallengeDifficulty.entries.forEachIndexed { index, difficulty ->
                    SegmentedButton(
                        selected = defaultChallengeDifficulty == difficulty,
                        onClick = { onDifficultyChange(difficulty) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ChallengeDifficulty.entries.size
                        )
                    ) {
                        Text(difficulty.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display Settings Section
            Text(
                text = "Display Settings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 24-hour format
            SettingsSwitchItem(
                title = "24-Hour Format",
                subtitle = "Display time in 24-hour format",
                checked = use24HourFormat,
                onCheckedChange = onTimeFormatChange
            )
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.semantics {
                    contentDescription = "$title ${if (checked) "enabled" else "disabled"}"
                }
            )
        }
    }
}

@Composable
private fun SettingsSliderItem(
    title: String,
    value: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    valueLabel: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = valueLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = value.toFloat(),
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = ((valueRange.endInclusive - valueRange.start) / 5).toInt() - 1,
                modifier = Modifier.semantics {
                    contentDescription = "$title: $valueLabel"
                }
            )
        }
    }
}
