package com.euyuil.alarmmap.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.euyuil.alarmmap.utility.AlarmUtils;

import java.util.Date;

/**
 * Alarm service that handles alarms.
 * @author EUYUIL
 * @version 0.0.20131022
 */
public class AlarmService extends Service { // TODO Register content observers.

    public static final String TAG = "AlarmService";
    public static final String ACTION = "com.euyuil.alarmmap.service.AlarmService";

    public static final int RESULT_OK = Activity.RESULT_OK;

    public static final int RESULT_CUSTOM = 10000;
    public static final int RESULT_URI_NOT_SPECIFIED = RESULT_CUSTOM + 1;
    public static final int RESULT_ALARM_NOT_FOUND = RESULT_CUSTOM + 2;

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
    public static void prepareAlarm(Context context, Uri uri) {
        // TODO Check this query.
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null)
            throw new IllegalArgumentException("Cannot get alarm cursor by given arguments");
        if (cursor.moveToFirst()) {
            do {
                ContentValues alarm = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, alarm);
                prepareAlarm(context, alarm);
            } while (cursor.moveToNext());
        }
    }

    private static void prepareAlarm(Context context, ContentValues alarm) {

        Uri uri = AlarmUtils.getUri(alarm);

        if (AlarmUtils.getState(alarm) == AlarmUtils.AlarmState.DISABLED)
            unregisterAlarm(context, uri);

        Date triggerTime = AlarmUtils.getTimeOfDayNextTrigger(alarm, null);

        if (triggerTime == null)
            return;

        registerAlarm(context, uri, triggerTime);
    }

    private static void registerAlarm(Context context, Uri uri, Date nextRingingDate) {

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntentOfAlarm(context, uri);

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
                context.getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntentOfAlarm(context, uri);

        Log.i(TAG, String.format("unregister %s", uri.toString()));

        alarmManager.cancel(pendingIntent);
    }

    private static PendingIntent getPendingIntentOfAlarm(Context context, Uri uri) {

        Intent intent = new Intent(context, AlarmService.class)
                .setAction(ACTION)
                .setData(uri);

        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    /**
     * Every time the Service starts, it should babysit all the alarms
     * if intent.getData() is empty, or URI to all alarms.
     * But if the intent.getData() is one alarm, just babysit that one.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri uri = intent.getData();

        Log.i(TAG, String.format("onStartCommand %s", uri));

        if (uri == null)
            return RESULT_URI_NOT_SPECIFIED;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst())
            return RESULT_ALARM_NOT_FOUND;

        ContentValues alarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, alarm);

        // TODO Check whether the alarm should be rang or not.

        ringAlarm(alarm);

        return RESULT_OK;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void ringAlarm(ContentValues alarm) {
        Uri uri = AlarmUtils.getUri(alarm);
        Log.i(TAG, String.format("ringAlarm %s", uri));
    }
}
