package com.betterclock.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Enum representing challenge difficulty levels
 */
enum class ChallengeDifficulty {
    EASY,
    MEDIUM,
    HARD
}

/**
 * Enum representing available challenge types
 */
enum class ChallengeType {
    NONE,
    MATH,
    MEMORY,
    PATTERN
}

/**
 * Room type converters for enum types
 */
class Converters {
    @TypeConverter
    fun fromChallengeDifficulty(value: ChallengeDifficulty): String = value.name

    @TypeConverter
    fun toChallengeDifficulty(value: String): ChallengeDifficulty =
        ChallengeDifficulty.valueOf(value)

    @TypeConverter
    fun fromChallengeType(value: ChallengeType): String = value.name

    @TypeConverter
    fun toChallengeType(value: String): ChallengeType =
        ChallengeType.valueOf(value)
}

/**
 * Alarm entity representing a single alarm with all its configuration
 */
@Entity(tableName = "alarms")
@TypeConverters(Converters::class)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val hour: Int,
    val minute: Int,

    val isEnabled: Boolean = true,
    val label: String = "",

    // Challenge configuration
    val challengeEnabled: Boolean = false,
    val challengeType: ChallengeType = ChallengeType.MATH,
    val challengeDifficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM,
    val challengeTimeoutMinutes: Int = 5,

    // Sound and vibration
    val soundUri: String? = null,
    val vibrationEnabled: Boolean = true,

    // Snooze configuration
    val snoozeDurationMinutes: Int = 10,
    val snoozeCount: Int = 0,

    // Timestamp for next alarm trigger (calculated)
    val nextTriggerTime: Long = 0,

    // Days of week (bitmask: Sunday = 1, Monday = 2, Tuesday = 4, etc.)
    // 0 means one-time alarm
    val repeatDays: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Returns formatted time string based on 24-hour preference
     */
    fun getFormattedTime(use24Hour: Boolean): String {
        return if (use24Hour) {
            String.format("%02d:%02d", hour, minute)
        } else {
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            val amPm = if (hour < 12) "AM" else "PM"
            String.format("%d:%02d %s", displayHour, minute, amPm)
        }
    }

    /**
     * Check if alarm repeats on a specific day
     */
    fun repeatsOnDay(dayOfWeek: Int): Boolean {
        if (repeatDays == 0) return false
        val dayBit = 1 shl dayOfWeek
        return (repeatDays and dayBit) != 0
    }

    /**
     * Returns true if this is a one-time alarm
     */
    fun isOneTime(): Boolean = repeatDays == 0

    /**
     * Returns human-readable repeat schedule
     */
    fun getRepeatDescription(): String {
        if (repeatDays == 0) return "One time"
        if (repeatDays == 127) return "Every day"
        if (repeatDays == 62) return "Weekdays"
        if (repeatDays == 65) return "Weekends"

        val days = mutableListOf<String>()
        val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        for (i in 0..6) {
            if ((repeatDays and (1 shl i)) != 0) {
                days.add(dayNames[i])
            }
        }
        return days.joinToString(", ")
    }
}
