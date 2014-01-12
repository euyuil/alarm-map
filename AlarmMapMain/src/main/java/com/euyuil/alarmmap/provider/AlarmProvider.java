package com.euyuil.alarmmap.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.euyuil.alarmmap.model.AlarmContract;
import com.euyuil.alarmmap.model.MyDbHelper;
import com.euyuil.alarmmap.utility.Babysitter;

/**
 * Provides alarm entities.
 * @author EUYUIL
 * @version 0.0.20130929
 */
public class AlarmProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY =
            "com.euyuil.alarmmap.provider";

    public static final String CONTENT_TYPE_DIR =
            "vnd.android.cursor.dir/vnd.com.euyuil.alarmmap.provider.alarm";

    public static final String CONTENT_TYPE_ITEM =
            "vnd.android.cursor.item/vnd.com.euyuil.alarmmap.provider.alarm";

    private static final int ALARM = 1;

    private static final int ALARM_ID = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // Gets all alarms.
        // Address - content://com.euyuil.alarmmap.provider/alarm
        matcher.addURI(CONTENT_AUTHORITY, AlarmContract.TABLE_NAME, ALARM);

        // Gets one alarm.
        // Address - content://com.euyuil.alarmmap.provider/alarm/1
        matcher.addURI(CONTENT_AUTHORITY,
                String.format("%s/#", AlarmContract.TABLE_NAME), ALARM_ID);
    }

    private MyDbHelper helper;

    @Override
    public boolean onCreate() {
        helper = new MyDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (matcher.match(uri)) {

            // Gets all alarms.
            case ALARM:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = AlarmContract._ID + " ASC";
                break;

            // Gets one alarm with ID.
            case ALARM_ID:
                selection = AlarmContract._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;

            // Error URI.
            default:
                // TODO Error handling
                return null;
        }

        Context context = getContext();
        SQLiteDatabase db = helper.getReadableDatabase();

        if (context == null || db == null)
            return null;

        Cursor cursor = db.query(AlarmContract.TABLE_NAME, projection,
                selection, selectionArgs, null, null, sortOrder);

        if (cursor == null)
            return null;

        cursor.setNotificationUri(context.getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (matcher.match(uri)) {

            case ALARM:
                break;
            default:
                // TODO Error handling
                return null;
        }

        Context context = getContext();
        SQLiteDatabase db = helper.getWritableDatabase();

        if (context == null || db == null)
            return null;

        long id = db.insert(AlarmContract.TABLE_NAME, AlarmContract.COLUMN_NAME_NULLABLE, values);

        if (id <= 0)
            return null;

        Uri alarm = Uri.parse("content://com.euyuil.alarmmap.provider/" + id);

        Babysitter.takeCareOf(alarm);

        context.getContentResolver().notifyChange(uri, null);
        // context.getContentResolver().notifyChange(alarm, null); TODO Is it necessary? Or the above one is redundant?

        return alarm;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (matcher.match(uri)) {

            // Delete all alarms, or with selection.
            case ALARM:
                break;

            // Delete alarm with ID, ignore client's selection.
            case ALARM_ID:
                selection = AlarmContract._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;

            // 0 rows affected.
            default:
                return 0;
        }

        Context context = getContext();
        SQLiteDatabase db = helper.getWritableDatabase();

        if (context == null || db == null)
            return 0;

        int ret = db.delete(AlarmContract.TABLE_NAME, selection, selectionArgs);

        if (ret > 0) {
            Babysitter.takeCareOf(uri);
            context.getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (matcher.match(uri)) {

            // Update all alarms, or with selection.
            case ALARM:
                break;

            // Update alarm with ID, ignore client's selection.
            case ALARM_ID:
                selection = AlarmContract._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;

            default:
                return 0;
        }

        Context context = getContext();
        SQLiteDatabase db = helper.getWritableDatabase();

        if (context == null || db == null)
            return 0;

        int ret = db.update(AlarmContract.TABLE_NAME, values, selection, selectionArgs);

        if (ret > 0) {
            Babysitter.takeCareOf(uri);
            context.getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }

    @Override
    public String getType(Uri uri) {

        switch (matcher.match(uri)) {

            case ALARM:
                return CONTENT_TYPE_DIR;

            case ALARM_ID:
                return CONTENT_TYPE_ITEM;

            default:
                return null;
        }
    }
}
