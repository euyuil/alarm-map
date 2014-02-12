package com.euyuil.alarmmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.euyuil.alarmmap.provider.AlarmContract;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author EUYUIL
 * @version 0.0.20140206
 */
public class AlarmUtils {

    private static String TAG = "AlarmUtils";

    /**
     * Creates a default alarm, whose time is set to now.
     * @return A default alarm.
     */
    public static ContentValues createDefaultAlarm() {
        ContentValues alarm = new ContentValues();
        Date now = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        setTimeOfDay(alarm, calendar.get(GregorianCalendar.HOUR_OF_DAY),
                calendar.get(GregorianCalendar.MINUTE));
        setState(alarm, AlarmState.ENABLED);
        alarm.put(AlarmContract.COLUMN_NAME_USES_TIME_OF_DAY, true);
        return alarm;
    }

    public static ContentValues findOne(Context context, Uri uri) {
        if (context == null) {
            Log.e(TAG, "findOne: cannot get context");
            return null;
        }
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()) {
            Log.e(TAG, String.format("findOne: alarm %s not found", uri));
            return null;
        }
        ContentValues alarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, alarm);
        return alarm;
    }

    /**
     * Gets the URI of an alarm with ID given.
     * @param id The alarm ID.
     * @return The URI of the alarm.
     */
    public static Uri getUri(long id) {
        return Uri.parse(String.format("content://%s/%s/%d",
                AlarmContract.CONTENT_AUTHORITY, AlarmContract.TABLE_NAME, id));
    }

    /**
     * Gets the URI of an alarm with its ContentValues.
     * @param alarm The alarm ContentValues object.
     * @return The URI of the alarm.
     */
    public static Uri getUri(ContentValues alarm) {
        Long id = alarm.getAsLong(AlarmContract._ID);
        if (id == null)
            throw new IllegalArgumentException("The '_id' field of the alarm is null");
        return getUri(id);
    }

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
     * Gets the time that the alarm was waked last time.
     * @param alarm The alarm ContentValues object.
     * @return The time, or null if not applied.
     */
    public static Date getLastWakeTime(ContentValues alarm) {
        Long lastWakeTimeLong = alarm.getAsLong(AlarmContract.COLUMN_NAME_LAST_WAKE_TIME);
        if (lastWakeTimeLong == null)
            return null;
        Date lastWakeTime = new Date(lastWakeTimeLong);
        return lastWakeTime;
    }

    /**
     * Sets the time that the alarm was waked.
     * @param alarm The alarm ContentValues object.
     * @param lastWakeTime The time that the alarm was waked.
     */
    public static void setLastWakeTime(ContentValues alarm, Date lastWakeTime) {
        if (lastWakeTime == null)
            alarm.putNull(AlarmContract.COLUMN_NAME_LAST_WAKE_TIME);
        else
            alarm.put(AlarmContract.COLUMN_NAME_LAST_WAKE_TIME, lastWakeTime.getTime());
    }

    public static boolean getBoolean(ContentValues alarm, String key) {
        Integer value = alarm.getAsInteger(key);
        return (value != null) && (value != 0);
    }

    public static void setBoolean(ContentValues alarm, String key, boolean value) {
        alarm.put(key, (Integer) (value ? 1 : 0));
    }

    /**
     * Does the alarm use the time information?
     * If it does, the time condition should be satisfied
     * in order to get the alarm rang.
     * @param alarm The alarm ContentValues object.
     * @return Whether it uses time information or not.
     */
    public static boolean getUsesTimeOfDay(ContentValues alarm) {
        Integer usesTimeOfDay = alarm.getAsInteger(AlarmContract.COLUMN_NAME_USES_TIME_OF_DAY);
        if (usesTimeOfDay == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'usesTimeOfDay' is null");
        return usesTimeOfDay != 0;
    }

    public static boolean getSatisfiesTimeOfDay(ContentValues alarm) {
        // TODO
        return false;
    }

    /**
     * Gets the hour of the time of the day of the alarm.
     * @param alarm The alarm ContentValues object.
     * @return The hour of the time of the day.
     */
    public static int getHourFromTimeOfDay(ContentValues alarm) {
        if (!getUsesTimeOfDay(alarm))
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
        if (!getUsesTimeOfDay(alarm))
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
     * Gets the time of day of the alarm as a string.
     * @param alarm The alarm ContentValues object.
     * @return The time of day of the alarm as a string.
     */
    public static String getTimeOfDayAsString(ContentValues alarm) {
        return String.format("%02d:%02d",
                getHourFromTimeOfDay(alarm), getMinuteFromTimeOfDay(alarm));
    }

    /**
     * Sets the time of day property of the alarm.
     * @param alarm The alarm ContentValues object.
     * @param hour The hour field of the time of day property.
     * @param minute The minute field of the time of day property.
     */
    public static void setTimeOfDay(ContentValues alarm, int hour, int minute) {
        alarm.put(AlarmContract.COLUMN_NAME_TIME_OF_DAY, hour * 100 + minute);
    }

    /**
     * Gets the time when the alarm will trigger next time.
     * @param alarm The alarm ContentValues object.
     * @param now The time of now. If it's null, it will use system time.
     * @return The time when the alarm will trigger. Or null if it won't trigger by time.
     */
    public static Date getTimeOfDayNextTrigger(ContentValues alarm, Date now) {

        if (now == null)
            now = new Date();

        if (AlarmUtils.getState(alarm) == AlarmUtils.AlarmState.DISABLED ||
                !AlarmUtils.getUsesTimeOfDay(alarm))
            return null;

        GregorianCalendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(now);

        Date startTime;
        GregorianCalendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(now);
        startCalendar.set(GregorianCalendar.SECOND, 0);
        startCalendar.set(GregorianCalendar.MILLISECOND, 0);

        // Let's see if today's HH:mm has been reached.
        int nowHour = nowCalendar.get(GregorianCalendar.HOUR_OF_DAY);
        int nowMinute = nowCalendar.get(GregorianCalendar.MINUTE);
        int alarmHour = AlarmUtils.getHourFromTimeOfDay(alarm);
        int alarmMinute = AlarmUtils.getMinuteFromTimeOfDay(alarm);
        if ((nowHour < alarmHour) || (nowHour == alarmHour && nowMinute < alarmMinute)) {
            // HH:mm of today hasn't been reached.
        } else {
            // HH:mm of today has been reached, so start from tomorrow.
            startCalendar.add(GregorianCalendar.DATE, 1);
        }
        startCalendar.set(GregorianCalendar.HOUR_OF_DAY, alarmHour);
        startCalendar.set(GregorianCalendar.MINUTE, alarmMinute);
        startTime = startCalendar.getTime();

        // Calculate the time for next ringing.
        if (!AlarmUtils.getUsesRepeat(alarm)) {
            // startTime is fairly enough.
            // Will ring at startTime.
            return startTime;
        }

        // alarm.getUsesRepeat() == true
        for (int i = 0; i < 7; ++i) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(startTime);
            calendar.add(GregorianCalendar.DATE, i);
            int calendarWeekday = calendar.get(GregorianCalendar.DAY_OF_WEEK);
            if (AlarmUtils.getDayOfWeek(alarm, calendarWeekday)) {
                Date time = calendar.getTime();
                return time;
            }
        }

        throw new IllegalArgumentException("The alarm repeats but no weekday was set");
    }

    /**
     * Does the alarm use the location information?
     * If it does, the location condition should be satisfied
     * in order to get the alarm rang.
     * @param alarm The alarm ContentValues object.
     * @return Whether it uses location information or not.
     */
    public static boolean getUsesLocation(ContentValues alarm) {
        Integer usesLocation = alarm.getAsInteger(AlarmContract.COLUMN_NAME_USES_LOCATION);
        if (usesLocation == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'usesLocation' is null");
        return usesLocation != 0;
    }

    public static boolean getSatisfiesLocation(ContentValues alarm) {
        // TODO
        return false;
    }

    /**
     * Gets the friendly location address of an alarm.
     * If the address was specified, it will be that address.
     * Otherwise, it will be a coordinate on the earth.
     * @param alarm The alarm ContentValues object.
     * @return The friendly location address.
     */
    public static String getFriendlyLocationAddress(ContentValues alarm) {
        String locationAddress = alarm.getAsString(AlarmContract.COLUMN_NAME_LOCATION_ADDRESS);
        // TODO Fallback strings.
        return locationAddress;
    }

    /**
     * Does the alarm repeat?
     * If it does, the alarm will be still enabled after it rang,
     * and the weekday information could be specified.
     * @param alarm The alarm ContentValues object.
     * @return Whether it repeats or not.
     */
    public static boolean getUsesRepeat(ContentValues alarm) {
        Integer usesRepeat = alarm.getAsInteger(AlarmContract.COLUMN_NAME_USES_REPEAT);
        if (usesRepeat == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'usesRepeat' is null");
        return usesRepeat != 0;
    }

    public static boolean getSatisfiesRepeat(ContentValues alarm) {
        // TODO
        return false;
    }

    /**
     * Gets the weekdays that the repeated alarm is triggered.
     * @param alarm The alarm ContentValues object.
     * @return The mask of the weekdays that the repeated alarm is triggered.
     */
    public static int getDaysOfWeek(ContentValues alarm) {
        if (!getUsesRepeat(alarm))
            throw new UnsupportedOperationException("Cannot get 'daysOfWeek' of the alarm " +
                    "because it doesn't repeat");
        Integer daysOfWeek = alarm.getAsInteger(AlarmContract.COLUMN_NAME_REPEAT_DAYS_OF_WEEK);
        if (daysOfWeek == null)
            throw new IllegalArgumentException("The ContentValues object is not a valid alarm " +
                    "because its 'daysOfWeek' is null and it repeats");
        return daysOfWeek;
    }

    public static String getFriendlyDaysOfWeek(ContentValues alarm) {
        return AlarmUtils.getUsesRepeat(alarm) ?
                String.valueOf(AlarmUtils.getDaysOfWeek(alarm)) : "N/A";
    }

    /**
     * Gets whether the specified weekday of the repeated alarm is set or not.
     * @param alarm The alarm ContentValues object.
     * @param weekday The weekday.
     * @return Whether the specified weekday of the repeated alarm is set or not.
     */
    public static boolean getDayOfWeek(ContentValues alarm, int weekday) {
        if (weekday < GregorianCalendar.SUNDAY || weekday > GregorianCalendar.SATURDAY)
            throw new IllegalArgumentException("The 'weekday' parameter should be between 1 and 7");
        int daysOfWeek = getDaysOfWeek(alarm);
        return (daysOfWeek & (1 << (weekday - GregorianCalendar.SUNDAY))) != 0;
    }

    public static enum AlarmState {
        DISABLED, ENABLED, RINGING, BREAK, SNOOZED
    }
}
