package com.euyuil.alarmmap.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Alarm database helper.
 * @author EUYUIL
 * @version 0.0.20130927
 */
public class AppDbHelper extends SQLiteOpenHelper {

    // Maybe it should be in another app-scope class.
    public static final String APP_CONTENT_AUTHORITY = "com.euyuil.alarmmap.provider";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarm.db";

    private static final String SQL_CREATE_TABLES = AlarmContract.SQL_CREATE_TABLE;
    private static final String SQL_DROP_TABLES = AlarmContract.SQL_DROP_TABLE;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
