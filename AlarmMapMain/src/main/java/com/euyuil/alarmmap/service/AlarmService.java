package com.euyuil.alarmmap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Yue on 13-10-22.
 */

public class AlarmService extends Service {

    public static final String ACTION = "com.euyuil.alarmmap.service.AlarmService";

    public IBinder onBind(Intent intent) {
        return null;
    }
}
