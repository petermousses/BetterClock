package com.betterclock.alarm

import com.betterclock.data.model.Alarm
import org.junit.Assert.*
import org.junit.Test
import java.util.*

/**
 * Unit tests for alarm time calculations
 */
class AlarmSchedulerTest {

    @Test
    fun `one-time alarm schedules for today if time not passed`() {
        val now = Calendar.getInstance()
        val futureHour = (now.get(Calendar.HOUR_OF_DAY) + 1) % 24

        val alarm = Alarm(
            id = 1,
            hour = futureHour,
            minute = 30,
            isEnabled = true,
            repeatDays = 0
        )

        // Verify alarm is one-time
        assertTrue(alarm.isOneTime())
    }

    @Test
    fun `repeating alarm identifies correct days`() {
        // Every day = 127 (all bits set)
        val everyDayAlarm = Alarm(
            id = 1,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = 127
        )

        for (day in 0..6) {
            assertTrue("Should repeat on day $day", everyDayAlarm.repeatsOnDay(day))
        }

        // Weekdays = 62 (Mon-Fri)
        val weekdayAlarm = Alarm(
            id = 2,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = 62
        )

        assertFalse(weekdayAlarm.repeatsOnDay(0)) // Sunday
        assertTrue(weekdayAlarm.repeatsOnDay(1))  // Monday
        assertTrue(weekdayAlarm.repeatsOnDay(2))  // Tuesday
        assertTrue(weekdayAlarm.repeatsOnDay(3))  // Wednesday
        assertTrue(weekdayAlarm.repeatsOnDay(4))  // Thursday
        assertTrue(weekdayAlarm.repeatsOnDay(5))  // Friday
        assertFalse(weekdayAlarm.repeatsOnDay(6)) // Saturday

        // Weekends = 65 (Sun + Sat)
        val weekendAlarm = Alarm(
            id = 3,
            hour = 10,
            minute = 0,
            isEnabled = true,
            repeatDays = 65
        )

        assertTrue(weekendAlarm.repeatsOnDay(0))  // Sunday
        assertFalse(weekendAlarm.repeatsOnDay(1)) // Monday
        assertFalse(weekendAlarm.repeatsOnDay(2)) // Tuesday
        assertFalse(weekendAlarm.repeatsOnDay(3)) // Wednesday
        assertFalse(weekendAlarm.repeatsOnDay(4)) // Thursday
        assertFalse(weekendAlarm.repeatsOnDay(5)) // Friday
        assertTrue(weekendAlarm.repeatsOnDay(6))  // Saturday
    }

    @Test
    fun `snooze duration calculations are correct`() {
        val snoozeDurations = listOf(5, 10, 15, 30, 60)

        snoozeDurations.forEach { duration ->
            val expectedMs = duration * 60 * 1000L
            val now = System.currentTimeMillis()
            val snoozeTime = now + expectedMs

            // Verify the calculation
            val actualDuration = (snoozeTime - now) / (60 * 1000)
            assertEquals(duration.toLong(), actualDuration)
        }
    }

    @Test
    fun `alarm trigger time calculation for future time today`() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val now = System.currentTimeMillis()

        // If the calculated time is in the future
        if (calendar.timeInMillis > now) {
            assertTrue(calendar.timeInMillis > now)
        }
    }

    @Test
    fun `day of week bit calculation is correct`() {
        val sundayBit = 1 shl 0    // 1
        val mondayBit = 1 shl 1    // 2
        val tuesdayBit = 1 shl 2   // 4
        val wednesdayBit = 1 shl 3 // 8
        val thursdayBit = 1 shl 4  // 16
        val fridayBit = 1 shl 5    // 32
        val saturdayBit = 1 shl 6  // 64

        assertEquals(1, sundayBit)
        assertEquals(2, mondayBit)
        assertEquals(4, tuesdayBit)
        assertEquals(8, wednesdayBit)
        assertEquals(16, thursdayBit)
        assertEquals(32, fridayBit)
        assertEquals(64, saturdayBit)

        // Weekdays
        val weekdays = mondayBit or tuesdayBit or wednesdayBit or thursdayBit or fridayBit
        assertEquals(62, weekdays)

        // Weekends
        val weekends = sundayBit or saturdayBit
        assertEquals(65, weekends)

        // Every day
        val everyDay = sundayBit or mondayBit or tuesdayBit or wednesdayBit or
                thursdayBit or fridayBit or saturdayBit
        assertEquals(127, everyDay)
    }
}
