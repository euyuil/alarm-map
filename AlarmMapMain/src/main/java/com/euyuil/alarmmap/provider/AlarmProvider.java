package com.euyuil.alarmmap.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;
import com.euyuil.alarmmap.AlarmDbHelper;

/**
 * Provides alarm entities.
 * @author EUYUIL
 * @version 0.0.20130929
 */

public class AlarmProvider extends ContentProvider {

    private static final int ALARM = 1;
    private static final int ALARM_ID = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI("com.euyuil.alarmmap.provider", "alarm", ALARM);
        matcher.addURI("com.euyuil.alarmmap.provider", "alarm/#", ALARM_ID);
    }

    private AlarmDbHelper alarmDbHelper;

    @Override
    public boolean onCreate() {
        alarmDbHelper = new AlarmDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (matcher.match(uri)) {
            case ALARM:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = AlarmEntry._ID + " ASC";
                break;
            case ALARM_ID:
                selection = AlarmEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;
            default:
                // TODO Error handling
                return null;
        }

        SQLiteDatabase db = alarmDbHelper.getReadableDatabase();

        if (db == null)
            return null;

        Cursor cursor = db.query(AlarmEntry.TABLE_NAME, projection,
                selection, selectionArgs, null, null, sortOrder);

        if (cursor == null)
            return null;

        Context context = getContext();

        if (context == null)
            return null;

        cursor.setNotificationUri(context.getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (matcher.match(uri)) {

            case ALARM:
                return "vnd.android.cursor.dir/vnd.com.euyuil.alarmmap.provider.alarm";

            case ALARM_ID:
                return "vnd.android.cursor.item/vnd.com.euyuil.alarmmap.provider.alarm";

            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (matcher.match(uri)) {

            case ALARM:

                SQLiteDatabase db = alarmDbHelper.getWritableDatabase();

                if (db == null)
                    return null;

                Context context = getContext();

                if (context == null)
                    return null;

                long id = db.insert(AlarmEntry.TABLE_NAME, AlarmEntry.COLUMN_NAME_NULLABLE, values);

                if (id <= 0)
                    return null;

                context.getContentResolver().notifyChange(uri, null);

                return Uri.parse("content://com.euyuil.alarmmap.provider/" + id);
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (matcher.match(uri)) {

            case ALARM:
                break;

            case ALARM_ID:
                selection = AlarmEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;

            default:
                return 0;
        }

        SQLiteDatabase db = alarmDbHelper.getWritableDatabase();

        if (db == null)
            return 0;

        Context context = getContext();

        if (context == null)
            return 0;

        int ret = db.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);

        if (ret > 0)
            context.getContentResolver().notifyChange(uri, null);

        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (matcher.match(uri)) {

            case ALARM:
                break;

            case ALARM_ID:
                selection = AlarmEntry._ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;

            default:
                return 0;
        }

        SQLiteDatabase db = alarmDbHelper.getWritableDatabase();

        if (db == null)
            return 0;

        Context context = getContext();

        if (context == null)
            return 0;

        int ret = db.update(AlarmEntry.TABLE_NAME, values, selection, selectionArgs);

        if (ret > 0)
            context.getContentResolver().notifyChange(uri, null);

        return ret;
    }
}
