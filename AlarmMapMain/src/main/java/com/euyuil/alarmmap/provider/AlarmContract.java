package com.euyuil.alarmmap.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This is the column definition for model Alarm.
 * @author EUYUIL
 * @version 0.0.20130927
 */
public final class AlarmContract implements BaseColumns {

    public AlarmContract() {
    }

    public static final String CONTENT_AUTHORITY = AppDbHelper.APP_CONTENT_AUTHORITY;

    public static final String TABLE_NAME = "alarm";

    public static final String TABLE_CONTENT_URI_STRING =
            String.format("content://%s/%s", CONTENT_AUTHORITY, TABLE_NAME);

    public static final Uri TABLE_CONTENT_URI = Uri.parse(TABLE_CONTENT_URI_STRING);

    public static final String CONTENT_TYPE_DIR =
            String.format("vnd.android.cursor.dir/vnd.%s.%s", CONTENT_AUTHORITY, TABLE_NAME);

    public static final String CONTENT_TYPE_ITEM =
            String.format("vnd.android.cursor.item/vnd.%s.%s", CONTENT_AUTHORITY, TABLE_NAME);

    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";

    public static final String COLUMN_NAME_STATE = "state";
    public static final String COLUMN_NAME_LAST_WAKE_TIME = "lastWakeTime"; // TODO Invalidate when TZ changes.

    public static final String COLUMN_NAME_USES_REPEAT = "usesRepeat";
    public static final String COLUMN_NAME_REPEAT_DAYS_OF_WEEK = "repeatDaysOfWeek";
    public static final String COLUMN_NAME_REPEAT_DESCRIPTION = "repeatDescription";

    public static final String COLUMN_NAME_USES_TIME_OF_DAY = "usesTimeOfDay";
    public static final String COLUMN_NAME_TIME_OF_DAY = "timeOfDay";
    public static final String COLUMN_NAME_TIME_OF_DAY_TIMEOUT = "timeOfDayTimeout";
    public static final String COLUMN_NAME_TIME_OF_DAY_DESCRIPTION = "timeOfDayDescription";

    public static final String COLUMN_NAME_USES_LOCATION = "usesLocation";
    public static final String COLUMN_NAME_LOCATION_LATITUDE = "locationLatitude";
    public static final String COLUMN_NAME_LOCATION_LONGITUDE = "locationLongitude";
    public static final String COLUMN_NAME_LOCATION_RADIUS = "locationRadius";
    public static final String COLUMN_NAME_LOCATION_ADDRESS = "locationAddress";
    public static final String COLUMN_NAME_LOCATION_DESCRIPTION = "locationDescription";

    public static final String COLUMN_NAME_RINGTONE = "ringtone";
    public static final String COLUMN_NAME_RINGING_ONCE_LENGTH = "ringingOnceLength";
    public static final String COLUMN_NAME_RINGING_TOTAL_LENGTH = "ringingTotalLength";
    public static final String COLUMN_NAME_RINGING_BREAK_LENGTH = "ringingBreakLength";
    public static final String COLUMN_NAME_RINGING_SNOOZE_LENGTH = "ringingSnoozeLength";

    public static final String COLUMN_NAME_NULLABLE = COLUMN_NAME_TITLE;

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +

                    COLUMN_NAME_TITLE + " TEXT, " +
                    COLUMN_NAME_DESCRIPTION + " TEXT, " +

                    COLUMN_NAME_STATE + " VARCHAR(15) NOT NULL DEFAULT 'enabled', " +
                    COLUMN_NAME_LAST_WAKE_TIME + " LONG, " +

                    COLUMN_NAME_USES_REPEAT + " BOOLEAN NOT NULL DEFAULT 0, " +
                    COLUMN_NAME_REPEAT_DAYS_OF_WEEK + " INTEGER, " +
                    COLUMN_NAME_REPEAT_DESCRIPTION + " TEXT, " +

                    COLUMN_NAME_USES_TIME_OF_DAY + " BOOLEAN NOT NULL DEFAULT 0, " +
                    COLUMN_NAME_TIME_OF_DAY + " INTEGER, " +
                    COLUMN_NAME_TIME_OF_DAY_TIMEOUT + " INTEGER, " +
                    COLUMN_NAME_TIME_OF_DAY_DESCRIPTION + " TEXT, " +

                    COLUMN_NAME_USES_LOCATION + " BOOLEAN NOT NULL DEFAULT 0, " +
                    COLUMN_NAME_LOCATION_LATITUDE + " REAL, " +
                    COLUMN_NAME_LOCATION_LONGITUDE + " REAL, " +
                    COLUMN_NAME_LOCATION_RADIUS + " REAL, " +
                    COLUMN_NAME_LOCATION_ADDRESS + " TEXT, " +
                    COLUMN_NAME_LOCATION_DESCRIPTION + " TEXT, " +

                    COLUMN_NAME_RINGTONE + " TEXT, " +
                    COLUMN_NAME_RINGING_ONCE_LENGTH + " INTEGER, " +
                    COLUMN_NAME_RINGING_TOTAL_LENGTH + " INTEGER, " +
                    COLUMN_NAME_RINGING_BREAK_LENGTH + " INTEGER, " +
                    COLUMN_NAME_RINGING_SNOOZE_LENGTH + " INTEGER" +
                    " )";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String[] PROJECTION_STAR = new String[] {

            _ID,

            COLUMN_NAME_TITLE,
            COLUMN_NAME_DESCRIPTION,

            COLUMN_NAME_STATE,
            COLUMN_NAME_LAST_WAKE_TIME,

            COLUMN_NAME_USES_REPEAT,
            COLUMN_NAME_REPEAT_DAYS_OF_WEEK,
            COLUMN_NAME_REPEAT_DESCRIPTION,

            COLUMN_NAME_USES_TIME_OF_DAY,
            COLUMN_NAME_TIME_OF_DAY,
            COLUMN_NAME_TIME_OF_DAY_TIMEOUT,
            COLUMN_NAME_TIME_OF_DAY_DESCRIPTION,

            COLUMN_NAME_USES_LOCATION,
            COLUMN_NAME_LOCATION_LATITUDE,
            COLUMN_NAME_LOCATION_LONGITUDE,
            COLUMN_NAME_LOCATION_RADIUS,
            COLUMN_NAME_LOCATION_ADDRESS,
            COLUMN_NAME_LOCATION_DESCRIPTION,

            COLUMN_NAME_RINGTONE,
            COLUMN_NAME_RINGING_ONCE_LENGTH,
            COLUMN_NAME_RINGING_TOTAL_LENGTH,
            COLUMN_NAME_RINGING_BREAK_LENGTH,
            COLUMN_NAME_RINGING_SNOOZE_LENGTH,
    };
}
