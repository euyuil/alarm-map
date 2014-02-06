package com.euyuil.alarmmap.provider;

import android.provider.BaseColumns;

/**
 * This is the column definition for model Alarm.
 * @author EUYUIL
 * @version 0.0.20130927
 */
public final class AlarmContract implements BaseColumns {

    public AlarmContract() {
    }

    public static final String TABLE_NAME = "alarm";

    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";

    public static final String COLUMN_NAME_STATE = "state";
    public static final String COLUMN_NAME_REPEAT = "repeat";

    public static final String COLUMN_NAME_USES_TIME = "usesTime";
    public static final String COLUMN_NAME_TIME_OF_DAY = "timeOfDay";
    public static final String COLUMN_NAME_DAYS_OF_WEEK = "daysOfWeek"; // Used when repeat.
    public static final String COLUMN_NAME_TRIGGER_TIMEOUT = "triggerTimeout";
    public static final String COLUMN_NAME_TIME_DESCRIPTION = "timeDescription";

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
                    COLUMN_NAME_REPEAT + " BOOLEAN NOT NULL DEFAULT 0, " +

                    COLUMN_NAME_USES_TIME + " BOOLEAN NOT NULL DEFAULT 0, " +
                    COLUMN_NAME_TIME_OF_DAY + " INTEGER, " +
                    COLUMN_NAME_DAYS_OF_WEEK + " INTEGER, " +
                    COLUMN_NAME_TRIGGER_TIMEOUT + " INTEGER, " +
                    COLUMN_NAME_TIME_DESCRIPTION + " TEXT, " +

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
            COLUMN_NAME_REPEAT,

            COLUMN_NAME_USES_TIME,
            COLUMN_NAME_TIME_OF_DAY,
            COLUMN_NAME_DAYS_OF_WEEK,
            COLUMN_NAME_TRIGGER_TIMEOUT,
            COLUMN_NAME_TIME_DESCRIPTION,

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
