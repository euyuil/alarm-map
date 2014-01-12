package com.euyuil.alarmmap.model;

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

    public static final String COLUMN_NAME_ALARM_AVAILABLE = "available";
    public static final String COLUMN_NAME_ALARM_TITLE = "title";
    public static final String COLUMN_NAME_ALARM_TIME_OF_DAY = "timeOfDay";
    public static final String COLUMN_NAME_ALARM_LOCATION_LATITUDE = "locationLatitude";
    public static final String COLUMN_NAME_ALARM_LOCATION_LONGITUDE = "locationLongitude";
    public static final String COLUMN_NAME_ALARM_LOCATION_RADIUS = "locationRadius";
    public static final String COLUMN_NAME_ALARM_LOCATION_ADDRESS = "locationAddress";
    public static final String COLUMN_NAME_ALARM_DAY_OF_WEEK = "dayOfWeek";
    public static final String COLUMN_NAME_ALARM_REPEAT = "repeat";
    public static final String COLUMN_NAME_ALARM_RINGTONE = "ringtone";
    // TODO Add last rang time and next ring time, which should be handled by content provider.
    // TODO And should be reset every time the alarm or time settings are changed.
    public static final String COLUMN_NAME_LAST_DISMISS_TIME = "lastDismissTime";
    public static final String COLUMN_NAME_LAST_SNOOZE_TIME = "lastSnoozeTime";
    public static final String COLUMN_NAME_NEXT_RING_TIME = "nextRingTime";

    public static final String COLUMN_NAME_NULLABLE = COLUMN_NAME_ALARM_TITLE;

    public static final String[] PROJECTION_ALARM_STAR = new String[] {
            _ID,
            COLUMN_NAME_ALARM_AVAILABLE,
            COLUMN_NAME_ALARM_TITLE,
            COLUMN_NAME_ALARM_TIME_OF_DAY,
            COLUMN_NAME_ALARM_LOCATION_LATITUDE,
            COLUMN_NAME_ALARM_LOCATION_LONGITUDE,
            COLUMN_NAME_ALARM_LOCATION_RADIUS,
            COLUMN_NAME_ALARM_LOCATION_ADDRESS,
            COLUMN_NAME_ALARM_DAY_OF_WEEK,
            COLUMN_NAME_ALARM_REPEAT,
            COLUMN_NAME_ALARM_RINGTONE
    };
}
