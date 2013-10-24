package com.euyuil.alarmmap.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.service.AlarmService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmRegisterUtility {

    public static final String TAG = "AlarmDateTimeUtility";

    public static void registerAll(Context context) {
        // TODO
    }

    public static void register(Context context, Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, alarm);

        alarmManager.cancel(pendingIntent);

        if (!alarm.getAvailable())
            return;

        Date now = new Date();

        if (alarm.getRepeat())
            alarm.setTimeOfDay(AlarmDateTimeUtility
                    .getNextRingingDateTimeFromRepeatAlarm(now, alarm));
        else
            alarm.setTimeOfDay(AlarmDateTimeUtility
                    .getFirstOccurrenceOfThisTimeInTheFuture(now, alarm.getTimeOfDay()));

        long triggerAtMillis = alarm.getTimeOfDay().getTime();

        Log.i(TAG, String.format("register %s %s %d %d",
                alarm.getUri(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(alarm.getTimeOfDay()),
                triggerAtMillis,
                triggerAtMillis - System.currentTimeMillis()));

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    public static void unregister(Context context, Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, alarm);

        Log.i(TAG, String.format("unregister %s", alarm.getUri()));

        alarmManager.cancel(pendingIntent);
    }

    public static PendingIntent getPendingIntent(Context context, Alarm alarm) {

        Intent intent = new Intent(context, AlarmService.class)
                .setAction(AlarmService.ACTION)
                .setData(alarm.getUri());

        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}