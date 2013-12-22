package com.euyuil.alarmmap;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by Yue on 13-10-29.
 */

public class AlarmApplication extends Application {

    private static AlarmApplication application;

    public static AlarmApplication instance() {
        return application;
    }

    public static Context context() {
        return instance().getApplicationContext();
    }

    public static ContentResolver contentResolver() {
        return instance().getContentResolver();
    }

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
    }
}
