package com.euyuil.alarmmap.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.euyuil.alarmmap.model.AlarmContract.AlarmEntry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Alarm database helper.
 * @author EUYUIL
 * @version 0.0.20130927
 */

public class MyDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarm.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" +
            AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE + " BOOLEAN NOT NULL DEFAULT 0, " +
            AlarmEntry.COLUMN_NAME_ALARM_TITLE + " TEXT, " +
            AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY + " INTEGER, " +
            AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE + " REAL, " +
            AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE + " REAL, " +
            AlarmEntry.COLUMN_NAME_ALARM_LOCATION_RADIUS + " REAL, " +
            AlarmEntry.COLUMN_NAME_ALARM_LOCATION_ADDRESS + " TEXT, " +
            AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK + " INTEGER, " +
            AlarmEntry.COLUMN_NAME_ALARM_REPEAT + " BOOLEAN NOT NULL DEFAULT 0, " +
            AlarmEntry.COLUMN_NAME_ALARM_RINGTONE + " TEXT" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;

    public static <T> T getObject(Cursor cursor, Class<T> clazz) {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
        return getObject(contentValues, clazz);
    }

    public static <T> T getObject(ContentValues contentValues, Class<T> clazz) {

        Object object = getObject(clazz);

        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {

            Method setter = createSetter(entry.getKey(), clazz);

            if (setter == null)
                continue;

            try {
                setter.invoke(object, entry.getValue());
            } catch (Exception e) {
                // e.printStackTrace();
                throw new RuntimeException("Cannot set " + entry.getKey() + " property of object");
            }
        }

        return (T) object;
    }

    private static <T> T getObject(Class<T> clazz) {

        Constructor<?> constructor;
        Object object;

        try {
            constructor = clazz.getConstructor(clazz);
            object = constructor.newInstance();
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException("Cannot instantiate a new object of " + clazz.getName());
        }

        return (T) object;
    }

    private static <T> Method createGetter(String propertyName, Class<T> clazz) {

        Method method = null;

        try {
            method = clazz.getMethod(createMethodName(propertyName, "get"), clazz);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        if (method == null) {
            try {
                method = clazz.getMethod(createMethodName(propertyName, "is"), clazz);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

        return method;
    }

    private static <T> Method createSetter(String propertyName, Class<T> clazz) {

        Method method = null;

        try {
            method = clazz.getMethod(createMethodName(propertyName, "set"), clazz);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return method;
    }

    private static String createMethodName(String propertyName, String prefix) {
        String result = null;
        if (propertyName != null) {
            String bos = propertyName.substring(0, 1).toUpperCase();
            String eos = propertyName.substring(1, propertyName.length());
            result = prefix + bos + eos;
        }
        return result;
    }

    public MyDbHelper(Context context) {
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
