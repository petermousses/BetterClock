package com.betterclock.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.ui.screens.SettingsScreen
import com.betterclock.ui.theme.BetterClockTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for SettingsScreen
 */
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun settingsScreen_displaysAllSections() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Alarm Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Challenge Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Display Settings").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysSnoozeSlider() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Snooze Duration").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 minutes").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysVibrationToggle() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Vibration").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vibrate when alarm sounds").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysChallengeToggle() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Enable Challenges").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysDifficultyButtons() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Default Difficulty").assertIsDisplayed()
        composeTestRule.onNodeWithText("Easy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Medium").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hard").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displays24HourToggle() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("24-Hour Format").assertIsDisplayed()
        composeTestRule.onNodeWithText("Display time in 24-hour format").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_backButton_triggersCallback() {
        var backClicked = false
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = { backClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()
        assert(backClicked)
    }

    @Test
    fun settingsScreen_confirmStopToggle_visible() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = true,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Confirm Stop").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_difficultySelection_triggersCallback() {
        var selectedDifficulty: ChallengeDifficulty? = null
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = { selectedDifficulty = it },
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Hard").performClick()
        assert(selectedDifficulty == ChallengeDifficulty.HARD)
    }

    @Test
    fun settingsScreen_withDifferentSnoozeDuration_displaysCorrectValue() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 30,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.MEDIUM,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("30 minutes").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_withEasyDifficulty_showsCorrectSelection() {
        composeTestRule.setContent {
            BetterClockTheme {
                SettingsScreen(
                    snoozeDuration = 10,
                    challengesEnabledGlobally = true,
                    defaultChallengeDifficulty = ChallengeDifficulty.EASY,
                    vibrationEnabled = true,
                    use24HourFormat = false,
                    confirmStopEnabled = false,
                    onSnoozeDurationChange = {},
                    onChallengesEnabledChange = {},
                    onDifficultyChange = {},
                    onVibrationChange = {},
                    onTimeFormatChange = {},
                    onConfirmStopChange = {},
                    onNavigateBack = {}
                )
            }
        }

        // Verify difficulty buttons are present
        composeTestRule.onNodeWithText("Easy").assertIsDisplayed()
    }
}
