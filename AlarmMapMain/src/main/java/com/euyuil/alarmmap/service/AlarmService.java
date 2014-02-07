package com.euyuil.alarmmap.service;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.euyuil.alarmmap.provider.AlarmContract;
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
