package com.euyuil.alarmmap;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import static android.preference.Preference.OnPreferenceChangeListener;

/**
 * @author EUYUIL
 * @version 0.0.20140212
 */
public class PreferenceUtils {

    private static final String SUMMARY_VALUE_NOT_SET = "(not set)"; // TODO To resource

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static OnPreferenceChangeListener bindSummaryToValueListener =
            new BindSummaryToValueListener();

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     */
    public static void bindPreferenceSummaryToValue(Preference preference) {
        bindPreferenceSummaryToValue(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext()), null);
    }

    public static void bindPreferenceSummaryToValue(Preference preference,
            SharedPreferences sharedPreferences, OnPreferenceChangeListener listener) {

        // Set the listener to watch for value changes.
        if (listener == null)
            preference.setOnPreferenceChangeListener(bindSummaryToValueListener);
        else
            preference.setOnPreferenceChangeListener(new BindSummaryToValueListener(listener));

        // Trigger the listener immediately with the preference's
        // current value.
        bindSummaryToValueListener.onPreferenceChange(preference,
                sharedPreferences.getString(preference.getKey(), SUMMARY_VALUE_NOT_SET));
    }

    private static class BindSummaryToValueListener
            implements OnPreferenceChangeListener {

        private final OnPreferenceChangeListener listener;

        public BindSummaryToValueListener() {
            listener = null;
        }

        public BindSummaryToValueListener(OnPreferenceChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }

            return listener == null || listener.onPreferenceChange(preference, value);
        }
    }
}
