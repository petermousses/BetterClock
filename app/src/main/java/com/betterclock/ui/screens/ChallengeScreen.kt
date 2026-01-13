package com.betterclock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.betterclock.viewmodel.ChallengeUiState

@Composable
fun ChallengeScreen(
    uiState: ChallengeUiState,
    onAnswerChange: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onSnooze: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alarm = uiState.alarm
    val challenge = uiState.challenge

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Alarm Time Display
            if (alarm != null) {
                Text(
                    text = alarm.getFormattedTime(false),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                if (alarm.label.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = alarm.label,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Challenge Section
            if (challenge != null && !uiState.canSnooze) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Solve to dismiss",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = challenge.getDisplayQuestion(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = uiState.userAnswer,
                            onValueChange = onAnswerChange,
                            label = { Text("Your Answer") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = "Enter your answer"
                                },
                            singleLine = true,
                            isError = uiState.showError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onSubmitAnswer() }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                errorContainerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        )

                        if (uiState.showError) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Attempts: ${uiState.attemptCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onSubmitAnswer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .semantics {
                                    contentDescription = "Submit answer"
                                }
                        ) {
                            Text("Submit")
                        }

                        // Timeout display
                        if (uiState.isTimeoutEnabled && uiState.remainingTimeSeconds > 0) {
                            Spacer(modifier = Modifier.height(16.dp))
                            val minutes = uiState.remainingTimeSeconds / 60
                            val seconds = uiState.remainingTimeSeconds % 60
                            Text(
                                text = "Time remaining: ${String.format("%02d:%02d", minutes, seconds)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (uiState.remainingTimeSeconds < 60) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                }
                            )
                        }
                    }
                }
            } else if (uiState.isAnswerCorrect == true) {
                // Success message
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Correct!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Action Buttons
            if (uiState.canSnooze || uiState.canStop) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.canSnooze) {
                        OutlinedButton(
                            onClick = onSnooze,
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .semantics {
                                    contentDescription = "Snooze alarm for ${alarm?.snoozeDurationMinutes ?: 10} minutes"
                                }
                        ) {
                            Text("Snooze\n${alarm?.snoozeDurationMinutes ?: 10} min")
                        }
                    }

                    if (uiState.canStop) {
                        Button(
                            onClick = onStop,
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .semantics {
                                    contentDescription = "Stop and dismiss alarm"
                                },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Stop")
                        }
                    }
                }
            }
        }
    }
}
