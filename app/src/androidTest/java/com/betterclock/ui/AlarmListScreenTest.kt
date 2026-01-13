package com.betterclock.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.betterclock.data.model.Alarm
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType
import com.betterclock.ui.screens.AlarmListScreen
import com.betterclock.ui.theme.BetterClockTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for AlarmListScreen
 */
@RunWith(AndroidJUnit4::class)
class AlarmListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptyState_displaysCorrectMessage() {
        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = emptyList(),
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("No alarms yet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap the button below to create your first alarm")
            .assertIsDisplayed()
    }

    @Test
    fun alarmList_displaysAlarms() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 8, minute = 0, label = "Morning"),
            createTestAlarm(id = 2, hour = 14, minute = 30, label = "Afternoon")
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("8:00 AM").assertIsDisplayed()
        composeTestRule.onNodeWithText("Morning").assertIsDisplayed()
        composeTestRule.onNodeWithText("2:30 PM").assertIsDisplayed()
        composeTestRule.onNodeWithText("Afternoon").assertIsDisplayed()
    }

    @Test
    fun alarmList_displays24HourFormat() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 14, minute = 30)
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = true,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("14:30").assertIsDisplayed()
    }

    @Test
    fun fab_hasCorrectContentDescription() {
        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = emptyList(),
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add new alarm").assertExists()
    }

    @Test
    fun settingsButton_hasCorrectContentDescription() {
        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = emptyList(),
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Open settings").assertExists()
    }

    @Test
    fun alarmCard_displaysRepeatInfo() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 8, minute = 0, repeatDays = 62) // Weekdays
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Weekdays").assertIsDisplayed()
    }

    @Test
    fun alarmSwitch_isAccessible() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 8, minute = 0, isEnabled = true)
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Disable alarm").assertExists()
    }

    @Test
    fun fab_clickAction_triggersCallback() {
        var addClicked = false
        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = emptyList(),
                    use24HourFormat = false,
                    onAddAlarm = { addClicked = true },
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add new alarm").performClick()
        assert(addClicked)
    }

    @Test
    fun settingsButton_clickAction_triggersCallback() {
        var settingsClicked = false
        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = emptyList(),
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = { settingsClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Open settings").performClick()
        assert(settingsClicked)
    }

    @Test
    fun alarmToggle_triggersCallback() {
        var toggledAlarmId: Long? = null
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 8, minute = 0, isEnabled = true)
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = { toggledAlarmId = it.id },
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Disable alarm").performClick()
        assert(toggledAlarmId == 1L)
    }

    @Test
    fun disabledAlarm_showsEnableSwitch() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 8, minute = 0, isEnabled = false)
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Enable alarm").assertExists()
    }

    @Test
    fun alarmWithChallenge_displaysLabel() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 8, minute = 0, challengeEnabled = true, label = "Work")
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Work").assertIsDisplayed()
    }

    @Test
    fun multipleAlarms_allDisplayed() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 6, minute = 0, label = "Early"),
            createTestAlarm(id = 2, hour = 8, minute = 30, label = "Morning"),
            createTestAlarm(id = 3, hour = 12, minute = 0, label = "Noon")
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Early").assertIsDisplayed()
        composeTestRule.onNodeWithText("Morning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Noon").assertIsDisplayed()
    }

    @Test
    fun alarmWithWeekendRepeat_displaysCorrectly() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 10, minute = 0, repeatDays = 65) // Weekends
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Weekends").assertIsDisplayed()
    }

    @Test
    fun alarmWithEveryDayRepeat_displaysCorrectly() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 7, minute = 0, repeatDays = 127) // Every day
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Every day").assertIsDisplayed()
    }

    @Test
    fun alarmAt_midnight_displaysCorrectly() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 0, minute = 0)
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("12:00 AM").assertIsDisplayed()
    }

    @Test
    fun alarmAt_noon_displaysCorrectly() {
        val alarms = listOf(
            createTestAlarm(id = 1, hour = 12, minute = 0)
        )

        composeTestRule.setContent {
            BetterClockTheme {
                AlarmListScreen(
                    alarms = alarms,
                    use24HourFormat = false,
                    onAddAlarm = {},
                    onEditAlarm = {},
                    onToggleAlarm = {},
                    onDeleteAlarm = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("12:00 PM").assertIsDisplayed()
    }

    private fun createTestAlarm(
        id: Long = 1,
        hour: Int = 8,
        minute: Int = 0,
        isEnabled: Boolean = true,
        label: String = "",
        challengeEnabled: Boolean = false,
        repeatDays: Int = 0
    ) = Alarm(
        id = id,
        hour = hour,
        minute = minute,
        isEnabled = isEnabled,
        label = label,
        challengeEnabled = challengeEnabled,
        challengeType = ChallengeType.MATH,
        challengeDifficulty = ChallengeDifficulty.MEDIUM,
        repeatDays = repeatDays
    )
}
