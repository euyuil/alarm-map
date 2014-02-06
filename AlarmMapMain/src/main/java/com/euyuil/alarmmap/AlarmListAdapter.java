package com.euyuil.alarmmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.euyuil.alarmmap.provider.AppDbHelper;

/**
 * List view adapter for alarm entities.
 * @author EUYUIL
 * @version 0.0.20130929
 */
public class AlarmListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public AlarmListAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.list_item_alarm, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView timeOfDay = (TextView) view.findViewById(R.id.time_of_day);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView dayOfWeek = (TextView) view.findViewById(R.id.day_of_week);
        CheckBox available = (CheckBox) view.findViewById(R.id.available);

        final Alarm alarm = AppDbHelper.getObject(cursor, Alarm.class);

        title.setText(alarm.getTitle());
        timeOfDay.setText(alarm.getTimeOfDay().toString());
        location.setText(alarm.getLocation().toString());
        dayOfWeek.setText(alarm.getDayOfWeek().toString());
        available.setChecked(alarm.getAvailable());

        timeOfDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = inflater.inflate(R.layout.dialog_edit_alarm_time_of_day, null);
                new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
                                alarm.setTimeOfDay(new Alarm.TimeOfDay(
                                        timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                                alarm.update();
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
                Intent intent = new Intent(context, EditAlarmLocationActivity.class);
                intent.putExtra("alarm", alarm.getId());
                context.startActivity(intent);
            }
        });
    }
}
