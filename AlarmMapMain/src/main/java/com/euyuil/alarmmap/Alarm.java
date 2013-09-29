package com.euyuil.alarmmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;

import java.util.Date;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;

/**
 * Created by Yue on 13-9-27.
 */

public class Alarm {

    private Long id;
    private String title;
    private Date alarmTime;
    private Location alarmLocation;
    private Double alarmLocationRadius; // TODO Integrate this.
    private Integer alarmDayOfWeek;

    public enum Weekday {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public static Alarm findById(Context context, Long id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, AlarmEntry._ID + " = ?",
                new String[] { id.toString() }, null);

        if (cursor != null && cursor.moveToFirst()) {

            Alarm alarm = new Alarm();

            alarm.setId(cursor.getLong(cursor.getColumnIndex(AlarmEntry._ID)));
            alarm.setTitle(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TITLE)));
            alarm.setAlarmTime(new Date(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TIME))));

            Location alarmLocation = new Location("content://com.euyuil.alarmmap.provider/alarm");
            alarmLocation.setLatitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE)));
            alarmLocation.setLongitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE)));
            alarm.setAlarmLocation(alarmLocation);

            alarm.setAlarmLocationRadius(cursor.getDouble(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_RADIUS)));
            alarm.setAlarmDayOfWeek(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK)));

            return alarm;
        }

        return null;
    }

    public static Cursor findAll(Context context) {
        return context.getContentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);
    }

    public boolean insert(Context context) {

        ContentValues values = new ContentValues();

        values.put(AlarmEntry.COLUMN_NAME_ALARM_TITLE, getTitle());

        if (getAlarmTime() != null)
            values.put(AlarmEntry.COLUMN_NAME_ALARM_TIME, getAlarmTime().getTime());

        if (getAlarmLocation() != null) {
            values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE, getAlarmLocation().getLatitude());
            values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE, getAlarmLocation().getLongitude());
        }

        values.put(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK, getAlarmDayOfWeek());

        Uri uri = context.getContentResolver().insert(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"), values);

        if (uri == null)
            return false;

        try {
            id = Long.parseLong(uri.getLastPathSegment());
        } catch (NumberFormatException nfe) {
            id = null;
        }

        return id != null;
    }

    public boolean delete(Context context) {
        // TODO
        return false;
    }

    public boolean update(Context context) {
        // TODO
        return false;
    }

    public boolean getAlarmDayOfWeek(Weekday weekday) {
        return alarmDayOfWeek != null && (alarmDayOfWeek & (1 << weekday.ordinal())) != 0;
    }

    public void setAlarmDayOfWeek(Weekday weekday, boolean flag) {
        if (alarmDayOfWeek == null)
            alarmDayOfWeek = 0;
        if (flag)
            alarmDayOfWeek = alarmDayOfWeek | (1 << weekday.ordinal());
        else
            alarmDayOfWeek = alarmDayOfWeek & ~(1 << weekday.ordinal());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAlarmDayOfWeek() {
        return alarmDayOfWeek;
    }

    public void setAlarmDayOfWeek(Integer alarmDayOfWeek) {
        this.alarmDayOfWeek = alarmDayOfWeek;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Location getAlarmLocation() {
        return alarmLocation;
    }

    public void setAlarmLocation(Location alarmLocation) {
        this.alarmLocation = alarmLocation;
    }

    public Double getAlarmLocationRadius() {
        return alarmLocationRadius;
    }

    public void setAlarmLocationRadius(Double alarmLocationRadius) {
        this.alarmLocationRadius = alarmLocationRadius;
    }
}
