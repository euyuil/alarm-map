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

        // public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
        public static final String COLUMN_NAME_ENTRY_TITLE = "title";
        public static final String COLUMN_NAME_ENTRY_ALARM_TIME = "alarm_time";
        // public static final String COLUMN_NAME_ENTRY_ALARM_LOCATION = "alarm_location";
        public static final String COLUMN_NAME_ENTRY_ALARM_LOCATION_LATITUDE = "alarm_location_latitude";
        public static final String COLUMN_NAME_ENTRY_ALARM_LOCATION_LONGITUDE = "alarm_location_longitude";
        public static final String COLUMN_NAME_ENTRY_ALARM_DAY_OF_WEEK = "alarm_day_of_week";

        public static final String COLUMN_NAME_NULLABLE = COLUMN_NAME_ENTRY_TITLE;
    }
}
