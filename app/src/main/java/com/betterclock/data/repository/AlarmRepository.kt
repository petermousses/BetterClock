package com.betterclock.data.repository

import com.betterclock.data.dao.AlarmDao
import com.betterclock.data.model.Alarm
import kotlinx.coroutines.flow.Flow

/**
 * Repository for alarm data operations
 */
class AlarmRepository(private val alarmDao: AlarmDao) {

    val allAlarms: Flow<List<Alarm>> = alarmDao.getAllAlarms()
    val enabledAlarms: Flow<List<Alarm>> = alarmDao.getEnabledAlarms()

    suspend fun getAlarmById(id: Long): Alarm? {
        return alarmDao.getAlarmById(id)
    }

    fun getAlarmByIdFlow(id: Long): Flow<Alarm?> {
        return alarmDao.getAlarmByIdFlow(id)
    }

    suspend fun insertAlarm(alarm: Alarm): Long {
        return alarmDao.insertAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    suspend fun deleteAlarmById(id: Long) {
        alarmDao.deleteAlarmById(id)
    }

    suspend fun setAlarmEnabled(id: Long, enabled: Boolean) {
        alarmDao.setAlarmEnabled(id, enabled)
    }

    suspend fun updateTriggerTime(id: Long, triggerTime: Long) {
        alarmDao.updateTriggerTime(id, triggerTime)
    }

    suspend fun updateSnoozeCount(id: Long, snoozeCount: Int) {
        alarmDao.updateSnoozeCount(id, snoozeCount)
    }

    suspend fun getEnabledAlarmsList(): List<Alarm> {
        return alarmDao.getEnabledAlarmsList()
    }

    suspend fun getAlarmCount(): Int {
        return alarmDao.getAlarmCount()
    }
}
