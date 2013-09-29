package com.euyuil.alarmmap;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Yue on 13-9-29.
 */

public class AlarmListAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public AlarmListAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.list_item_alarm, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView timeOfDay = (TextView) view.findViewById(R.id.time_of_day);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView dayOfWeek = (TextView) view.findViewById(R.id.day_of_week);
        CheckBox available = (CheckBox) view.findViewById(R.id.available);

        Alarm alarm = Alarm.fromCursor(cursor);

        title.setText(alarm.getTitle());
        timeOfDay.setText(new SimpleDateFormat("HH:mm").format(alarm.getTimeOfDay()));
        location.setText(alarm.getLocation().toString());
        dayOfWeek.setText(alarm.getDayOfWeek().toString());
        available.setChecked(alarm.getAvailable());
    }
}
