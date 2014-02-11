package com.euyuil.alarmmap;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.euyuil.alarmmap.ui.AwakeAlarmActivity;
import com.euyuil.alarmmap.ui.MainActivity;

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

    public static final String ACTION_DISMISS_ALARM =
            "com.euyuil.alarmmap.AlarmService.ACTION_DISMISS_ALARM";

    public static final String ACTION_SNOOZE_ALARM =
            "com.euyuil.alarmmap.AlarmService.ACTION_SNOOZE_ALARM";

    public static final String ACTION_TRIGGER_TIME_OF_DAY =
            "com.euyuil.alarmmap.AlarmService.ACTION_TRIGGER_TIME_OF_DAY";

    public static final String ACTION_TRIGGER_REPEAT =
            "com.euyuil.alarmmap.AlarmService.ACTION_TRIGGER_REPEAT";

    public static final String ACTION_TRIGGER_LOCATION =
            "com.euyuil.alarmmap.AlarmService.ACTION_TRIGGER_LOCATION";

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
        context.startService(new Intent(context, AlarmService.class)
                .setAction(ACTION_PREPARE_ALARM).setData(uri));
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

        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();
        Uri uri = intent.getData();

        do {

            if (uri == null || action == null) {
                Log.i(TAG, "onStartCommand: started with no alarm URI or no action specified");
                break;
            }

            Log.i(TAG, String.format("onStartCommand: %s %s", action, uri));

            if (action.equals(ACTION_PREPARE_ALARM))
                prepareAlarm(uri);
            else if (action.equals(ACTION_DISMISS_ALARM))
                dismissAlarm(uri);
            else if (action.equals(ACTION_SNOOZE_ALARM))
                snoozeAlarm(uri);
            else if (action.equals(ACTION_TRIGGER_TIME_OF_DAY))
                triggerAlarmTimeOfDay(uri);
            else if (action.equals(ACTION_TRIGGER_REPEAT))
                triggerAlarmRepeat(uri);
            else if (action.equals(ACTION_TRIGGER_LOCATION))
                triggerAlarmLocation(uri);

        } while (false);

        return START_REDELIVER_INTENT; // TODO Is this appropriate?
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    public void prepareAlarm(Uri uri) {
        // TODO Check this query.
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null)
            throw new IllegalArgumentException("Cannot get alarm cursor by given arguments");

        if (cursor.moveToFirst()) {
            do {

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
    }

    public void dismissAlarm(Uri uri) {
        Log.i(TAG, String.format("dismissAlarm: %s", uri));
    }

    public void snoozeAlarm(Uri uri) {

    }

    private void registerTriggerAlarmTimeOfDay(Uri uri, Date nextRingingTime) {

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmService.class)
                .setAction(ACTION_TRIGGER_TIME_OF_DAY).setData(uri);

        PendingIntent pendingIntent = PendingIntent.getService(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        long triggerAtMillis = nextRingingTime.getTime();

        Log.i(TAG, String.format("registerTriggerAlarmTimeOfDay: %s %d %d",
                uri, triggerAtMillis, triggerAtMillis - System.currentTimeMillis()));

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    private void unregisterTriggerAlarmTimeOfDay(Uri uri) {

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmService.class)
                .setAction(ACTION_TRIGGER_TIME_OF_DAY).setData(uri);

        PendingIntent pendingIntent = PendingIntent.getService(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(TAG, String.format("unregisterTriggerAlarmTimeOfDay: %s", uri));

        alarmManager.cancel(pendingIntent);
    }

    private void triggerAlarmTimeOfDay(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()) {
            Log.e(TAG, String.format("triggerAlarmTimeOfDay: %s not found", uri));
            return;
        }

        Log.i(TAG, String.format("triggerAlarmTimeOfDay: %s", uri));

        ContentValues alarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, alarm);

        // TODO Check whether the alarm should be rang or not.

        wakeAlarm(alarm);
    }

    private void triggerAlarmRepeat(Uri uri) {
    }

    private void triggerAlarmLocation(Uri uri) {
    }

    private void wakeAlarm(ContentValues alarm) {

        Uri uri = AlarmUtils.getUri(alarm);
        Log.i(TAG, String.format("wakeAlarm %s", uri));

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, AwakeAlarmActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissIntent = new Intent(this, AlarmService.class)
                .setAction(ACTION_DISMISS_ALARM).setData(uri);

        PendingIntent deleteIntent = PendingIntent.getService(
                this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Alarm Map")
                .setContentText(uri.toString())
                .setContentIntent(contentIntent)
                .setDeleteIntent(deleteIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
