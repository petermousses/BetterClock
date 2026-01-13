package com.betterclock.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.betterclock.challenge.MathChallenge
import com.betterclock.challenge.MathOperator
import com.betterclock.data.model.Alarm
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType
import com.betterclock.ui.screens.ChallengeScreen
import com.betterclock.ui.theme.BetterClockTheme
import com.betterclock.viewmodel.ChallengeUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for ChallengeScreen
 */
@RunWith(AndroidJUnit4::class)
class ChallengeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun challengeScreen_displaysChallengeQuestion() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            userAnswer = "",
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("5 + 3 = ?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Solve to dismiss").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_displaysAlarmTime() {
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(hour = 8, minute = 30),
            challenge = null,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("8:30 AM").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_displaysAlarmLabel() {
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(label = "Wake up!"),
            challenge = null,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Wake up!").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_showsSnoozeAndStopButtons_whenChallengeCompleted() {
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = null,
            isAnswerCorrect = true,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Snooze alarm for 10 minutes").assertExists()
        composeTestRule.onNodeWithContentDescription("Stop and dismiss alarm").assertExists()
    }

    @Test
    fun challengeScreen_hidesButtons_whenChallengeNotCompleted() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(challengeEnabled = true),
            challenge = challenge,
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Snooze").assertDoesNotExist()
        composeTestRule.onNodeWithText("Stop").assertDoesNotExist()
    }

    @Test
    fun challengeScreen_displaysErrorMessage() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            userAnswer = "",
            showError = true,
            errorMessage = "Incorrect answer. Try again!",
            attemptCount = 1,
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Incorrect answer. Try again!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Attempts: 1").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_answerInput_hasCorrectDescription() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Enter your answer").assertExists()
    }

    @Test
    fun challengeScreen_displaysSuccessMessage_whenCorrect() {
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = null,
            isAnswerCorrect = true,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Correct!").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_snoozeButton_triggersCallback() {
        var snoozeCalled = false
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = null,
            isAnswerCorrect = true,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = { snoozeCalled = true },
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Snooze alarm for 10 minutes").performClick()
        assert(snoozeCalled)
    }

    @Test
    fun challengeScreen_stopButton_triggersCallback() {
        var stopCalled = false
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = null,
            isAnswerCorrect = true,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = { stopCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Stop and dismiss alarm").performClick()
        assert(stopCalled)
    }

    @Test
    fun challengeScreen_displaysSubtractionChallenge() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.MEDIUM,
            operand1 = 10,
            operand2 = 4,
            operator = MathOperator.SUBTRACT,
            answer = 6
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("10 - 4 = ?").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_displaysMultiplicationChallenge() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.HARD,
            operand1 = 7,
            operand2 = 8,
            operator = MathOperator.MULTIPLY,
            answer = 56
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("7 × 8 = ?").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_displaysDivisionChallenge() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.HARD,
            operand1 = 24,
            operand2 = 6,
            operator = MathOperator.DIVIDE,
            answer = 4
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("24 ÷ 6 = ?").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_multipleAttempts_displaysCorrectCount() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )

        val uiState = ChallengeUiState(
            alarm = createTestAlarm(),
            challenge = challenge,
            attemptCount = 5,
            showError = true,
            errorMessage = "Incorrect!",
            canSnooze = false,
            canStop = false
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Attempts: 5").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_alarmWithoutLabel_displaysTimeOnly() {
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(hour = 7, minute = 0, label = ""),
            challenge = null,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithText("7:00 AM").assertIsDisplayed()
    }

    @Test
    fun challengeScreen_differentSnoozeDuration_displaysCorrectly() {
        val uiState = ChallengeUiState(
            alarm = createTestAlarm(snoozeDurationMinutes = 15),
            challenge = null,
            isAnswerCorrect = true,
            canSnooze = true,
            canStop = true
        )

        composeTestRule.setContent {
            BetterClockTheme {
                ChallengeScreen(
                    uiState = uiState,
                    onAnswerChange = {},
                    onSubmitAnswer = {},
                    onSnooze = {},
                    onStop = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Snooze alarm for 15 minutes").assertExists()
    }

    private fun createTestAlarm(
        hour: Int = 8,
        minute: Int = 30,
        label: String = "",
        challengeEnabled: Boolean = true,
        snoozeDurationMinutes: Int = 10
    ) = Alarm(
        id = 1,
        hour = hour,
        minute = minute,
        label = label,
        isEnabled = true,
        challengeEnabled = challengeEnabled,
        challengeType = ChallengeType.MATH,
        challengeDifficulty = ChallengeDifficulty.MEDIUM,
        snoozeDurationMinutes = snoozeDurationMinutes
    )
}
