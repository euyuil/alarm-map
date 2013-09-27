package com.euyuil.alarmmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;

/**
 * Created by Yue on 13-9-27.
 */

public class AlarmDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarm.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" +
            AlarmEntry._ID + " LONG PRIMARY KEY NOT NULL, " +
            AlarmEntry.COLUMN_NAME_ENTRY_TITLE + " TEXT, " +
            AlarmEntry.COLUMN_NAME_ENTRY_ALARM_TIME + " TIME, " +
            AlarmEntry.COLUMN_NAME_ENTRY_ALARM_LOCATION_LATITUDE + " LONG, " +
            AlarmEntry.COLUMN_NAME_ENTRY_ALARM_LOCATION_LONGITUDE + " LONG, " +
            AlarmEntry.COLUMN_NAME_ENTRY_ALARM_DAY_OF_WEEK + " INTEGER" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;

    public AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}