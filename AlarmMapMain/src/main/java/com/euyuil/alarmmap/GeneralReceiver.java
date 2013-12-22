package com.euyuil.alarmmap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.euyuil.alarmmap.service.AlarmService;

/**
 * Created by Yue on 13-10-29.
 */

public class GeneralReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmServiceIntent = new Intent(context, AlarmService.class);

        context.startService(alarmServiceIntent);
    }
}
