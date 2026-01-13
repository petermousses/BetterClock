package com.betterclock.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.betterclock.data.model.Alarm;
import com.betterclock.data.model.ChallengeDifficulty;
import com.betterclock.data.model.ChallengeType;
import com.betterclock.data.model.Converters;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AlarmDao_Impl implements AlarmDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Alarm> __insertionAdapterOfAlarm;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Alarm> __deletionAdapterOfAlarm;

  private final EntityDeletionOrUpdateAdapter<Alarm> __updateAdapterOfAlarm;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAlarmById;

  private final SharedSQLiteStatement __preparedStmtOfSetAlarmEnabled;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTriggerTime;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSnoozeCount;

  public AlarmDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAlarm = new EntityInsertionAdapter<Alarm>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `alarms` (`id`,`hour`,`minute`,`isEnabled`,`label`,`challengeEnabled`,`challengeType`,`challengeDifficulty`,`challengeTimeoutMinutes`,`soundUri`,`vibrationEnabled`,`snoozeDurationMinutes`,`snoozeCount`,`nextTriggerTime`,`repeatDays`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Alarm entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHour());
        statement.bindLong(3, entity.getMinute());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getLabel());
        final int _tmp_1 = entity.getChallengeEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        final String _tmp_2 = __converters.fromChallengeType(entity.getChallengeType());
        statement.bindString(7, _tmp_2);
        final String _tmp_3 = __converters.fromChallengeDifficulty(entity.getChallengeDifficulty());
        statement.bindString(8, _tmp_3);
        statement.bindLong(9, entity.getChallengeTimeoutMinutes());
        if (entity.getSoundUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSoundUri());
        }
        final int _tmp_4 = entity.getVibrationEnabled() ? 1 : 0;
        statement.bindLong(11, _tmp_4);
        statement.bindLong(12, entity.getSnoozeDurationMinutes());
        statement.bindLong(13, entity.getSnoozeCount());
        statement.bindLong(14, entity.getNextTriggerTime());
        statement.bindLong(15, entity.getRepeatDays());
        statement.bindLong(16, entity.getCreatedAt());
        statement.bindLong(17, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfAlarm = new EntityDeletionOrUpdateAdapter<Alarm>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `alarms` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Alarm entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAlarm = new EntityDeletionOrUpdateAdapter<Alarm>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `alarms` SET `id` = ?,`hour` = ?,`minute` = ?,`isEnabled` = ?,`label` = ?,`challengeEnabled` = ?,`challengeType` = ?,`challengeDifficulty` = ?,`challengeTimeoutMinutes` = ?,`soundUri` = ?,`vibrationEnabled` = ?,`snoozeDurationMinutes` = ?,`snoozeCount` = ?,`nextTriggerTime` = ?,`repeatDays` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Alarm entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHour());
        statement.bindLong(3, entity.getMinute());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getLabel());
        final int _tmp_1 = entity.getChallengeEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        final String _tmp_2 = __converters.fromChallengeType(entity.getChallengeType());
        statement.bindString(7, _tmp_2);
        final String _tmp_3 = __converters.fromChallengeDifficulty(entity.getChallengeDifficulty());
        statement.bindString(8, _tmp_3);
        statement.bindLong(9, entity.getChallengeTimeoutMinutes());
        if (entity.getSoundUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSoundUri());
        }
        final int _tmp_4 = entity.getVibrationEnabled() ? 1 : 0;
        statement.bindLong(11, _tmp_4);
        statement.bindLong(12, entity.getSnoozeDurationMinutes());
        statement.bindLong(13, entity.getSnoozeCount());
        statement.bindLong(14, entity.getNextTriggerTime());
        statement.bindLong(15, entity.getRepeatDays());
        statement.bindLong(16, entity.getCreatedAt());
        statement.bindLong(17, entity.getUpdatedAt());
        statement.bindLong(18, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAlarmById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM alarms WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetAlarmEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alarms SET isEnabled = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTriggerTime = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alarms SET nextTriggerTime = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSnoozeCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alarms SET snoozeCount = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAlarm(final Alarm alarm, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAlarm.insertAndReturnId(alarm);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAlarm(final Alarm alarm, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAlarm.handle(alarm);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAlarm(final Alarm alarm, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAlarm.handle(alarm);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAlarmById(final long alarmId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAlarmById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, alarmId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAlarmById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setAlarmEnabled(final long alarmId, final boolean isEnabled, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetAlarmEnabled.acquire();
        int _argIndex = 1;
        final int _tmp = isEnabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, alarmId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetAlarmEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTriggerTime(final long alarmId, final long triggerTime, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTriggerTime.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, triggerTime);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, alarmId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateTriggerTime.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSnoozeCount(final long alarmId, final int snoozeCount, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSnoozeCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, snoozeCount);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, alarmId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateSnoozeCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Alarm>> getAllAlarms() {
    final String _sql = "SELECT * FROM alarms ORDER BY hour, minute";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alarms"}, new Callable<List<Alarm>>() {
      @Override
      @NonNull
      public List<Alarm> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
          final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfChallengeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeEnabled");
          final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
          final int _cursorIndexOfChallengeDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeDifficulty");
          final int _cursorIndexOfChallengeTimeoutMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeTimeoutMinutes");
          final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeCount");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfRepeatDays = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Alarm> _result = new ArrayList<Alarm>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Alarm _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpMinute;
            _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpChallengeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfChallengeEnabled);
            _tmpChallengeEnabled = _tmp_1 != 0;
            final ChallengeType _tmpChallengeType;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfChallengeType);
            _tmpChallengeType = __converters.toChallengeType(_tmp_2);
            final ChallengeDifficulty _tmpChallengeDifficulty;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfChallengeDifficulty);
            _tmpChallengeDifficulty = __converters.toChallengeDifficulty(_tmp_3);
            final int _tmpChallengeTimeoutMinutes;
            _tmpChallengeTimeoutMinutes = _cursor.getInt(_cursorIndexOfChallengeTimeoutMinutes);
            final String _tmpSoundUri;
            if (_cursor.isNull(_cursorIndexOfSoundUri)) {
              _tmpSoundUri = null;
            } else {
              _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpSnoozeCount;
            _tmpSnoozeCount = _cursor.getInt(_cursorIndexOfSnoozeCount);
            final long _tmpNextTriggerTime;
            _tmpNextTriggerTime = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            final int _tmpRepeatDays;
            _tmpRepeatDays = _cursor.getInt(_cursorIndexOfRepeatDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Alarm(_tmpId,_tmpHour,_tmpMinute,_tmpIsEnabled,_tmpLabel,_tmpChallengeEnabled,_tmpChallengeType,_tmpChallengeDifficulty,_tmpChallengeTimeoutMinutes,_tmpSoundUri,_tmpVibrationEnabled,_tmpSnoozeDurationMinutes,_tmpSnoozeCount,_tmpNextTriggerTime,_tmpRepeatDays,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Alarm>> getEnabledAlarms() {
    final String _sql = "SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY hour, minute";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alarms"}, new Callable<List<Alarm>>() {
      @Override
      @NonNull
      public List<Alarm> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
          final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfChallengeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeEnabled");
          final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
          final int _cursorIndexOfChallengeDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeDifficulty");
          final int _cursorIndexOfChallengeTimeoutMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeTimeoutMinutes");
          final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeCount");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfRepeatDays = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Alarm> _result = new ArrayList<Alarm>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Alarm _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpMinute;
            _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpChallengeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfChallengeEnabled);
            _tmpChallengeEnabled = _tmp_1 != 0;
            final ChallengeType _tmpChallengeType;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfChallengeType);
            _tmpChallengeType = __converters.toChallengeType(_tmp_2);
            final ChallengeDifficulty _tmpChallengeDifficulty;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfChallengeDifficulty);
            _tmpChallengeDifficulty = __converters.toChallengeDifficulty(_tmp_3);
            final int _tmpChallengeTimeoutMinutes;
            _tmpChallengeTimeoutMinutes = _cursor.getInt(_cursorIndexOfChallengeTimeoutMinutes);
            final String _tmpSoundUri;
            if (_cursor.isNull(_cursorIndexOfSoundUri)) {
              _tmpSoundUri = null;
            } else {
              _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpSnoozeCount;
            _tmpSnoozeCount = _cursor.getInt(_cursorIndexOfSnoozeCount);
            final long _tmpNextTriggerTime;
            _tmpNextTriggerTime = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            final int _tmpRepeatDays;
            _tmpRepeatDays = _cursor.getInt(_cursorIndexOfRepeatDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Alarm(_tmpId,_tmpHour,_tmpMinute,_tmpIsEnabled,_tmpLabel,_tmpChallengeEnabled,_tmpChallengeType,_tmpChallengeDifficulty,_tmpChallengeTimeoutMinutes,_tmpSoundUri,_tmpVibrationEnabled,_tmpSnoozeDurationMinutes,_tmpSnoozeCount,_tmpNextTriggerTime,_tmpRepeatDays,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEnabledAlarmsList(final Continuation<? super List<Alarm>> $completion) {
    final String _sql = "SELECT * FROM alarms WHERE isEnabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Alarm>>() {
      @Override
      @NonNull
      public List<Alarm> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
          final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfChallengeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeEnabled");
          final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
          final int _cursorIndexOfChallengeDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeDifficulty");
          final int _cursorIndexOfChallengeTimeoutMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeTimeoutMinutes");
          final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeCount");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfRepeatDays = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Alarm> _result = new ArrayList<Alarm>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Alarm _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpMinute;
            _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpChallengeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfChallengeEnabled);
            _tmpChallengeEnabled = _tmp_1 != 0;
            final ChallengeType _tmpChallengeType;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfChallengeType);
            _tmpChallengeType = __converters.toChallengeType(_tmp_2);
            final ChallengeDifficulty _tmpChallengeDifficulty;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfChallengeDifficulty);
            _tmpChallengeDifficulty = __converters.toChallengeDifficulty(_tmp_3);
            final int _tmpChallengeTimeoutMinutes;
            _tmpChallengeTimeoutMinutes = _cursor.getInt(_cursorIndexOfChallengeTimeoutMinutes);
            final String _tmpSoundUri;
            if (_cursor.isNull(_cursorIndexOfSoundUri)) {
              _tmpSoundUri = null;
            } else {
              _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpSnoozeCount;
            _tmpSnoozeCount = _cursor.getInt(_cursorIndexOfSnoozeCount);
            final long _tmpNextTriggerTime;
            _tmpNextTriggerTime = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            final int _tmpRepeatDays;
            _tmpRepeatDays = _cursor.getInt(_cursorIndexOfRepeatDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Alarm(_tmpId,_tmpHour,_tmpMinute,_tmpIsEnabled,_tmpLabel,_tmpChallengeEnabled,_tmpChallengeType,_tmpChallengeDifficulty,_tmpChallengeTimeoutMinutes,_tmpSoundUri,_tmpVibrationEnabled,_tmpSnoozeDurationMinutes,_tmpSnoozeCount,_tmpNextTriggerTime,_tmpRepeatDays,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAlarmById(final long alarmId, final Continuation<? super Alarm> $completion) {
    final String _sql = "SELECT * FROM alarms WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, alarmId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Alarm>() {
      @Override
      @Nullable
      public Alarm call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
          final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfChallengeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeEnabled");
          final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
          final int _cursorIndexOfChallengeDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeDifficulty");
          final int _cursorIndexOfChallengeTimeoutMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeTimeoutMinutes");
          final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeCount");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfRepeatDays = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Alarm _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpMinute;
            _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpChallengeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfChallengeEnabled);
            _tmpChallengeEnabled = _tmp_1 != 0;
            final ChallengeType _tmpChallengeType;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfChallengeType);
            _tmpChallengeType = __converters.toChallengeType(_tmp_2);
            final ChallengeDifficulty _tmpChallengeDifficulty;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfChallengeDifficulty);
            _tmpChallengeDifficulty = __converters.toChallengeDifficulty(_tmp_3);
            final int _tmpChallengeTimeoutMinutes;
            _tmpChallengeTimeoutMinutes = _cursor.getInt(_cursorIndexOfChallengeTimeoutMinutes);
            final String _tmpSoundUri;
            if (_cursor.isNull(_cursorIndexOfSoundUri)) {
              _tmpSoundUri = null;
            } else {
              _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpSnoozeCount;
            _tmpSnoozeCount = _cursor.getInt(_cursorIndexOfSnoozeCount);
            final long _tmpNextTriggerTime;
            _tmpNextTriggerTime = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            final int _tmpRepeatDays;
            _tmpRepeatDays = _cursor.getInt(_cursorIndexOfRepeatDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Alarm(_tmpId,_tmpHour,_tmpMinute,_tmpIsEnabled,_tmpLabel,_tmpChallengeEnabled,_tmpChallengeType,_tmpChallengeDifficulty,_tmpChallengeTimeoutMinutes,_tmpSoundUri,_tmpVibrationEnabled,_tmpSnoozeDurationMinutes,_tmpSnoozeCount,_tmpNextTriggerTime,_tmpRepeatDays,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Alarm> getAlarmByIdFlow(final long alarmId) {
    final String _sql = "SELECT * FROM alarms WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, alarmId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alarms"}, new Callable<Alarm>() {
      @Override
      @Nullable
      public Alarm call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
          final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfChallengeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeEnabled");
          final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
          final int _cursorIndexOfChallengeDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeDifficulty");
          final int _cursorIndexOfChallengeTimeoutMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeTimeoutMinutes");
          final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeCount");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfRepeatDays = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Alarm _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpMinute;
            _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpChallengeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfChallengeEnabled);
            _tmpChallengeEnabled = _tmp_1 != 0;
            final ChallengeType _tmpChallengeType;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfChallengeType);
            _tmpChallengeType = __converters.toChallengeType(_tmp_2);
            final ChallengeDifficulty _tmpChallengeDifficulty;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfChallengeDifficulty);
            _tmpChallengeDifficulty = __converters.toChallengeDifficulty(_tmp_3);
            final int _tmpChallengeTimeoutMinutes;
            _tmpChallengeTimeoutMinutes = _cursor.getInt(_cursorIndexOfChallengeTimeoutMinutes);
            final String _tmpSoundUri;
            if (_cursor.isNull(_cursorIndexOfSoundUri)) {
              _tmpSoundUri = null;
            } else {
              _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpSnoozeCount;
            _tmpSnoozeCount = _cursor.getInt(_cursorIndexOfSnoozeCount);
            final long _tmpNextTriggerTime;
            _tmpNextTriggerTime = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            final int _tmpRepeatDays;
            _tmpRepeatDays = _cursor.getInt(_cursorIndexOfRepeatDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Alarm(_tmpId,_tmpHour,_tmpMinute,_tmpIsEnabled,_tmpLabel,_tmpChallengeEnabled,_tmpChallengeType,_tmpChallengeDifficulty,_tmpChallengeTimeoutMinutes,_tmpSoundUri,_tmpVibrationEnabled,_tmpSnoozeDurationMinutes,_tmpSnoozeCount,_tmpNextTriggerTime,_tmpRepeatDays,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAlarmCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM alarms";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEnabledAlarmCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM alarms WHERE isEnabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
