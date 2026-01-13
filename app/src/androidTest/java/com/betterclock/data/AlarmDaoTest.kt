package com.betterclock.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.betterclock.data.dao.AlarmDao
import com.betterclock.data.model.Alarm
import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for AlarmDao
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var alarmDao: AlarmDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        alarmDao = database.alarmDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveAlarm() = runBlocking {
        val alarm = createTestAlarm(hour = 8, minute = 30, label = "Wake up")
        val id = alarmDao.insertAlarm(alarm)

        val retrieved = alarmDao.getAlarmById(id)

        assertNotNull(retrieved)
        assertEquals(8, retrieved?.hour)
        assertEquals(30, retrieved?.minute)
        assertEquals("Wake up", retrieved?.label)
    }

    @Test
    fun updateAlarm() = runBlocking {
        val alarm = createTestAlarm(hour = 8, minute = 30)
        val id = alarmDao.insertAlarm(alarm)

        val updated = alarm.copy(id = id, hour = 9, minute = 0, label = "Updated")
        alarmDao.updateAlarm(updated)

        val retrieved = alarmDao.getAlarmById(id)
        assertEquals(9, retrieved?.hour)
        assertEquals(0, retrieved?.minute)
        assertEquals("Updated", retrieved?.label)
    }

    @Test
    fun deleteAlarm() = runBlocking {
        val alarm = createTestAlarm()
        val id = alarmDao.insertAlarm(alarm)

        val toDelete = alarm.copy(id = id)
        alarmDao.deleteAlarm(toDelete)

        val retrieved = alarmDao.getAlarmById(id)
        assertNull(retrieved)
    }

    @Test
    fun deleteAlarmById() = runBlocking {
        val alarm = createTestAlarm()
        val id = alarmDao.insertAlarm(alarm)

        alarmDao.deleteAlarmById(id)

        val retrieved = alarmDao.getAlarmById(id)
        assertNull(retrieved)
    }

    @Test
    fun getAllAlarms_returnsSortedByTime() = runBlocking {
        alarmDao.insertAlarm(createTestAlarm(hour = 14, minute = 0))
        alarmDao.insertAlarm(createTestAlarm(hour = 8, minute = 30))
        alarmDao.insertAlarm(createTestAlarm(hour = 8, minute = 0))

        val alarms = alarmDao.getAllAlarms().first()

        assertEquals(3, alarms.size)
        assertEquals(8, alarms[0].hour)
        assertEquals(0, alarms[0].minute)
        assertEquals(8, alarms[1].hour)
        assertEquals(30, alarms[1].minute)
        assertEquals(14, alarms[2].hour)
    }

    @Test
    fun getEnabledAlarms_onlyReturnsEnabled() = runBlocking {
        alarmDao.insertAlarm(createTestAlarm(hour = 8, isEnabled = true))
        alarmDao.insertAlarm(createTestAlarm(hour = 9, isEnabled = false))
        alarmDao.insertAlarm(createTestAlarm(hour = 10, isEnabled = true))

        val enabledAlarms = alarmDao.getEnabledAlarms().first()

        assertEquals(2, enabledAlarms.size)
        assertTrue(enabledAlarms.all { it.isEnabled })
    }

    @Test
    fun setAlarmEnabled_updatesStatus() = runBlocking {
        val alarm = createTestAlarm(isEnabled = true)
        val id = alarmDao.insertAlarm(alarm)

        alarmDao.setAlarmEnabled(id, false)

        val retrieved = alarmDao.getAlarmById(id)
        assertFalse(retrieved?.isEnabled ?: true)
    }

    @Test
    fun updateTriggerTime_updatesCorrectly() = runBlocking {
        val alarm = createTestAlarm()
        val id = alarmDao.insertAlarm(alarm)
        val newTriggerTime = System.currentTimeMillis() + 3600000

        alarmDao.updateTriggerTime(id, newTriggerTime)

        val retrieved = alarmDao.getAlarmById(id)
        assertEquals(newTriggerTime, retrieved?.nextTriggerTime)
    }

    @Test
    fun updateSnoozeCount_updatesCorrectly() = runBlocking {
        val alarm = createTestAlarm()
        val id = alarmDao.insertAlarm(alarm)

        alarmDao.updateSnoozeCount(id, 3)

        val retrieved = alarmDao.getAlarmById(id)
        assertEquals(3, retrieved?.snoozeCount)
    }

    @Test
    fun getAlarmCount_returnsCorrectCount() = runBlocking {
        assertEquals(0, alarmDao.getAlarmCount())

        alarmDao.insertAlarm(createTestAlarm())
        alarmDao.insertAlarm(createTestAlarm())
        alarmDao.insertAlarm(createTestAlarm())

        assertEquals(3, alarmDao.getAlarmCount())
    }

    @Test
    fun getEnabledAlarmCount_returnsCorrectCount() = runBlocking {
        alarmDao.insertAlarm(createTestAlarm(isEnabled = true))
        alarmDao.insertAlarm(createTestAlarm(isEnabled = false))
        alarmDao.insertAlarm(createTestAlarm(isEnabled = true))

        assertEquals(2, alarmDao.getEnabledAlarmCount())
    }

    @Test
    fun alarmWithChallengeSettings_persistsCorrectly() = runBlocking {
        val alarm = createTestAlarm(
            challengeEnabled = true,
            challengeType = ChallengeType.MATH,
            challengeDifficulty = ChallengeDifficulty.HARD
        )
        val id = alarmDao.insertAlarm(alarm)

        val retrieved = alarmDao.getAlarmById(id)

        assertTrue(retrieved?.challengeEnabled ?: false)
        assertEquals(ChallengeType.MATH, retrieved?.challengeType)
        assertEquals(ChallengeDifficulty.HARD, retrieved?.challengeDifficulty)
    }

    @Test
    fun alarmWithRepeatDays_persistsCorrectly() = runBlocking {
        val alarm = createTestAlarm(repeatDays = 62) // Weekdays
        val id = alarmDao.insertAlarm(alarm)

        val retrieved = alarmDao.getAlarmById(id)

        assertEquals(62, retrieved?.repeatDays)
        assertTrue(retrieved?.repeatsOnDay(1) ?: false) // Monday
        assertFalse(retrieved?.repeatsOnDay(0) ?: true)  // Sunday
    }

    private fun createTestAlarm(
        hour: Int = 8,
        minute: Int = 0,
        isEnabled: Boolean = true,
        label: String = "",
        challengeEnabled: Boolean = false,
        challengeType: ChallengeType = ChallengeType.MATH,
        challengeDifficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM,
        repeatDays: Int = 0
    ) = Alarm(
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
