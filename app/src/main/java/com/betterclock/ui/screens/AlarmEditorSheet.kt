package com.betterclock.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.betterclock.data.model.Alarm
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmEditorSheet(
    existingAlarm: Alarm?,
    onSave: (Alarm) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hour by remember { mutableIntStateOf(existingAlarm?.hour ?: 8) }
    var minute by remember { mutableIntStateOf(existingAlarm?.minute ?: 0) }
    var label by remember { mutableStateOf(existingAlarm?.label ?: "") }
    var challengeEnabled by remember { mutableStateOf(existingAlarm?.challengeEnabled ?: false) }
    var challengeType by remember { mutableStateOf(existingAlarm?.challengeType ?: ChallengeType.MATH) }
    var challengeDifficulty by remember { mutableStateOf(existingAlarm?.challengeDifficulty ?: ChallengeDifficulty.MEDIUM) }
    var repeatDays by remember { mutableIntStateOf(existingAlarm?.repeatDays ?: 0) }

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute
    )

    // Update hour and minute when time picker changes
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        hour = timePickerState.hour
        minute = timePickerState.minute
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (existingAlarm != null) "Edit Alarm" else "New Alarm",
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.semantics {
                    contentDescription = "Close editor"
                }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Time Picker
        TimePicker(
            state = timePickerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .semantics {
                    contentDescription = "Select alarm time"
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Label
        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text("Label (optional)") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Alarm label"
                },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Repeat Days
        Text(
            text = "Repeat",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        DaySelector(
            selectedDays = repeatDays,
            onDayToggle = { dayBit ->
                repeatDays = repeatDays xor dayBit
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Challenge Settings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enable Challenge",
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = challengeEnabled,
                onCheckedChange = { challengeEnabled = it },
                modifier = Modifier.semantics {
                    contentDescription = if (challengeEnabled) "Disable challenge" else "Enable challenge"
                }
            )
        }

        if (challengeEnabled) {
            Spacer(modifier = Modifier.height(16.dp))

            // Challenge Type
            Text(
                text = "Challenge Type",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ChallengeType.entries.filter { it != ChallengeType.NONE }.forEachIndexed { index, type ->
                    SegmentedButton(
                        selected = challengeType == type,
                        onClick = { challengeType = type },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ChallengeType.entries.size - 1
                        )
                    ) {
                        Text(type.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty
            Text(
                text = "Difficulty",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ChallengeDifficulty.entries.forEachIndexed { index, difficulty ->
                    SegmentedButton(
                        selected = challengeDifficulty == difficulty,
                        onClick = { challengeDifficulty = difficulty },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ChallengeDifficulty.entries.size
                        )
                    ) {
                        Text(difficulty.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Button(
            onClick = {
                val alarm = existingAlarm?.copy(
                    hour = hour,
                    minute = minute,
                    label = label,
                    challengeEnabled = challengeEnabled,
                    challengeType = if (challengeEnabled) challengeType else ChallengeType.NONE,
                    challengeDifficulty = challengeDifficulty,
                    repeatDays = repeatDays
                ) ?: Alarm(
                    hour = hour,
                    minute = minute,
                    label = label,
                    challengeEnabled = challengeEnabled,
                    challengeType = if (challengeEnabled) challengeType else ChallengeType.NONE,
                    challengeDifficulty = challengeDifficulty,
                    repeatDays = repeatDays
                )
                onSave(alarm)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .semantics {
                    contentDescription = "Save alarm"
                }
        ) {
            Text(if (existingAlarm != null) "Update Alarm" else "Create Alarm")
        }
    }
}

@Composable
private fun DaySelector(
    selectedDays: Int,
    onDayToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf(
        "S" to 1,   // Sunday
        "M" to 2,   // Monday
        "T" to 4,   // Tuesday
        "W" to 8,   // Wednesday
        "T" to 16,  // Thursday
        "F" to 32,  // Friday
        "S" to 64   // Saturday
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { (label, bit) ->
            val isSelected = (selectedDays and bit) != 0
            FilterChip(
                selected = isSelected,
                onClick = { onDayToggle(bit) },
                label = { Text(label) },
                modifier = Modifier.semantics {
                    contentDescription = "$label ${if (isSelected) "selected" else "not selected"}"
                }
            )
        }
    }
}
