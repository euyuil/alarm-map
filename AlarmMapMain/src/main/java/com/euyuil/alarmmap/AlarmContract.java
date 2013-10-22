package com.euyuil.alarmmap;

import android.provider.BaseColumns;

/**
 * This is the column definition for model Alarm.
 * @author EUYUIL
 * @version 0.0.20130927
 */

public final class AlarmContract {

    public AlarmContract() {
    }

    public static abstract class AlarmEntry implements BaseColumns {

        public static final String TABLE_NAME = "alarm";

        public static final String COLUMN_NAME_ALARM_AVAILABLE = "available";
        public static final String COLUMN_NAME_ALARM_TITLE = "title";
        public static final String COLUMN_NAME_ALARM_TIME_OF_DAY = "time_of_day";
        public static final String COLUMN_NAME_ALARM_LOCATION_LATITUDE = "location_latitude";
        public static final String COLUMN_NAME_ALARM_LOCATION_LONGITUDE = "location_longitude";
        public static final String COLUMN_NAME_ALARM_LOCATION_RADIUS = "location_radius";
        public static final String COLUMN_NAME_ALARM_LOCATION_ADDRESS = "location_address";
        public static final String COLUMN_NAME_ALARM_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_NAME_ALARM_REPEAT = "repeat";
        public static final String COLUMN_NAME_ALARM_RINGTONE = "ringtone";

        public static final String COLUMN_NAME_NULLABLE = COLUMN_NAME_ALARM_TITLE;

        public static final String[] PROJECTION_ALARM_DETAILS = new String[] {
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
}
