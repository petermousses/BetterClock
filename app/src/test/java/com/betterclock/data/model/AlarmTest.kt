package com.betterclock.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Alarm model
 */
class AlarmTest {

    @Test
    fun `getFormattedTime returns correct 24-hour format`() {
        val alarm = createAlarm(hour = 14, minute = 30)
        assertEquals("14:30", alarm.getFormattedTime(true))

        val midnightAlarm = createAlarm(hour = 0, minute = 0)
        assertEquals("00:00", midnightAlarm.getFormattedTime(true))

        val morningAlarm = createAlarm(hour = 9, minute = 5)
        assertEquals("09:05", morningAlarm.getFormattedTime(true))
    }

    @Test
    fun `getFormattedTime returns correct 12-hour format`() {
        val afternoonAlarm = createAlarm(hour = 14, minute = 30)
        assertEquals("2:30 PM", afternoonAlarm.getFormattedTime(false))

        val midnightAlarm = createAlarm(hour = 0, minute = 0)
        assertEquals("12:00 AM", midnightAlarm.getFormattedTime(false))

        val noonAlarm = createAlarm(hour = 12, minute = 0)
        assertEquals("12:00 PM", noonAlarm.getFormattedTime(false))

        val morningAlarm = createAlarm(hour = 9, minute = 5)
        assertEquals("9:05 AM", morningAlarm.getFormattedTime(false))

        val eveningAlarm = createAlarm(hour = 23, minute = 59)
        assertEquals("11:59 PM", eveningAlarm.getFormattedTime(false))
    }

    @Test
    fun `isOneTime returns true when repeatDays is 0`() {
        val oneTimeAlarm = createAlarm(repeatDays = 0)
        assertTrue(oneTimeAlarm.isOneTime())

        val repeatingAlarm = createAlarm(repeatDays = 62) // Weekdays
        assertFalse(repeatingAlarm.isOneTime())
    }

    @Test
    fun `repeatsOnDay correctly identifies repeat days`() {
        // Weekdays only (Mon-Fri) = 2+4+8+16+32 = 62
        val weekdayAlarm = createAlarm(repeatDays = 62)

        assertFalse(weekdayAlarm.repeatsOnDay(0)) // Sunday
        assertTrue(weekdayAlarm.repeatsOnDay(1))  // Monday
        assertTrue(weekdayAlarm.repeatsOnDay(2))  // Tuesday
        assertTrue(weekdayAlarm.repeatsOnDay(3))  // Wednesday
        assertTrue(weekdayAlarm.repeatsOnDay(4))  // Thursday
        assertTrue(weekdayAlarm.repeatsOnDay(5))  // Friday
        assertFalse(weekdayAlarm.repeatsOnDay(6)) // Saturday
    }

    @Test
    fun `repeatsOnDay returns false for one-time alarm`() {
        val oneTimeAlarm = createAlarm(repeatDays = 0)

        for (day in 0..6) {
            assertFalse(oneTimeAlarm.repeatsOnDay(day))
        }
    }

    @Test
    fun `getRepeatDescription returns correct descriptions`() {
        assertEquals("One time", createAlarm(repeatDays = 0).getRepeatDescription())
        assertEquals("Every day", createAlarm(repeatDays = 127).getRepeatDescription())
        assertEquals("Weekdays", createAlarm(repeatDays = 62).getRepeatDescription())
        assertEquals("Weekends", createAlarm(repeatDays = 65).getRepeatDescription())

        // Monday and Wednesday = 2 + 8 = 10
        assertEquals("Mon, Wed", createAlarm(repeatDays = 10).getRepeatDescription())

        // Sunday only = 1
        assertEquals("Sun", createAlarm(repeatDays = 1).getRepeatDescription())
    }

    @Test
    fun `alarm equality works correctly`() {
        val alarm1 = createAlarm(id = 1, hour = 8, minute = 30)
        val alarm2 = createAlarm(id = 1, hour = 8, minute = 30)
        val alarm3 = createAlarm(id = 2, hour = 8, minute = 30)

        assertEquals(alarm1, alarm2)
        assertNotEquals(alarm1, alarm3)
    }

    @Test
    fun `alarm copy works correctly`() {
        val original = createAlarm(
            hour = 8,
            minute = 30,
            label = "Wake up",
            isEnabled = true,
            challengeEnabled = true
        )

        val modified = original.copy(hour = 9, label = "New label")

        assertEquals(9, modified.hour)
        assertEquals("New label", modified.label)
        assertEquals(30, modified.minute) // Unchanged
        assertTrue(modified.isEnabled) // Unchanged
        assertTrue(modified.challengeEnabled) // Unchanged
    }

    @Test
    fun `default alarm values are correct`() {
        val alarm = Alarm(hour = 8, minute = 0)

        assertTrue(alarm.isEnabled)
        assertEquals("", alarm.label)
        assertFalse(alarm.challengeEnabled)
        assertEquals(ChallengeType.MATH, alarm.challengeType)
        assertEquals(ChallengeDifficulty.MEDIUM, alarm.challengeDifficulty)
        assertEquals(5, alarm.challengeTimeoutMinutes)
        assertTrue(alarm.vibrationEnabled)
        assertEquals(10, alarm.snoozeDurationMinutes)
        assertEquals(0, alarm.snoozeCount)
        assertEquals(0, alarm.repeatDays)
    }

    private fun createAlarm(
        id: Long = 1,
        hour: Int = 8,
        minute: Int = 0,
        isEnabled: Boolean = true,
        label: String = "",
        challengeEnabled: Boolean = false,
        challengeType: ChallengeType = ChallengeType.MATH,
        challengeDifficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM,
        repeatDays: Int = 0
    ) = Alarm(
        id = id,
        hour = hour,
        minute = minute,
        isEnabled = isEnabled,
        label = label,
        challengeEnabled = challengeEnabled,
        challengeType = challengeType,
        challengeDifficulty = challengeDifficulty,
        repeatDays = repeatDays
    )
}
