package com.betterclock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.betterclock.data.AppDatabase
import com.betterclock.data.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Receiver to reschedule alarms after device boot
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {

            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val database = AppDatabase.getInstance(context)
                    val repository = AlarmRepository(database.alarmDao())
                    val scheduler = AlarmScheduler(context)

                    val enabledAlarms = repository.getEnabledAlarmsList()
                    scheduler.rescheduleAllAlarms(enabledAlarms)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
