package com.euyuil.alarmmap.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.AlarmApplication;
import com.euyuil.alarmmap.service.AlarmService;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AlarmRegisterUtility {

    public static final String TAG = "AlarmDateTimeUtility";

    public static void registerAll() {
        // TODO
    }

    public static void register(@NotNull Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager)
                AlarmApplication.context().getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(AlarmApplication.context(), alarm);

        alarmManager.cancel(pendingIntent);

        if (!alarm.getAvailable())
            return;

        Date now = new Date();
        Date nextRingingDate = AlarmDateTimeUtility.getNextRingingDateTime(now, alarm);

        long triggerAtMillis = nextRingingDate.getTime();

        Log.i(TAG, String.format("register %s %s %d %d",
                alarm.toUri(),
                alarm.getTimeOfDay().toString(),
                triggerAtMillis,
                triggerAtMillis - System.currentTimeMillis()));

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    public static void unregister(Context context, Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, alarm);

        Log.i(TAG, String.format("unregister %s", alarm.toUri()));

        alarmManager.cancel(pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context, Alarm alarm) {

        Intent intent = new Intent(context, AlarmService.class)
                .setAction(AlarmService.ACTION)
                .setData(alarm.toUri());

        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
