package com.euyuil.alarmmap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.utility.AlarmRegisterUtility;
import com.euyuil.alarmmap.utility.AlarmDateTimeUtility;

import java.util.Date;

/**
 * Alarm service that handles alarms.
 * @author EUYUIL
 * @version 0.0.20131022
 */

public class AlarmService extends Service { // TODO Register content observers.

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

        Date now = new Date();
        Date nextRingingDate = AlarmDateTimeUtility.getNextRingingDateTime(now, alarm);

        if (AlarmDateTimeUtility
                .clearDateButPreserveTime(nextRingingDate).getTime()
                >= AlarmDateTimeUtility
                .clearDateButPreserveTime(now).getTime()) {

            // Time is up, then look for repeat flag.

            if (alarm.getRepeat()) {

                // Alarm was set to repeat, look for weekday settings.

                if (alarm.getDayOfWeek(AlarmDateTimeUtility.getNowWeekday(now)))
                    ring(alarm);

            } else {
                ring(alarm);
            }

        } else {

            // Should calculate time diff if it's larger than 1 minute then use this.
            // Otherwise just register alarm manager manually.
            AlarmRegisterUtility.register(getApplicationContext(), alarm);

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void ring(Alarm alarm) {
        Log.i(TAG, String.format("ring %s", alarm.getUri()));
    }
}
