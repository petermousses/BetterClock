package com.betterclock.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.betterclock.R
import com.betterclock.data.AppDatabase
import com.betterclock.data.repository.AlarmRepository
import com.betterclock.ui.screens.AlarmActivity
import kotlinx.coroutines.*

/**
 * Foreground service that handles alarm triggering, sound, and vibration
 */
class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val CHANNEL_ID = "alarm_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_SNOOZE = "com.betterclock.ACTION_SNOOZE"
        const val ACTION_STOP = "com.betterclock.ACTION_STOP"

        private var currentAlarmId: Long = -1

        fun getCurrentAlarmId(): Long = currentAlarmId
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmId = intent?.getLongExtra(AlarmScheduler.EXTRA_ALARM_ID, -1) ?: -1

        when (intent?.action) {
            ACTION_SNOOZE -> {
                handleSnooze(alarmId)
                return START_NOT_STICKY
            }
            ACTION_STOP -> {
                handleStop(alarmId)
                return START_NOT_STICKY
            }
        }

        if (alarmId != -1L) {
            currentAlarmId = alarmId
            serviceScope.launch {
                val database = AppDatabase.getInstance(applicationContext)
                val repository = AlarmRepository(database.alarmDao())
                val alarm = repository.getAlarmById(alarmId)

                withContext(Dispatchers.Main) {
                    if (alarm != null) {
                        startForeground(NOTIFICATION_ID, createNotification(alarm.label, alarmId))
                        startAlarmSound(alarm.soundUri)
                        if (alarm.vibrationEnabled) {
                            startVibration()
                        }
                        launchAlarmActivity(alarmId)
                    }
                }
            }
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for active alarms"
                setSound(null, null)
                enableVibration(false)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(label: String, alarmId: Long): Notification {
        val fullScreenIntent = Intent(this, AlarmActivity::class.java).apply {
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_NO_USER_ACTION
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
        }
        val snoozePendingIntent = PendingIntent.getService(
            this,
            1,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_STOP
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            2,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText(label.ifEmpty { "Alarm is ringing" })
            .setSmallIcon(R.drawable.ic_alarm)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .addAction(R.drawable.ic_snooze, "Snooze", snoozePendingIntent)
            .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
    }

    private fun startAlarmSound(soundUri: String?) {
        try {
            val uri = if (soundUri != null) {
                android.net.Uri.parse(soundUri)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    ?: Settings.System.DEFAULT_ALARM_ALERT_URI
            }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, uri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startVibration() {
        val pattern = longArrayOf(0, 500, 500, 500, 500, 500)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, 0)
        }
    }

    private fun launchAlarmActivity(alarmId: Long) {
        val intent = Intent(this, AlarmActivity::class.java).apply {
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
        }
        startActivity(intent)
    }

    private fun handleSnooze(alarmId: Long) {
        serviceScope.launch {
            val database = AppDatabase.getInstance(applicationContext)
            val repository = AlarmRepository(database.alarmDao())
            val alarm = repository.getAlarmById(alarmId)

            alarm?.let {
                val scheduler = AlarmScheduler(applicationContext)
                scheduler.scheduleSnooze(alarmId, it.snoozeDurationMinutes)
                repository.updateSnoozeCount(alarmId, it.snoozeCount + 1)
            }

            withContext(Dispatchers.Main) {
                stopAlarm()
            }
        }
    }

    private fun handleStop(alarmId: Long) {
        serviceScope.launch {
            val database = AppDatabase.getInstance(applicationContext)
            val repository = AlarmRepository(database.alarmDao())
            val alarm = repository.getAlarmById(alarmId)

            alarm?.let {
                if (it.isOneTime()) {
                    repository.setAlarmEnabled(alarmId, false)
                } else {
                    // Reschedule for next repeat day
                    val scheduler = AlarmScheduler(applicationContext)
                    scheduler.scheduleAlarm(it)
                }
                repository.updateSnoozeCount(alarmId, 0)
            }

            withContext(Dispatchers.Main) {
                stopAlarm()
            }
        }
    }

    fun stopAlarm() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null

        vibrator?.cancel()
        currentAlarmId = -1

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        vibrator?.cancel()
        serviceScope.cancel()
    }
}
