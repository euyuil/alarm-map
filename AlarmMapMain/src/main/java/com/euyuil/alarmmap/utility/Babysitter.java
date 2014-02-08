package com.euyuil.alarmmap.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.euyuil.alarmmap.service.AlarmService;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Consider moving it into AlarmService.
 * @author Yue
 * @version 0.0.2014.01.12
 */
public class Babysitter {

    private static String TAG = "Babysitter";

    /**
     * Invoke this method internally in the provider, each time after the
     * alarm being inserted, updated, or deleted.
     * And if the system time settings are changed, this method should be
     * invoked, too. Including time and date changes, and time-zone changes.
     * Every time the system boots up, it should be invoked, too.
     * And if the Daylight Saving Time changes, you should call this method.
     * You can pass in the root URI of alarms, which means all the alarms
     * should be taken care of.
     * @param uri The URI of the alarm, which should be taken care of.
     */
    public static void takeCareOf(Context context, Uri uri) {
        // TODO Check this query.
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null)
            throw new IllegalArgumentException("Cannot get alarm cursor by given arguments");
        if (cursor.moveToFirst()) {
            do {
                ContentValues alarm = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, alarm);
                takeCareOf(context, alarm);
            } while (cursor.moveToNext());
        }
    }

    private static void takeCareOf(Context context, ContentValues alarm) {

        Uri uri = AlarmUtils.getUri(alarm);

        if (AlarmUtils.getState(alarm) == AlarmUtils.AlarmState.DISABLED) {
            unregisterAlarm(context, uri);
            return;
        }

        // TODO Check uses time and location.

        Date now = new Date();
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
            registerAlarm(context, uri, startTime);
            return;
        }

        // alarm.getUsesRepeat() == true
        for (int i = 0; i < 7; ++i) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(startTime);
            calendar.add(GregorianCalendar.DATE, i);
            int calendarWeekday = calendar.get(GregorianCalendar.DAY_OF_WEEK);
            if (AlarmUtils.getDayOfWeek(alarm, calendarWeekday)) {
                Date time = calendar.getTime();
                registerAlarm(context, uri, time);
                return;
            }
        }
    }

    private static void registerAlarm(Context context, Uri uri, Date nextRingingDate) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, uri);

        alarmManager.cancel(pendingIntent);

        long triggerAtMillis = nextRingingDate.getTime();

        Log.i(TAG, String.format("register %s %d %d",
                uri.toString(),
                triggerAtMillis,
                triggerAtMillis - System.currentTimeMillis()));

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    private static void unregisterAlarm(Context context, Uri uri) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, uri);

        Log.i(TAG, String.format("unregister %s", uri.toString()));

        alarmManager.cancel(pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context, Uri uri) {

        Intent intent = new Intent(context, AlarmService.class)
                .setAction(AlarmService.ACTION)
                .setData(uri);

        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
