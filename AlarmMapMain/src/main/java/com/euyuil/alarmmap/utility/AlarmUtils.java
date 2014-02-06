package com.euyuil.alarmmap.utility;

import android.content.ContentValues;

import com.euyuil.alarmmap.provider.AlarmContract;

/**
 * Created by t-yul on 2/6/14.
 */
public class AlarmUtils {

    /**
     * Gets the state of the alarm.
     * @param alarm The alarm ContentValues object.
     * @return The state of the alarm.
     */
    public static AlarmState getState(ContentValues alarm) {
        String alarmStateString = alarm.getAsString(AlarmContract.COLUMN_NAME_STATE);
        if (alarmStateString == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its alarmState is null");
        return AlarmState.valueOf(alarmStateString.toUpperCase());
    }

    /**
     * Sets the state of the alarm.
     * @param alarm The alarm ContentValues object.
     * @param alarmState The state of the alarm.
     */
    public static void setState(ContentValues alarm, AlarmState alarmState) {
        alarm.put(AlarmContract.COLUMN_NAME_STATE, alarmState.toString());
    }

    /**
     * Does the alarm use the time information?
     * If it does, the time condition should be satisfied
     * in order to get the alarm rang.
     * @param alarm The alarm ContentValues object.
     * @return Whether it uses time information or not.
     */
    public static boolean getUsesTime(ContentValues alarm) {
        Boolean usesTime = alarm.getAsBoolean(AlarmContract.COLUMN_NAME_USES_TIME);
        if (usesTime == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'usesTime' is null");
        return usesTime;
    }

    /**
     * Gets the hour of the time of the day of the alarm.
     * @param alarm The alarm ContentValues object.
     * @return The hour of the time of the day.
     */
    public static int getHourFromTimeOfDay(ContentValues alarm) {
        if (!getUsesTime(alarm))
            throw new UnsupportedOperationException("Cannot get 'timeOfDay' of the alarm " +
                    "because it doesn't use time information");
        Integer timeOfDay = alarm.getAsInteger(AlarmContract.COLUMN_NAME_TIME_OF_DAY);
        if (timeOfDay == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'timeOfDay' is null but it uses time information");
        int hour = timeOfDay / 100;
        if (timeOfDay < 0 || timeOfDay > 2359 || hour < 0 || hour > 23)
            throw new IllegalArgumentException("The ContentValues object's 'timeOfDay' is invalid");
        return hour;
    }

    /**
     * Gets the minute of the hour of the time of the day of the alarm.
     * @param alarm The alarm ContentValues object.
     * @return The minute of the hour of the time of the day.
     */
    public static int getMinuteFromTimeOfDay(ContentValues alarm) {
        if (!getUsesTime(alarm))
            throw new UnsupportedOperationException("Cannot get 'timeOfDay' of the alarm " +
                    "because it doesn't use time information");
        Integer timeOfDay = alarm.getAsInteger(AlarmContract.COLUMN_NAME_TIME_OF_DAY);
        if (timeOfDay == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'timeOfDay' is null but it uses time information");
        int minute = timeOfDay % 100;
        if (timeOfDay < 0 || timeOfDay > 2359 || minute < 0 || minute > 59)
            throw new IllegalArgumentException("The ContentValues object's 'timeOfDay' is invalid");
        return minute;
    }

    /**
     * Does the alarm use the location information?
     * If it does, the location condition should be satisfied
     * in order to get the alarm rang.
     * @param alarm The alarm ContentValues object.
     * @return Whether it uses location information or not.
     */
    public static boolean getUsesLocation(ContentValues alarm) {
        Boolean usesLocation = alarm.getAsBoolean(AlarmContract.COLUMN_NAME_USES_LOCATION);
        if (usesLocation == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'usesLocation' is null");
        return usesLocation;
    }

    /**
     * Does the alarm repeat?
     * If it does, the alarm will be still enabled after it rang,
     * and the weekday information could be specified.
     * @param alarm The alarm ContentValues object.
     * @return Whether it repeats or not.
     */
    public static boolean getRepeat(ContentValues alarm) {
        Boolean repeat = alarm.getAsBoolean(AlarmContract.COLUMN_NAME_REPEAT);
        if (repeat == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'repeat' is null");
        return repeat;
    }

    /**
     * Gets the weekdays that the repeated alarm is triggered.
     * @param alarm The alarm ContentValues object.
     * @return The mask of the weekdays that the repeated alarm is triggered.
     */
    public static int getDaysOfWeek(ContentValues alarm) {
        if (!getRepeat(alarm))
            throw new UnsupportedOperationException("Cannot get 'daysOfWeek' of the alarm " +
                    "because it doesn't repeat");
        Integer daysOfWeek = alarm.getAsInteger(AlarmContract.COLUMN_NAME_DAYS_OF_WEEK);
        if (daysOfWeek == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'daysOfWeek' is null and it repeats");
        return daysOfWeek;
    }

    /**
     * Gets whether the specified weekday of the repeated alarm is set or not.
     * @param alarm The alarm ContentValues object.
     * @param weekday The weekday.
     * @return Whether the specified weekday of the repeated alarm is set or not.
     */
    public static boolean getDayOfWeek(ContentValues alarm, int weekday) {
        if (weekday < 1 || weekday > 7)
            throw new IllegalArgumentException("The 'weekday' parameter should be between 1 and 7");
        int daysOfWeek = getDaysOfWeek(alarm);
        return (daysOfWeek & (1 << (weekday - 1))) != 0;
    }

    public static enum AlarmState {
        DISABLED, ENABLED, RINGING, BREAK, SNOOZED
    }
}
