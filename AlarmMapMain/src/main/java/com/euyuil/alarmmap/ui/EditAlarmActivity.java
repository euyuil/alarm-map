package com.euyuil.alarmmap.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.euyuil.alarmmap.AlarmUtils;
import com.euyuil.alarmmap.PreferenceUtils;
import com.euyuil.alarmmap.R;
import com.euyuil.alarmmap.provider.AlarmContract;

import static android.preference.Preference.OnPreferenceChangeListener;
import static android.preference.Preference.OnPreferenceClickListener;

@SuppressWarnings("deprecation")
public class EditAlarmActivity extends PreferenceActivity {

    private static final String TAG = "EditAlarmActivity";

    private Uri uri;
    private ContentValues initialAlarm;

    private CheckBoxPreference enabled;
    private EditTextPreference title;
    private CheckBoxPreference usesTimeOfDay;
    private Preference timeOfDay;
    private CheckBoxPreference usesRepeat;
    private MultiSelectListPreference repeat;
    private CheckBoxPreference usesLocation;
    private Preference location;
    private RingtonePreference ringtone;
    private CheckBoxPreference vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("_edit_alarm");
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);

        addPreferencesFromResource(R.xml.pref_edit_alarm);

        enabled = (CheckBoxPreference) findPreference("enabled");
        title = (EditTextPreference) findPreference("title");
        usesTimeOfDay = (CheckBoxPreference) findPreference("usesTimeOfDay");
        timeOfDay = findPreference("timeOfDay");
        usesRepeat = (CheckBoxPreference) findPreference("usesRepeat");
        repeat = (MultiSelectListPreference) findPreference("repeat");
        usesLocation = (CheckBoxPreference) findPreference("usesLocation");
        location = findPreference("location");
        ringtone = (RingtonePreference) findPreference("ringtone");
        vibrate = (CheckBoxPreference) findPreference("vibrate");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent == null) {
            Log.e(TAG, "onPostCreate: cannot edit alarm because no intent specified");
            return;
        }

        uri = intent.getData();

        if (uri == null) {
            Log.e(TAG, "onPostCreate: cannot edit alarm because alarm URI not specified");
            return;
        }

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null || !cursor.moveToFirst() || cursor.getCount() != 1) {
            Log.e(TAG, String.format("onPostCreate: cannot find alarm %s", uri));
            return;
        }

        initialAlarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, initialAlarm);

        enabled.setChecked(AlarmUtils.getState(initialAlarm) != AlarmUtils.AlarmState.DISABLED);
        enabled.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean value = (o != null) && (Boolean) o;
                ContentValues alarm = new ContentValues();
                if (value)
                    AlarmUtils.setState(alarm, AlarmUtils.AlarmState.ENABLED);
                else
                    AlarmUtils.setState(alarm, AlarmUtils.AlarmState.DISABLED);
                getContentResolver().update(uri, alarm, null, null);
                return true; // TODO Meaning of this?
            }
        });

        bindEditTextPreference(title);
        bindCheckBoxPreference(usesTimeOfDay, "Uses time of day", "Do not use time of day"); // TODO To Resource
        bindCheckBoxPreference(usesRepeat, "Uses repeat", "Do not use repeat");
        bindCheckBoxPreference(usesLocation, "Uses location", "Do not use location");

        timeOfDay.setSummary(AlarmUtils.getTimeOfDayAsString(initialAlarm));
        timeOfDay.setOnPreferenceClickListener(new OnTimeOfDayClickListener());

        repeat.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String value = o.toString();
                int weekday = Integer.valueOf(value);
                return true; // TODO Finish this method.
            }
        });

        location.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(EditAlarmActivity.this,
                        EditLocationActivity.class).setData(uri);
                startActivity(intent);
                return true;
            }
        });
    }

    private void bindEditTextPreference(EditTextPreference preference) {
        preference.setText(initialAlarm.getAsString(preference.getKey()));
        PreferenceUtils.bindPreferenceSummaryToValue(preference,
                getPreferenceManager().getSharedPreferences(),
                new OnEditTextPreferenceChangeListener());
    }

    private void bindCheckBoxPreference(CheckBoxPreference preference,
                                        String checkedString, String uncheckedString) {
        boolean value = AlarmUtils.getBoolean(initialAlarm, preference.getKey());
        preference.setChecked(value);
        OnCheckBoxPreferenceChangeListener listener =
                new OnCheckBoxPreferenceChangeListener(checkedString, uncheckedString);
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference, value);
    }

    private class OnTimeOfDayClickListener implements OnPreferenceClickListener {

        @Override
        public boolean onPreferenceClick(Preference preference) {

            final Context context = EditAlarmActivity.this;

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View dialogView = layoutInflater.inflate(R.layout.dialog_edit_time_of_day, null);

            new AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setPositiveButton("OK", new OnTimeOfDayOkClickListener(dialogView))
                    .setNegativeButton("Cancel", new OnTimeOfDayCancelClickListener(dialogView))
                    .create()
                    .show();

            return true;
        }
    }

    private class OnTimeOfDayOkClickListener implements DialogInterface.OnClickListener {

        private final View dialogView;

        public OnTimeOfDayOkClickListener(View dialogView) {
            this.dialogView = dialogView;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            Context context = dialogView.getContext();
            TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
            assert context != null;
            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
            ContentValues alarm = new ContentValues();
            AlarmUtils.setBoolean(alarm,
                    AlarmContract.COLUMN_NAME_USES_TIME_OF_DAY, true);
            AlarmUtils.setTimeOfDay(alarm,
                    timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            timeOfDay.setSummary(AlarmUtils.getTimeOfDayAsString(alarm));
            context.getContentResolver().update(uri, alarm, null, null);
        }
    }

    private class OnTimeOfDayCancelClickListener implements DialogInterface.OnClickListener {

        private final View dialogView;

        public OnTimeOfDayCancelClickListener(View dialogView) {
            this.dialogView = dialogView;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Context context = dialogView.getContext();
            assert context != null;
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
        }
    }

    private class OnEditTextPreferenceChangeListener implements OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String value = o.toString();
            ContentValues alarm = new ContentValues();
            alarm.put(preference.getKey(), value);
            getContentResolver().update(uri, alarm, null, null);
            return true;
        }
    }

    private class OnCheckBoxPreferenceChangeListener implements OnPreferenceChangeListener {

        private final String checkedString;
        private final String uncheckedString;

        public OnCheckBoxPreferenceChangeListener(String checkedString, String uncheckedString) {
            this.checkedString = checkedString;
            this.uncheckedString = uncheckedString;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            boolean value = (o != null) && (Boolean) o;
            preference.setSummary(value ? checkedString : uncheckedString);
            ContentValues alarm = new ContentValues();
            AlarmUtils.setBoolean(alarm, preference.getKey(), value);
            getContentResolver().update(uri, alarm, null, null);
            return true;
        }
    }
}
