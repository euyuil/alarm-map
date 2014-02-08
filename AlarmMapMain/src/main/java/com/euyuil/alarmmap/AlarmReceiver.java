package com.euyuil.alarmmap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Yue on 13-10-29.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmServiceIntent = new Intent(context, AlarmService.class);

        context.startService(alarmServiceIntent);
    }
}
