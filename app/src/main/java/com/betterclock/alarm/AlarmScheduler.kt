package com.betterclock.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.betterclock.data.model.Alarm
import java.util.*

/**
 * Handles scheduling and canceling alarms using AlarmManager
 */
class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val ACTION_ALARM_TRIGGER = "com.betterclock.ALARM_TRIGGER"
        const val EXTRA_ALARM_ID = "extra_alarm_id"
    }

    /**
     * Schedule an alarm to trigger at the specified time
     */
    fun scheduleAlarm(alarm: Alarm) {
        if (!alarm.isEnabled) return

        val triggerTime = calculateNextTriggerTime(alarm)
        val pendingIntent = createAlarmPendingIntent(alarm.id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                    pendingIntent
                )
            }
        } else {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                pendingIntent
            )
        }
    }

    /**
     * Cancel a scheduled alarm
     */
    fun cancelAlarm(alarmId: Long) {
        val pendingIntent = createAlarmPendingIntent(alarmId)
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Schedule snooze for an alarm
     */
    fun scheduleSnooze(alarmId: Long, snoozeDurationMinutes: Int) {
        val triggerTime = System.currentTimeMillis() + (snoozeDurationMinutes * 60 * 1000)
        val pendingIntent = createAlarmPendingIntent(alarmId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                    pendingIntent
                )
            }
        } else {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                pendingIntent
            )
        }
    }

    /**
     * Reschedule all enabled alarms (called after device boot)
     */
    suspend fun rescheduleAllAlarms(alarms: List<Alarm>) {
        alarms.filter { it.isEnabled }.forEach { alarm ->
            scheduleAlarm(alarm)
        }
    }

    private fun createAlarmPendingIntent(alarmId: Long): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_ALARM_TRIGGER
            putExtra(EXTRA_ALARM_ID, alarmId)
        }

        return PendingIntent.getBroadcast(
            context,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Calculate the next trigger time for an alarm
     */
    private fun calculateNextTriggerTime(alarm: Alarm): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If alarm time has passed today, schedule for tomorrow (or next repeat day)
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            if (alarm.isOneTime()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            } else {
                // Find next repeat day
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                while (!alarm.repeatsOnDay(getDayOfWeekBit(calendar))) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
        } else if (!alarm.isOneTime()) {
            // Check if today is a repeat day
            if (!alarm.repeatsOnDay(getDayOfWeekBit(calendar))) {
                while (!alarm.repeatsOnDay(getDayOfWeekBit(calendar))) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
        }

        return calendar.timeInMillis
    }

    /**
     * Get day of week bit (Sunday = 0, Monday = 1, etc.)
     */
    private fun getDayOfWeekBit(calendar: Calendar): Int {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            else -> 0
        }
    }
}
