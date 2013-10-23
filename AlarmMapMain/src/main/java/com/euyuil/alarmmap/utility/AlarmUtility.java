package com.euyuil.alarmmap.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.service.AlarmService;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utilities that help you register alarms in system.
 * TODO When timezone changes, will the alarms work?
 * @author EUYUIL
 * @version 0.0.20131023
 */

public class AlarmUtility {

    public static final String TAG = "AlarmUtility";

    public static Date clearDateButPreserveTime(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(2000, GregorianCalendar.JANUARY, 1);
        return calendar.getTime();
    }

    @SuppressWarnings("MagicConstant")
    public static Date timeAfterNow(Date date) {

        GregorianCalendar nowCalendar = new GregorianCalendar();
        GregorianCalendar dateCalendar = new GregorianCalendar();

        nowCalendar.setTime(new Date());
        dateCalendar.setTime(date);

        dateCalendar.set(
                nowCalendar.get(GregorianCalendar.YEAR),
                nowCalendar.get(GregorianCalendar.MONTH),
                nowCalendar.get(GregorianCalendar.DAY_OF_MONTH));

        if (dateCalendar.getTime().getTime() <= nowCalendar.getTime().getTime())
            dateCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);

        return dateCalendar.getTime();
    }

    public static int alarmWeekdayToCalendarWeekday(Alarm.Weekday weekday) {
        return weekday.ordinal() - Alarm.Weekday.SUNDAY.ordinal() + GregorianCalendar.SUNDAY;
    }

    public static Alarm.Weekday calendarWeekdayToAlarmWeekday(int weekday) {
        return Alarm.Weekday.values()[weekday - GregorianCalendar.SUNDAY + Alarm.Weekday.SUNDAY.ordinal()];
    }

    @SuppressWarnings("MagicConstant")
    public static Date timeAfterNowAtWeekday(Date date, Alarm.Weekday weekday) {

        GregorianCalendar weekdayCalendar = new GregorianCalendar();
        GregorianCalendar dateCalendar = new GregorianCalendar();

        dateCalendar.setTime(date);

        weekdayCalendar.setTime(new Date());

        Alarm.Weekday nowWeekday = calendarWeekdayToAlarmWeekday(weekdayCalendar.get(GregorianCalendar.DAY_OF_WEEK));

        int diffDaysFromTargetWeekdayToNow = weekday.ordinal() - nowWeekday.ordinal();

        if (diffDaysFromTargetWeekdayToNow < 0)
            diffDaysFromTargetWeekdayToNow += 7;

        if (diffDaysFromTargetWeekdayToNow == 0) {

            // It is today.

            return timeAfterNow(date);

        }

        // It is not today, but the future.

        weekdayCalendar.add(GregorianCalendar.DAY_OF_MONTH, diffDaysFromTargetWeekdayToNow);

        dateCalendar.set(
                weekdayCalendar.get(GregorianCalendar.YEAR),
                weekdayCalendar.get(GregorianCalendar.MONTH),
                weekdayCalendar.get(GregorianCalendar.DAY_OF_MONTH));

        return dateCalendar.getTime();
    }

    public static void register(Context context, Alarm alarm) {

        if (!alarm.getAvailable())
            return;

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, alarm);

        // TODO Update alarm.timeOfDay property.

        long intervalMillis = alarm.getTimeOfDay().getTime() - new Date().getTime(); // TODO Adjust time to today's time.

        Log.i(TAG, String.format("register %s %d", alarm.getUri(), intervalMillis));

        alarmManager.cancel(pendingIntent);

        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), intervalMillis, pendingIntent);
    }

    public static void unregister(Context context, Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, alarm);

        Log.i(TAG, String.format("unregister %s", alarm.getUri()));

        alarmManager.cancel(pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context, Alarm alarm) {

        Intent intent = new Intent(context, AlarmService.class)
                .setAction(AlarmService.ACTION)
                .setData(alarm.getUri());

        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
