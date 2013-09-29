package com.euyuil.alarmmap;

import android.provider.BaseColumns;

/**
 * Created by Yue on 13-9-27.
 */

public final class AlarmContract {

    public AlarmContract() {
    }

    public static abstract class AlarmEntry implements BaseColumns {

        public static final String TABLE_NAME = "alarm";

        public static final String COLUMN_NAME_ALARM_TITLE = "alarm_title";
        public static final String COLUMN_NAME_ALARM_TIME = "alarm_time";
        public static final String COLUMN_NAME_ALARM_LOCATION_LATITUDE = "alarm_location_latitude";
        public static final String COLUMN_NAME_ALARM_LOCATION_LONGITUDE = "alarm_location_longitude";
        public static final String COLUMN_NAME_ALARM_LOCATION_RADIUS = "alarm_location_radius";
        public static final String COLUMN_NAME_ALARM_DAY_OF_WEEK = "alarm_day_of_week";

        public static final String COLUMN_NAME_NULLABLE = COLUMN_NAME_ALARM_TITLE;

        public static final String[] PROJECTION_ALARM_DETAILS = new String[] {
                _ID,
                COLUMN_NAME_ALARM_TITLE,
                COLUMN_NAME_ALARM_TIME,
                COLUMN_NAME_ALARM_LOCATION_LATITUDE,
                COLUMN_NAME_ALARM_LOCATION_LONGITUDE,
                COLUMN_NAME_ALARM_LOCATION_RADIUS,
                COLUMN_NAME_ALARM_DAY_OF_WEEK
        };
    }
}
