package com.euyuil.alarmmap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author EUYUIL
 * @version 0.0.20131029
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmServiceIntent = new Intent(context, AlarmService.class);

        context.startService(alarmServiceIntent);
    }
}
