package com.betterclock.data.dao

import androidx.room.*
import com.betterclock.data.model.Alarm
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Alarm operations
 */
@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms ORDER BY hour, minute")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY hour, minute")
    fun getEnabledAlarms(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE isEnabled = 1")
    suspend fun getEnabledAlarmsList(): List<Alarm>

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarmById(alarmId: Long): Alarm?

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    fun getAlarmByIdFlow(alarmId: Long): Flow<Alarm?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteAlarmById(alarmId: Long)

    @Query("UPDATE alarms SET isEnabled = :isEnabled, updatedAt = :updatedAt WHERE id = :alarmId")
    suspend fun setAlarmEnabled(alarmId: Long, isEnabled: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE alarms SET nextTriggerTime = :triggerTime, updatedAt = :updatedAt WHERE id = :alarmId")
    suspend fun updateTriggerTime(alarmId: Long, triggerTime: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE alarms SET snoozeCount = :snoozeCount, updatedAt = :updatedAt WHERE id = :alarmId")
    suspend fun updateSnoozeCount(alarmId: Long, snoozeCount: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM alarms")
    suspend fun getAlarmCount(): Int

    @Query("SELECT COUNT(*) FROM alarms WHERE isEnabled = 1")
    suspend fun getEnabledAlarmCount(): Int
}
