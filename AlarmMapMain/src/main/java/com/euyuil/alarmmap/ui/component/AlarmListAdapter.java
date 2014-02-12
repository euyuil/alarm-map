package com.euyuil.alarmmap.ui.component;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.euyuil.alarmmap.AlarmUtils;
import com.euyuil.alarmmap.ui.EditAlarmActivity;
import com.euyuil.alarmmap.ui.EditLocationActivity;
import com.euyuil.alarmmap.R;
import com.euyuil.alarmmap.provider.AlarmContract;

import org.apache.http.auth.AUTH;

/**
 * List view adapter for alarm entities.
 * @author EUYUIL
 * @version 0.0.20130929
 */
public class AlarmListAdapter extends CursorAdapter {

    private static final String TAG = "AlarmListAdapter";

    private final Context context;
    private final LayoutInflater inflater;

    public AlarmListAdapter(Context context) {
        super(context, null, 0);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.list_item_alarm, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        View body = view.findViewById(R.id.item_body);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView timeOfDay = (TextView) view.findViewById(R.id.time_of_day);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView daysOfWeek = (TextView) view.findViewById(R.id.days_of_week);
        CheckBox enabled = (CheckBox) view.findViewById(R.id.enabled);

        ContentValues alarm = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, alarm);
        final Uri uri = AlarmUtils.getUri(alarm);

        title.setText(alarm.getAsString(AlarmContract.COLUMN_NAME_TITLE));
        if (AlarmUtils.getUsesTimeOfDay(alarm))
            timeOfDay.setText(AlarmUtils.getTimeOfDayAsString(alarm));
        else
            timeOfDay.setText("N/A"); // TODO To resource
        location.setText(AlarmUtils.getFriendlyLocationAddress(alarm));
        daysOfWeek.setText(AlarmUtils.getFriendlyDaysOfWeek(alarm)); // TODO
        enabled.setChecked(AlarmUtils.getState(alarm) != AlarmUtils.AlarmState.DISABLED);

        body.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if (context != null)
                    context.startActivity(
                            new Intent(context, EditAlarmActivity.class).setData(uri));
            }
        });

        enabled.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if (context == null) {
                    Log.e(TAG, "enabledOnClickListener: cannot get context");
                    return;
                }
                ContentValues theAlarm = AlarmUtils.findOne(view.getContext(), uri);
                if (AlarmUtils.getState(theAlarm) == AlarmUtils.AlarmState.DISABLED) {
                    AlarmUtils.setState(theAlarm, AlarmUtils.AlarmState.ENABLED);
                } else {
                    AlarmUtils.setState(theAlarm, AlarmUtils.AlarmState.DISABLED);
                }
                int result = context.getContentResolver().update(uri, theAlarm, null, null);
                if (result != 1) {
                    Log.e(TAG, String.format("enabledOnClickListener: set alarm %s state failed", uri));
                    return;
                }
            }
        });

        timeOfDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_edit_time_of_day, null);
                assert dialogView != null;
                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
                new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                                ContentValues newAlarmValues = new ContentValues();
                                AlarmUtils.setTimeOfDay(newAlarmValues,
                                        timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                                context.getContentResolver().update(uri, newAlarmValues, null, null);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
            }
        });

        location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditLocationActivity.class);
                context.startActivity(intent.setData(uri));
            }
        });
    }
}
