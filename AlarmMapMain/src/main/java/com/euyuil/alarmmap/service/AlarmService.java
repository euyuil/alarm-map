package com.euyuil.alarmmap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.utility.AlarmUtility;

import java.util.Date;

/**
 * Alarm service that handles alarms.
 * @author EUYUIL
 * @version 0.0.20131022
 */

public class AlarmService extends Service {

    public static final String TAG = "AlarmService";
    public static final String ACTION = "com.euyuil.alarmmap.service.AlarmService";

    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, String.format("onStartCommand %s", intent.getData()));

        Alarm alarm = Alarm.findByUri(getApplicationContext(), intent.getData());

        if (AlarmUtility.clearDateButPreserveTime(alarm.getTimeOfDay()).getTime() >=
                AlarmUtility.clearDateButPreserveTime(new Date()).getTime()) {

            // Time is up, then look for repeat flag.

            if (alarm.getRepeat()) {

                // Alarm was set to repeat, look for weekday settings.

                // TODO if alarm.weekday contains today's weekday, it will be ringing.

            } else {

                // TODO alarm was not set to repeat, will be ringing.

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
