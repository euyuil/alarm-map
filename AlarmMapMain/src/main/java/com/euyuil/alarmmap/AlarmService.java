package com.euyuil.alarmmap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.euyuil.alarmmap.MainActivity;
import com.euyuil.alarmmap.R;
import com.euyuil.alarmmap.AlarmUtils;

import java.util.Date;

/**
 * Alarm service that handles alarms.
 * @author EUYUIL
 * @version 0.0.20131022
 */
public class AlarmService extends Service { // TODO Register content observers.

    public static final String TAG = "AlarmService";

    public static final String ACTION_PREPARE_ALARM =
            "com.euyuil.alarmmap.AlarmService.ACTION_PREPARE_ALARM";

    public static final String ACTION_TRIGGER_TIME_OF_DAY =
            "com.euyuil.alarmmap.AlarmService.ACTION_TRIGGER_TIME_OF_DAY";

    public static final String ACTION_TRIGGER_REPEAT =
            "com.euyuil.alarmmap.AlarmService.ACTION_TRIGGER_REPEAT";

    public static final String ACTION_TRIGGER_LOCATION =
            "com.euyuil.alarmmap.AlarmService.ACTION_TRIGGER_LOCATION";

    public static final int RESULT_OK = Activity.RESULT_OK;

    public static final int RESULT_ERROR = 10000;
    public static final int RESULT_ACTION_NOT_SPECIFIED = RESULT_ERROR + 1;
    public static final int RESULT_ACTION_NOT_FOUND = RESULT_ERROR + 2;
    public static final int RESULT_ALARM_NOT_SPECIFIED = RESULT_ERROR + 3;
    public static final int RESULT_ALARM_NOT_FOUND = RESULT_ERROR + 4;

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
        context.startService(new Intent(ACTION_PREPARE_ALARM, uri));
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

        String action = intent.getAction();
        Uri uri = intent.getData();

        if (uri == null)
            return RESULT_ALARM_NOT_SPECIFIED;
        if (action == null)
            return RESULT_ACTION_NOT_SPECIFIED;

        Log.i(TAG, String.format("onStartCommand %s %s", action, uri));

        if (action.equals(ACTION_PREPARE_ALARM))
            return prepareAlarm(uri);
        if (action.equals(ACTION_TRIGGER_TIME_OF_DAY))
            return triggerAlarmTimeOfDay(uri);
        if (action.equals(ACTION_TRIGGER_REPEAT))
            return triggerAlarmRepeat(uri);
        if (action.equals(ACTION_TRIGGER_LOCATION))
            return triggerAlarmLocation(uri);

        return RESULT_ACTION_NOT_FOUND;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    public int prepareAlarm(Uri uri) {
        // TODO Check this query.
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null)
            throw new IllegalArgumentException("Cannot get alarm cursor by given arguments");

        if (cursor.moveToFirst()) {
            do {

                // TODO Check if single alarm registration succeeds.

                ContentValues alarm = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, alarm);

                Uri currentAlarmUri = AlarmUtils.getUri(alarm);

                if (AlarmUtils.getState(alarm) == AlarmUtils.AlarmState.DISABLED)
                    unregisterTriggerAlarmTimeOfDay(currentAlarmUri);

                Date triggerTime = AlarmUtils.getTimeOfDayNextTrigger(alarm, null);

                if (triggerTime == null)
                    continue;

                registerTriggerAlarmTimeOfDay(currentAlarmUri, triggerTime);

            } while (cursor.moveToNext());
        }
        return RESULT_OK;
    }

    private int registerTriggerAlarmTimeOfDay(Uri uri, Date nextRingingTime) {

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(
                this, 0, new Intent(ACTION_TRIGGER_TIME_OF_DAY, uri),
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        long triggerAtMillis = nextRingingTime.getTime();

        Log.i(TAG, String.format("register %s %d %d",
                uri.toString(), triggerAtMillis,
                triggerAtMillis - System.currentTimeMillis()));

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

        return RESULT_OK;
    }

    private int unregisterTriggerAlarmTimeOfDay(Uri uri) {

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(
                this, 0, new Intent(ACTION_TRIGGER_TIME_OF_DAY, uri),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(TAG, String.format("unregister %s", uri.toString()));

        alarmManager.cancel(pendingIntent);

        return RESULT_OK;
    }

    private int triggerAlarmTimeOfDay(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst())
            return RESULT_ALARM_NOT_FOUND;

        ContentValues alarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, alarm);

        // TODO Check whether the alarm should be rang or not.

        ringAlarm(alarm);

        return RESULT_OK;
    }

    private int triggerAlarmRepeat(Uri uri) {
        return RESULT_ERROR;
    }

    private int triggerAlarmLocation(Uri uri) {
        return RESULT_ERROR;
    }

    private void ringAlarm(ContentValues alarm) {

        Uri uri = AlarmUtils.getUri(alarm);
        Log.i(TAG, String.format("ringAlarm %s", uri));

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Alarm Map")
                .setContentText(uri.toString())
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
