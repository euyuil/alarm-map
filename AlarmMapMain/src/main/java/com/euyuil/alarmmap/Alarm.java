package com.euyuil.alarmmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;

import java.util.Date;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;

/**
 * The model for Alarm object.
 * @author EUYUIL
 * @version 0.0.20130927
 */

public class Alarm {

    private Long id;
    private String title;
    private Boolean available;
    private Date timeOfDay;
    private Location location;
    private Double locationRadius; // TODO Integrate this.
    private String locationAddress;
    private Integer dayOfWeek;
    private Boolean repeat;
    private String ringtone;

    public enum Weekday {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public static Alarm findById(Context context, Long id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, AlarmEntry._ID + " = ?",
                new String[]{id.toString()}, null);

        if (cursor != null && cursor.moveToFirst())
            return fromCursor(cursor);

        return null;
    }

    public static Cursor findAll(Context context) {
        return context.getContentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);
    }

    public boolean insert(Context context) {

        Uri uri = context.getContentResolver().insert(getClassUri(), getContentValues());

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
        return context.getContentResolver().delete(getUri(), null, null) > 0;
    }

    public boolean update(Context context) {
        return context.getContentResolver().update(getUri(), getContentValues(), null, null) > 0;
    }

    public static Alarm fromCursor(Cursor cursor) {

        Alarm alarm = new Alarm();

        alarm.setId(cursor.getLong(cursor.getColumnIndex(AlarmEntry._ID)));
        alarm.setAvailable(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE)) != 0);
        alarm.setTitle(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TITLE)));
        alarm.setTimeOfDay(new Date(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY))));

        Location alarmLocation = new Location("content://com.euyuil.alarmmap.provider/alarm");
        alarmLocation.setLatitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE)));
        alarmLocation.setLongitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE)));
        alarm.setLocation(alarmLocation);

        alarm.setLocationRadius(cursor.getDouble(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_RADIUS)));
        alarm.setDayOfWeek(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK)));

        return alarm;
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put(AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE, getAvailable() != null && getAvailable());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_TITLE, getTitle());

        if (getTimeOfDay() != null)
            values.put(AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY, getTimeOfDay().getTime());

        if (getLocation() != null) {
            values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE, getLocation().getLatitude());
            values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE, getLocation().getLongitude());
        }

        values.put(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK, getDayOfWeek());

        return values;
    }

    public static Uri getClassUri() {
        return Uri.parse("content://com.euyuil.alarmmap.provider/alarm");
    }

    public Uri getUri() {
        return Uri.parse("content://com.euyuil.alarmmap.provider/alarm/" + getId().toString());
    }

    public boolean getAlarmDayOfWeek(Weekday weekday) {
        return dayOfWeek != null && (dayOfWeek & (1 << weekday.ordinal())) != 0;
    }

    public void setAlarmDayOfWeek(Weekday weekday, boolean flag) {
        if (dayOfWeek == null)
            dayOfWeek = 0;
        if (flag)
            dayOfWeek = dayOfWeek | (1 << weekday.ordinal());
        else
            dayOfWeek = dayOfWeek & ~(1 << weekday.ordinal());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(Date timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getLocationRadius() {
        return locationRadius;
    }

    public void setLocationRadius(Double locationRadius) {
        this.locationRadius = locationRadius;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }
}
