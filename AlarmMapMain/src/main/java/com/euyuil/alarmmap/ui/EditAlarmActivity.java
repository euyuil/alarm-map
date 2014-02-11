package com.euyuil.alarmmap.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.euyuil.alarmmap.AlarmUtils;
import com.euyuil.alarmmap.R;

public class EditAlarmActivity extends PreferenceActivity {

    private static final String TAG = "EditAlarmActivity";

    private CheckBoxPreference enabled;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("_edit_alarm");
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);

        addPreferencesFromResource(R.xml.pref_edit_alarm);

        enabled = (CheckBoxPreference) findPreference("enabled");
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent == null) {
            Log.e(TAG, "onPostCreate: cannot edit alarm because no intent specified");
            return;
        }

        Uri uri = intent.getData();

        if (uri == null) {
            Log.e(TAG, "onPostCreate: cannot edit alarm because alarm URI not specified");
            return;
        }

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            Log.e(TAG, String.format("onPostCreate: cannot find alarm %s", uri));
            return;
        }

        ContentValues alarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, alarm);

        enabled.setChecked(AlarmUtils.getState(alarm) != AlarmUtils.AlarmState.DISABLED);
    }
}
